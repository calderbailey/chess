package server;

import com.google.gson.Gson;
import exceptionhandling.*;
import handlers.LoginHandler;
import handlers.RegisterHandler;
import spark.*;

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
            return new RegisterHandler().handleRequest(req);
        });

        Spark.exception(DataAccessException.class, this::exceptionHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private void exceptionHandler(DataAccessException ex, Request req, Response res) {
        res.status(ex.getStatusCode());
        res.body(ex.toJson());
    }
}
