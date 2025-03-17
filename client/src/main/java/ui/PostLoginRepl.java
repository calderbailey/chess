package ui;

public class PostLoginRepl {
    private final ChessClient client;
    private final String serverUrl;
    private boolean proceed = true;

    public PostLoginRepl(String serverUrl) {
        this.serverUrl = serverUrl;
        client = new ChessClient(serverUrl);
    }

    public void run() {
        System.out.printf("PostLoginRepl Reached \n");
    }
}
