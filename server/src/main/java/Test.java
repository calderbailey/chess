import dataaccess.DatabaseManager;
import dataaccess.MySqlGameDAO;
import exceptionhandling.DataAccessException;
import java.sql.Connection;
import dataaccess.*;

public class Test {
    public static void main(String[] args) throws Exception {
        // Call createDatabase() normally
        DatabaseManager.createDatabase();
        MySqlGameDAO gameDAO = new MySqlGameDAO();
        gameDAO.configureDatabase();
        // Now, use try-with-resources for the connection
        try (Connection conn = DatabaseManager.getConnection()) {
            // Use the connection as needed...
        }
    }
}
