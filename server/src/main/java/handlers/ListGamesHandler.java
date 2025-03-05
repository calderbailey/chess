package handlers;

import exceptionhandling.DataAccessException;
import requestresult.ListGamesRequest;
import requestresult.ListGamesResult;
import service.GameService;
import spark.Request;

public class ListGamesHandler extends Handler{
    public String handleRequest(Request jsonRequest) throws DataAccessException {
        String authToken = jsonRequest.headers("Authorization");
        ListGamesRequest listReq = new ListGamesRequest();
        checkAuth(authToken);
        ListGamesResult listRes = new GameService().listGames(listReq);
        return toJson(listRes);
    }
}
