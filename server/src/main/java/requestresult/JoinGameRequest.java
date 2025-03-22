package requestresult;

public record JoinGameRequest(String playerColor, Integer gameID, String username) {
    public JoinGameRequest {}

    public JoinGameRequest(String playerColor, Integer gameID) {
        this(playerColor, gameID, null); // or use a default value instead of null
    }
}
