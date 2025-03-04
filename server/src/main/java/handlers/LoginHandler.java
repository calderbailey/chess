package handlers;

import exceptionhandling.DataAccessException;
import requestresult.LoginRequest;
import requestresult.LoginResult;
import service.UserService;
import spark.Request;

public class LoginHandler  extends Handler{
    public String handleRequest (Request  jsonRequest) throws DataAccessException {
        LoginRequest request = fromJson(jsonRequest, LoginRequest.class);
        LoginResult logRes =  new UserService().login(request);
        return toJson(logRes);
    }
}
