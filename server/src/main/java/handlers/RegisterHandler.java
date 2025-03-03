package handlers;

import exceptionhandling.DataAccessException;
import requestresult.RegisterRequest;
import requestresult.RegisterResult;
import service.UserService;
import spark.Request;
import spark.Response;

public class RegisterHandler extends Handler {
    public String handleRequest(Request  jsonRequest) throws DataAccessException{
        RegisterRequest regReq = fromJson(jsonRequest, RegisterRequest.class);
        RegisterResult regRes = new UserService().register(regReq);
        return toJson(regRes);
    }
}
