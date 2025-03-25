package handlers;

import exceptionhandling.DataAccessException;
import requestresult.LogoutRequest;
import requestresult.LogoutResult;
import service.UserService;
import spark.Request;

public class LogoutHandler extends Handler{
    public String handleRequest (Request jsonRequest) throws DataAccessException {
        String authToken = jsonRequest.headers("Authorization");
        checkAuth(authToken);
        LogoutRequest request = new LogoutRequest(authToken);
        LogoutResult result =  new UserService().logout(request);
        return toJson(result);
    }
}
