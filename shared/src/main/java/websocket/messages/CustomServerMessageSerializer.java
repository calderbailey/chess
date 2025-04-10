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
        }

        return serverMessage;
    }
}