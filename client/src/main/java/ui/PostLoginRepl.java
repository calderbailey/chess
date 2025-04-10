package ui;

import com.google.gson.Gson;
import exceptionhandling.DataAccessException;
import model.GameData;
import ui.websocket.NotificationHandler;
import ui.websocket.WebSocketFacade;
import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessage;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class PostLoginRepl {
    private final ChessClient client;
    private final String serverUrl;
    private boolean proceed = true;

    public PostLoginRepl (String serverUrl) {
        this.serverUrl = serverUrl;
        client = new ChessClient(serverUrl);
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        var result = "";
        printPrompt();
        while (proceed) {
            String[] userInput = parseInput(scanner.nextLine());
            try {
                checkInput(userInput);
                eval(userInput);
            } catch (Throwable e) {
                var msg = e.getMessage();
                System.out.print("\n" + "*** " + SET_TEXT_COLOR_RED + msg + RESET_TEXT_COLOR + " ***" + "\n");
                System.out.print(help());
            }
        }
        System.exit(1);
    }


    private void eval(String[] userInput) throws DataAccessException {
        switch (userInput[0].toLowerCase()) {
            case "create":
                System.out.printf(client.create(userInput) + "\n");
                break;
            case "list":
                System.out.printf(client.list() + "\n");
                break;
            case "join":
                client.join(userInput);
                break;
            case "observe":
                JoinObserveResult observeResult = client.observe(userInput);
                System.out.printf(observeResult.gamePrintString());
                new GamePlayRepl(serverUrl, "WHITE", "Observing").run();
                break;
            case "logout":
                System.out.print("\n");
                new PreLoginRepl(serverUrl).run();
                break;
            case "help":
                System.out.printf(help());
                break;
        }
    }


    private String[] parseInput(String userInput) {
        return userInput.split(" ");
    }

    private void checkInput(String [] arguments) throws IllegalArgumentException{
        int actualArgs = arguments.length;
        int expectedArgs = switch (arguments[0].toLowerCase()) {
            case "join" -> 3;
            case "create", "observe" -> 2;
            case "list", "logout", "help" -> 1;
            default -> throw new IllegalArgumentException("Unknown command: " + arguments[0]);
        };
        if (actualArgs != expectedArgs) {
            throw new IllegalArgumentException("ERROR: Invalid Number of Arguments --" +
                                               "Note: no spaces are allowed within arguments");
        }
    }


    private void printPrompt() {
        System.out.printf("[" + CurrentState.Logged_In + "] >>> ");
    }

    private String help() {
        return (SET_TEXT_COLOR_BLUE + "create <NAME> " +
                SET_TEXT_COLOR_MAGENTA + "- a game\n" +
                SET_TEXT_COLOR_BLUE + "list " +
                SET_TEXT_COLOR_MAGENTA + "- games\n" +
                SET_TEXT_COLOR_BLUE + "join <ID> [WHITE|BLACK] " +
                SET_TEXT_COLOR_MAGENTA + "- a game\n" +
                SET_TEXT_COLOR_BLUE + "observe <ID> " +
                SET_TEXT_COLOR_MAGENTA + "- a game\n" +
                SET_TEXT_COLOR_BLUE + "logout " +
                SET_TEXT_COLOR_MAGENTA + "- when you are done\n" +
                SET_TEXT_COLOR_BLUE + "help " +
                SET_TEXT_COLOR_MAGENTA + "- with possible commands\n" +
                RESET_TEXT_COLOR);
    }
}
