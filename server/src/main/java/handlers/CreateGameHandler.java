package handlers;

import exceptionhandling.DataAccessException;
import requestresult.CreateRequest;
import requestresult.CreateResult;
import service.GameService;
import spark.Request;

public class CreateGameHandler extends Handler{
    public String handleRequest(Request jsonRequest) throws DataAccessException {
        String authToken = jsonRequest.headers("Authorization");
        CreateRequest createReq = fromJson(jsonRequest, CreateRequest.class);
        checkAuth(authToken);
        CreateResult createResult = new GameService().createGame(createReq);
        return toJson(createResult);
    }
}
