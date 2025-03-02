package handlers;
import requestresult.RegisterRequest;
import requestresult.RegisterResult;
import service.UserService;
import spark.Request;
import spark.Response;

import com.google.gson.Gson;

public class RegisterHandler {
    public String handleRequest(Request req, Response res) {
        Gson gson = new Gson();
        RegisterRequest regReq = gson.fromJson(req.body(), RegisterRequest.class);
        RegisterResult regRes =  new UserService().register(regReq);
        res.body(gson.toJson(regRes));
        return res.body();
    }
}
