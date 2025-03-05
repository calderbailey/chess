package handlers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import model.GameData;
import java.lang.reflect.Type;
import com.google.gson.JsonNull;

public class CustomGameDataSerializer implements JsonSerializer<GameData> {
    @Override
    public JsonElement serialize(GameData src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("gameID", src.gameID());
        if (src.whiteUsername() == null) {
            jsonObject.add("whiteUsername", JsonNull.INSTANCE);
        } else {
            jsonObject.addProperty("whiteUsername", src.whiteUsername());
        }
        if (src.blackUsername() == null) {
            jsonObject.add("blackUsername", JsonNull.INSTANCE);
        } else {
            jsonObject.addProperty("blackUsername", src.blackUsername());
        }
        jsonObject.addProperty("gameName", src.gameName());
        return jsonObject;
    }
}