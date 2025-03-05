package handlers;

import exceptionhandling.DataAccessException;
import requestresult.ClearRequest;
import requestresult.ClearResult;
import service.UserService;

public class DeleteHandler extends Handler{
    public String handleRequest() throws DataAccessException {
        ClearResult clearRes = new UserService().clear(new ClearRequest());
        return toJson(clearRes);
    }
}
