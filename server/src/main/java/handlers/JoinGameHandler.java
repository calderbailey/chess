package handlers;

import exceptionhandling.DataAccessException;
import requestresult.*;
import service.GameService;
import spark.Request;

public class JoinGameHandler extends Handler{
    public String handleRequest(Request jsonRequest) throws DataAccessException {
        String authToken = jsonRequest.headers("Authorization");
        JoinGameRequest joinReq = fromJson(jsonRequest, JoinGameRequest.class);
        checkAuth(authToken);
        JoinGameRequest updatedJoinReq = new JoinGameRequest(joinReq.playerColor(), joinReq.gameID(), getUsername(authToken));
        JoinGameResult joinRes = new GameService().joinGame(updatedJoinReq);
        return toJson(joinRes);
    }
}
