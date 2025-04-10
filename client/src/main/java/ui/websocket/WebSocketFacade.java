package ui.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exceptionhandling.DataAccessException;
import websocket.commands.UserGameCommand;
import websocket.messages.CustomServerMessageSerializer;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

//need to extend Endpoint for websocket to work properly
public class WebSocketFacade extends Endpoint {

    Session session;
    NotificationHandler notificationHandler;
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(ServerMessage.class, new CustomServerMessageSerializer())
            .create();
    String teamColor;


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

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void connect(String authToken, int gameID) throws DataAccessException {
        try {
            UserGameCommand action = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID, teamColor);
            session.getBasicRemote().sendText(action.toString());
        } catch (Exception ex) {
            throw new DataAccessException(ex.getMessage(), 500);
        }
    }
}