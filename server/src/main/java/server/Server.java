package server;

import exceptionhandling.*;
import handlers.*;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

//         Register your endpoints and handle exceptions here.
//
        Spark.delete("/db", (req, res) -> {
            return new DeleteHandler().handleRequest();
        });

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
