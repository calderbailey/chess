package client;
import requestresult.*;

import java.io.IOException;
import java.net.HttpURLConnection;

public class ServerFacade {
    public ClearResult delete(ClearRequest request) {
        return null;
    }

    public LoginResult login(LoginRequest request) {
        return null;
    }

    public LogoutResult logout(LogoutRequest request) {
        return null;
    }

    public RegisterResult register(RegisterRequest request) {
        return null;
    }

    public CreateResult createGame(CreateRequest request) {
        return null;
    }

    public ListGamesResult listGames(ListGamesRequest request) {
        return null;
    }

    public JoinGameResult joinGame(JoinGameRequest request) {
        return null;
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) {
        return null;
    }
}
