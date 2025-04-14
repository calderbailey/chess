package websocket.commands;

import chess.ChessMove;
import com.google.gson.*;
import java.lang.reflect.Type;

public class CustomUserGameCommandSerializer implements JsonSerializer<UserGameCommand>, JsonDeserializer<UserGameCommand>{

    Gson gson = new Gson();

    @Override
    public JsonElement serialize(UserGameCommand src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("commandType", src.getCommandType().toString());
        jsonObject.addProperty("teamColor", src.getTeamColor());
        jsonObject.addProperty("gameID", src.getGameID().toString());
        jsonObject.addProperty("authToken", src.getAuthToken());

        if (src instanceof MakeMoveCommand) {
            ChessMove move = ((MakeMoveCommand) src).getMove();
            if (move != null) {
                JsonElement moveElement = gson.toJsonTree(move);
                jsonObject.add("move", moveElement);
            } else {
                jsonObject.addProperty("move", "NULL");
            }
        }
        return jsonObject;
    }


    @Override
    public UserGameCommand deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        JsonObject jsonObject = json.getAsJsonObject();
        UserGameCommand returnCommand = null;

        // Extract the command type
        String commandTypeString = jsonObject.get("commandType").getAsString();
        UserGameCommand.CommandType commandType = typeFormatter(commandTypeString);

        // Deserialize all commands but MakeMove
        if (!UserGameCommand.CommandType.MAKE_MOVE.equals(commandType)) {
            String authToken = null;
            JsonElement authTokenElement = jsonObject.get("authToken");
            if (authTokenElement != null && !authTokenElement.isJsonNull()) {
                authToken = authTokenElement.getAsString();
            }
            String teamColor = null;
            JsonElement teamColorElement = jsonObject.get("teamColor");
            if (teamColorElement != null && !teamColorElement.isJsonNull()) {
                teamColor = teamColorElement.getAsString();
            }
            UserGameCommand userGameCommand = new UserGameCommand(
                    commandType,
                    authToken,
                    jsonObject.get("gameID").getAsInt(),
                    teamColor
            );
            returnCommand = userGameCommand;
        }

        // Deserialize Make Move
        if (UserGameCommand.CommandType.MAKE_MOVE.equals(commandType)) {
            String authToken = null;
            JsonElement authTokenElement = jsonObject.get("authToken");
            if (authTokenElement != null && !authTokenElement.isJsonNull()) {
                authToken = authTokenElement.getAsString();
            }
            String teamColor = null;
            JsonElement teamColorElement = jsonObject.get("teamColor");
            if (teamColorElement != null && !teamColorElement.isJsonNull()) {
                teamColor = teamColorElement.getAsString();
            }
            MakeMoveCommand makeMoveCommand = new MakeMoveCommand(
                    authToken,
                    jsonObject.get("gameID").getAsInt(),
                    teamColor,
                    null);
            // Deserialize the ChessMove object if it's present
            JsonElement moveElement = jsonObject.get("move");
            if (moveElement != null && !moveElement.isJsonNull()) {
                ChessMove chessMove = gson.fromJson(moveElement, ChessMove.class);
                makeMoveCommand.setMove(chessMove);
            }
            returnCommand = makeMoveCommand;
        }
        return returnCommand;
    }

    private UserGameCommand.CommandType typeFormatter(String stringType) {
        UserGameCommand.CommandType type = null;
        switch (stringType) {
            case "CONNECT" -> type = UserGameCommand.CommandType.CONNECT;
            case "MAKE_MOVE" -> type = UserGameCommand.CommandType.MAKE_MOVE;
            case "LEAVE" -> type = UserGameCommand.CommandType.LEAVE;
            case "RESIGN" -> type = UserGameCommand.CommandType.RESIGN;
        }
        return type;
    }
}