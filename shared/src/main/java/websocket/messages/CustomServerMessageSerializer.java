package websocket.messages;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Objects;

import model.GameData;

public class CustomServerMessageSerializer implements JsonSerializer<ServerMessage>, JsonDeserializer<ServerMessage>{

    Gson gson = new Gson();

    @Override
    public JsonElement serialize(ServerMessage src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("serverMessageType", src.getServerMessageType().toString());
        if (src instanceof LoadGameMessage) {
            GameData game = ((LoadGameMessage) src).getGame();
            if (game != null) {
                JsonElement gameElement = gson.toJsonTree(game);
                jsonObject.add("game", gameElement);
            } else {
                jsonObject.addProperty("game", "NULL");
            }
        } else if (src instanceof NotificationMessage) {
            String message = ((NotificationMessage) src).getMessage();
            if (message != null) {
                jsonObject.addProperty("message", message);
            } else {
                jsonObject.addProperty("message", "NULL");
            }
        } else if (src instanceof ErrorMessage) {
            String errorMessage = ((ErrorMessage) src).getErrorMessage();
            jsonObject.addProperty("errorMessage", Objects.requireNonNullElse(errorMessage, "NULL"));
        }
        return jsonObject;
    }


    @Override
    public ServerMessage deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        JsonObject jsonObject = json.getAsJsonObject();

        // Extract the server message type
        String serverMessageType = jsonObject.get("serverMessageType").getAsString();
        ServerMessage serverMessage = null;

        // Deserialize based on the type of the message
        if ("LOAD_GAME".equals(serverMessageType)) {
            LoadGameMessage loadGameMessage = new LoadGameMessage(null);
            // Deserialize the GameData object if it's present
            JsonElement gameElement = jsonObject.get("game");
            if (gameElement != null && !gameElement.isJsonNull()) {
                GameData gameData = gson.fromJson(gameElement, GameData.class);
                loadGameMessage.setGame(gameData);
            }
            serverMessage = loadGameMessage;
        } else if ("NOTIFICATION".equals(serverMessageType)) {
            NotificationMessage notificationMessage = new NotificationMessage(null);
            // Deserialize the message object if it's present
            JsonElement messageElement = jsonObject.get("message");
            if (messageElement != null && !messageElement.isJsonNull()) {
                String message = gson.fromJson(messageElement, String.class);
                notificationMessage.setMessage(message);
            }
            serverMessage = notificationMessage;
        } else if ("ERROR".equals(serverMessageType)) {
            ErrorMessage errorMessage = new ErrorMessage(null);
            // Deserialize the message object if it's present
            JsonElement errorMessageElement = jsonObject.get("errorMessage");
            if (errorMessageElement != null && !errorMessageElement.isJsonNull()) {
                String message = gson.fromJson(errorMessageElement, String.class);
                errorMessage.setErrorMessage(message);
            }
            serverMessage = errorMessage;
        }
        return serverMessage;
    }
}