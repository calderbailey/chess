package ui.websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exceptionhandling.DataAccessException;
import websocket.commands.CustomUserGameCommandSerializer;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.CustomServerMessageSerializer;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

//need to extend Endpoint for websocket to work properly
public class WebSocketFacade extends Endpoint {

    private final Session session;
    private NotificationHandler notificationHandler;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(ServerMessage.class, new CustomServerMessageSerializer())
            .registerTypeAdapter(CustomUserGameCommandSerializer.class, new CustomUserGameCommandSerializer())
            .create();
    private final String teamColor;
    private String authToken = null;
    private Integer gameID = null;

    public WebSocketFacade(String url, NotificationHandler notificationHandler, String teamColor) throws DataAccessException {
        this.teamColor = teamColor;
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = gson.fromJson(message, ServerMessage.class);
                    notificationHandler.notify(notification);
                }
            });

        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new DataAccessException(ex.getMessage(), 500);
        }
    }

    private void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    private String getAuthToken() {
        return this.authToken;
    }

    private void setGameID(Integer gameID) {
        this.gameID = gameID;
    }

    private Integer getGameID() {
        return this.gameID;
    }

    private String getTeamColor() {
        return this.teamColor;
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void connect(String authToken, int gameID) throws DataAccessException {
        try {
            setAuthToken(authToken);
            setGameID(gameID);
            UserGameCommand action = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID, teamColor);
            session.getBasicRemote().sendText(gson.toJson(action));
        } catch (Exception ex) {
            throw new DataAccessException(ex.getMessage(), 500);
        }
    }

    public void makeMove(ChessMove move) throws DataAccessException {
        try {
            MakeMoveCommand action = new MakeMoveCommand(getAuthToken(), getGameID(), getTeamColor(), move);
            session.getBasicRemote().sendText(gson.toJson(action));
        } catch (Exception ex) {
            throw new DataAccessException(ex.getMessage(), 500);
        }
    }
}