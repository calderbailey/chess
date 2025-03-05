package handlers;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import exceptionhandling.DataAccessException;
import requestresult.ClearResult;

public class DeleteHandler extends Handler{
    public String handleRequest() throws DataAccessException {
        new MemoryAuthDAO().clear();
        new MemoryUserDAO().clear();
        new MemoryGameDAO().clear();
        return toJson(new ClearResult());
    }
}
