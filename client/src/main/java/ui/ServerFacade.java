package ui;

import exceptionhandling.DataAccessException;
import requestresult.*;
import com.google.gson.Gson;
import java.io.*;
import java.net.*;
import java.net.HttpURLConnection;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public ClearResult delete(ClearRequest request) throws DataAccessException {
        var path = "/db";
        return this.makeRequest("DELETE", path, null, null, null);
    }

    public LoginResult login(LoginRequest request) throws DataAccessException {
        var path = "/session";
        return this.makeRequest("POST", path, request, LoginResult.class, null);
    }

    public LogoutResult logout(LogoutRequest request, String authToken) throws DataAccessException {
        var path = "/session";
        return this.makeRequest("DELETE", path, request, LogoutResult.class, authToken);
    }

    public RegisterResult register(RegisterRequest request) throws DataAccessException {
        var path = "/user";
            return this.makeRequest("POST", path, request, RegisterResult.class, null);
    }

    public CreateResult createGame(CreateRequest request, String authToken) throws DataAccessException {
        var path = "/game";
        return this.makeRequest("POST", path, request, CreateResult.class, authToken);
    }

    public ListGamesResult listGames(ListGamesRequest request, String authToken) throws DataAccessException {
        var path = "/game";
        return this.makeRequest("GET", path, request, ListGamesResult.class, authToken);
    }

    public JoinGameResult joinGame(JoinGameRequest request, String authToken) throws DataAccessException {
        var path = "/game";
        return this.makeRequest("PUT", path, request, JoinGameResult.class, authToken);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String authToken) throws DataAccessException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            if (!method.equals("GET")){
                http.setDoOutput(true);
            }
            http.setRequestProperty("Authorization", authToken);
            if (!method.equals("GET")){
                writeBody(request, http);
            }
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (DataAccessException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new DataAccessException(ex.getMessage(), 500);
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, DataAccessException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    throw DataAccessException.fromJson(respErr);
                }
            }

            throw new DataAccessException("other failure: " + status, status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
