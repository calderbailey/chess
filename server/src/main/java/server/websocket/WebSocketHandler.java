package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataaccess.*;
import exceptionhandling.DataAccessException;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.CustomUserGameCommandSerializer;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.*;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;


@WebSocket
public class WebSocketHandler {
    private static final MySqlAuthDAO AUTHDAO;
    private static final MySqlGameDAO GAMEDAO;

    Gson gson = new GsonBuilder()
            .registerTypeAdapter(ServerMessage.class, new CustomServerMessageSerializer())
            .registerTypeAdapter(UserGameCommand.class, new CustomUserGameCommandSerializer())
            .create();

    static {
        try {
            AUTHDAO = new MySqlAuthDAO();
            GAMEDAO = new MySqlGameDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static final HashMap<Object, Object> COLUMN_MAP;

    static {
        COLUMN_MAP = new HashMap<>();
        COLUMN_MAP.put(1, 'a');
        COLUMN_MAP.put(2, 'b');
        COLUMN_MAP.put(3, 'c');
        COLUMN_MAP.put(4, 'd');
        COLUMN_MAP.put(5, 'e');
        COLUMN_MAP.put(6, 'f');
        COLUMN_MAP.put(7, 'g');
        COLUMN_MAP.put(8, 'h');
    }

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException, InvalidMoveException, SQLException {
        UserGameCommand command = gson.fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> connect(command.getTeamColor(), command.getGameID(), command.getAuthToken(), session);
            case MAKE_MOVE -> {
                MakeMoveCommand makeMoveCommand = ((MakeMoveCommand) command);
                makeMove(makeMoveCommand.getGameID(),
                        makeMoveCommand.getAuthToken(),
                        makeMoveCommand.getMove(),
                        session);
            }
            case LEAVE -> leave(command.getAuthToken(), command.getTeamColor(), command.getGameID());
            case RESIGN -> resign(command.getAuthToken(), command.getTeamColor(), command.getGameID());
        }
    }

    private void leave(String authToken, String playerColor, int gameID) throws DataAccessException, IOException, SQLException {
        GameData gameData = GAMEDAO.getGame(gameID);
        GAMEDAO.removePlayer(playerColor, gameID);
        GAMEDAO.setGame(gameID, gameData);
        GAMEDAO.removePlayer(playerColor, gameID);
        GameData updatedGame = GAMEDAO.getGame(gameID);
        GAMEDAO.setGame(gameID, updatedGame);
        String username = AUTHDAO.getAuth(authToken).username();
        NotificationMessage broadcastNotification = new NotificationMessage(
                username + " has left the game");
        connections.broadcast(authToken, gameID, broadcastNotification);
    }

    private void resign(String authToken, String playerColor, int gameID) throws DataAccessException, IOException, SQLException {
        GameData gameData = GAMEDAO.getGame(gameID);
        ChessGame game = gameData.game();
        game.setGameComplete();
        GameData updatedGame = new GameData(
                gameID,
                gameData.whiteUsername(),
                gameData.blackUsername(),
                gameData.gameName(),
                game
        );
        GAMEDAO.setGame(gameID, updatedGame);

        String loserUsername = AUTHDAO.getAuth(authToken).username();
        String winnerUsername = null;
        if (playerColor.equals("WHITE")) {
            winnerUsername = GAMEDAO.getGame(gameID).blackUsername();
        } else if (playerColor.equals("BLACK")) {
            winnerUsername = GAMEDAO.getGame(gameID).whiteUsername();
        }
        NotificationMessage broadcastNotification = new NotificationMessage(
                "Game completed: " + loserUsername + " has resigned and " + winnerUsername + " has won" );
        connections.broadcast(null, gameID, broadcastNotification);
        LoadGameMessage loadGameMessage = new LoadGameMessage(GAMEDAO.getGame(gameID));
        connections.broadcast(null, gameID, loadGameMessage);
    }

    private void connect(String teamColor, int gameID, String authToken, Session session) throws IOException, DataAccessException {
        if (AUTHDAO.getAuth(authToken) == null || GAMEDAO.getGame(gameID) == null) {
            ErrorMessage errorMessage = new ErrorMessage("Error: Bad Input");
            session.getRemote().sendString(gson.toJson(errorMessage));
        } else {
            connections.add(authToken, gameID, session);
            LoadGameMessage notification = new LoadGameMessage(GAMEDAO.getGame(gameID));
            connections.send(authToken, notification);
            String username = AUTHDAO.getAuth(authToken).username();
            NotificationMessage broadcastNotification;
            if (teamColor != null) {
                broadcastNotification = new NotificationMessage(
                        username + " has entered the game as the " + teamColor + " player");
            } else {
                broadcastNotification = new NotificationMessage(
                        username + " has entered the game as an OBSERVER");
            }
            connections.broadcast(authToken, gameID, broadcastNotification);
        }
    }

    private void makeMove(int gameID, String authToken, ChessMove move, Session session) throws DataAccessException, InvalidMoveException, IOException {
        GameData gameData = GAMEDAO.getGame(gameID);
        try {
            gameData.game().makeMove(move);
            GAMEDAO.setGame(gameID, gameData);
            String username = AUTHDAO.getAuth(authToken).username();
            String moveDescription = username + " moved " + "(" + move.getStartPosition().getRow() + ", " +
                    COLUMN_MAP.get(move.getStartPosition().getColumn()) + ")" +
                    " to  (" + move.getEndPosition().getRow() + ", " +
                    COLUMN_MAP.get(move.getEndPosition().getRow()) + ")";
            NotificationMessage moveMessage = new NotificationMessage(moveDescription);
            connections.broadcast(authToken, gameID, moveMessage);
            LoadGameMessage notification = new LoadGameMessage(GAMEDAO.getGame(gameID));
            connections.send(authToken, notification);
            connections.broadcast(authToken, gameID, notification);
            inCheckTracker(gameData);
        } catch (Exception ex) {
            ErrorMessage errorMessage = new ErrorMessage("Invalid move");
            connections.send(authToken, errorMessage);
        }
    }

    private void inCheckTracker (GameData gameData) throws IOException, SQLException, DataAccessException {
        ChessGame.TeamColor teamInCheck = null;
        String username = null;
        if (gameData.game().isInCheck(ChessGame.TeamColor.BLACK)) {
            teamInCheck = ChessGame.TeamColor.BLACK;
            username = gameData.blackUsername();
        } else if (gameData.game().isInCheck(ChessGame.TeamColor.WHITE)) {
            teamInCheck = ChessGame.TeamColor.WHITE;
            username = gameData.whiteUsername();
        } else {
            return;
        }
        if (!checkMateTracker(gameData, teamInCheck)) {
            String notification = username + " is in check";
            NotificationMessage notificationMessage = new NotificationMessage(notification);
            connections.broadcast(null, gameData.gameID(), notificationMessage);
        }

    }

    private boolean checkMateTracker (GameData gameData, ChessGame.TeamColor teamColor) throws IOException, SQLException, DataAccessException {
        if (gameData.game().isInCheckmate(teamColor)) {
            String winnerUsername = null;
            String loserUsername = null;
            switch (teamColor) {
                case WHITE -> {
                    loserUsername = gameData.whiteUsername();
                    winnerUsername = gameData.blackUsername();
                }
                case BLACK -> {
                    loserUsername = gameData.blackUsername();
                    winnerUsername = gameData.whiteUsername();
                }
            }
            ChessGame game = gameData.game();
            game.setGameComplete();
            GameData updatedGame = new GameData(
                    gameData.gameID(),
                    gameData.whiteUsername(),
                    gameData.blackUsername(),
                    gameData.gameName(),
                    game
            );
            GAMEDAO.setGame(gameData.gameID(), updatedGame);
            String notification = loserUsername + " is in checkmate: " + winnerUsername + " wins!";
            NotificationMessage notificationMessage = new NotificationMessage(notification);
            connections.broadcast(null, gameData.gameID(), notificationMessage);
            LoadGameMessage loadGameMessage = new LoadGameMessage(gameData);
            connections.broadcast(null, gameData.gameID(), loadGameMessage);
            return true;
        }
        return false;
    }
}