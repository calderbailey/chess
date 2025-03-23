package ui;

import static java.lang.System.exit;

public class GamePlayRepl {
    private final ChessClient client;
    private final String serverUrl;
    private boolean proceed = true;

    public GamePlayRepl(String serverUrl) {
        this.serverUrl = serverUrl;
        client = new ChessClient(serverUrl);
    }

    public void run(){
        System.out.print("YOU HAVE REACHED THE GAMEPLAYREPL\n\n");
        new PostLoginRepl(serverUrl).run();
    }
}
