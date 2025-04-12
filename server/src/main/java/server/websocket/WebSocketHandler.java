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

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException, InvalidMoveException {
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
        }
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
            NotificationMessage broadcastNotification = new NotificationMessage(username + " has entered the game as the " + teamColor + " player");
            connections.broadcast(authToken, gameID, broadcastNotification);
        }
    }

    private void makeMove(int gameID, String authToken, ChessMove move, Session session) throws DataAccessException, InvalidMoveException {
        GameData gameData = GAMEDAO.getGame(gameID);
        try {
            gameData.game().makeMove(move);
            GAMEDAO.setGame(gameID, gameData);
            LoadGameMessage notification = new LoadGameMessage(GAMEDAO.getGame(gameID));
            connections.send(authToken, notification);
        } catch (Exception ex) {
            System.out.print(ex.getMessage());
        }
    }
}