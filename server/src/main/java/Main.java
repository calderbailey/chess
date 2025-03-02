import InMemoryDatabases.UserDatabase;
import chess.*;
import server.Server;

public class Main {
    public static void main(String[] args) {
        UserDatabase userDatabase = new UserDatabase();
        Server server = new Server();
        server.run(8080);
    }
}