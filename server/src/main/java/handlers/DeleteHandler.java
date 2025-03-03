package handlers;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import exceptionhandling.DataAccessException;
import model.AuthData;
import requestresult.ClearResult;

import javax.xml.crypto.Data;

public class DeleteHandler extends Handler{
    public String handleRequest() throws DataAccessException {
        new MemoryAuthDAO().clear();
        new MemoryUserDAO().clear();
        return toJson(new ClearResult());
    }
}
