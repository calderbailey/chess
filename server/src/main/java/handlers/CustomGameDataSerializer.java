package handlers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import model.GameData;
import java.lang.reflect.Type;

public class CustomGameDataSerializer implements JsonSerializer<GameData> {
    @Override
    public JsonElement serialize(GameData src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("gameID", src.gameID());
        if (src.whiteUsername() == null) {
            jsonObject.addProperty("whiteUsername", "");
        } else {
            jsonObject.addProperty("whiteUsername", src.whiteUsername());
        }
        if (src.blackUsername() == null) {
            jsonObject.addProperty("blackUsername", "");
        } else {
            jsonObject.addProperty("blackUsername", src.blackUsername());
        }
        jsonObject.addProperty("gameName", src.gameName());
        return jsonObject;
    }
}