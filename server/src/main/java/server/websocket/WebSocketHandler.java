package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataaccess.*;
import exceptionhandling.DataAccessException;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;
import websocket.messages.CustomServerMessageSerializer;

import javax.print.attribute.standard.Severity;
import java.io.IOException;
import java.util.Timer;


@WebSocket
public class WebSocketHandler {
    private static MySqlAuthDAO AUTHDAO;
    private static MySqlGameDAO GAMEDAO;

    Gson gson = new GsonBuilder()
            .registerTypeAdapter(ServerMessage.class, new CustomServerMessageSerializer())
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
    public void onMessage(Session session, String message) throws IOException, DataAccessException {
        UserGameCommand command = gson.fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> Connect(command.getTeamColor(), command.getGameID(), command.getAuthToken(), session);
        }
    }

    private void Connect(String teamColor, int gameID, String authToken, Session session) throws IOException, DataAccessException {
        connections.add(authToken, gameID, session);
        LoadGameMessage notification = new LoadGameMessage(GAMEDAO.getGame(gameID));
        connections.send(authToken, notification);
        String username = AUTHDAO.getAuth(authToken).username();
        NotificationMessage broadcastNotification = new NotificationMessage(username + " has entered the game as the " + teamColor + " player");
        connections.broadcast(authToken, gameID, broadcastNotification);
    }
}