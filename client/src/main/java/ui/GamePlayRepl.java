package ui;

import model.GameData;

import static java.lang.System.exit;

public class GamePlayRepl {
    private final ChessClient client;
    private final String serverUrl;
    private boolean proceed = true;
    private GameData gameData;

    public GamePlayRepl(String serverUrl, GameData gameData) {
        this.serverUrl = serverUrl;
        this.gameData = gameData;
        client = new ChessClient(serverUrl);
    }

    public void run(){
        System.out.print("YOU HAVE REACHED THE GAMEPLAYREPL\n");
        System.out.printf(gameData.toString() + "\n\n");
        new PostLoginRepl(serverUrl).run();
    }
}
