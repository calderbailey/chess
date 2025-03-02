package server;

import dataaccess.MemoryUserDAO;
import handlers.LoginHandler;
import handlers.RegisterHandler;
import model.UserData;
import org.eclipse.jetty.util.log.Log;
import requestresult.LoginRequest;
import requestresult.RegisterResult;
import spark.*;

import com.google.gson.Gson;

import javax.xml.namespace.QName;
import java.util.HashMap;
import java.util.Map;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

//         Register your endpoints and handle exceptions here.
//
        Spark.post("/login", (req, res) -> {
            return new LoginHandler().handleRequest(req);
        });

        Spark.post("/register", (req, res) -> {
            return new RegisterHandler().handleRequest(req, res);
        });


        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
