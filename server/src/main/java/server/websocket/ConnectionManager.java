package server.websocket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.CustomServerMessageSerializer;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.print.attribute.standard.Severity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(ServerMessage.class, new CustomServerMessageSerializer())
            .create();

    public void add(String authToken, int gameID, Session session) {
        Connection connection = new Connection(gameID, session);
        connections.put(authToken, connection);
    }

    public void remove(String authToken) {
        connections.remove(authToken);
    }

    public void send(String authToken, ServerMessage notification) throws IOException {
        Connection connection = connections.get(authToken);
        connection.send(gson.toJson(notification, ServerMessage.class));
    }

    public void broadcast(String excludeAuthToken, int gameID, ServerMessage notification) throws IOException {
        var removeList = new ArrayList<String>();
        for (String c : connections.keySet()) {
            Connection connection = connections.get(c);
            if (connection.session.isOpen()) {
                if (connection.gameID == gameID && !Objects.equals(c, excludeAuthToken)) {
                    connection.send(gson.toJson(notification, NotificationMessage.class));
                }
            } else {
                removeList.add(c);
            }
        }
        // Clean up any connections that were left open.
        for (var c : removeList) {
            remove(c);
        }
    }
}