package handlers;

import model.UserData;
import org.eclipse.jetty.server.Authentication;
import requestresult.LoginRequest;
import requestresult.LoginResult;
import service.UserService;
import service.UserService.*;
import spark.Request;
import spark.Response;

import com.google.gson.Gson;

public class LoginHandler {
    public String handleRequest(Request req) {
        Gson gson = new Gson();
        LoginRequest logReq = gson.fromJson(req.body(), LoginRequest.class);
        LoginResult logRes =  new UserService().login(logReq);
        return gson.toJson(logRes);
    }
}
