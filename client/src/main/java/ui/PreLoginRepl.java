package ui;

import exceptionhandling.DataAccessException;
import ui.websocket.NotificationHandler;
import websocket.messages.ServerMessage;

import java.util.Scanner;
import static ui.EscapeSequences.*;

public class PreLoginRepl implements NotificationHandler {
    private final ChessClient client;
    private final String serverUrl;
    private boolean proceed = true;

    public PreLoginRepl(String serverUrl) {
        this.serverUrl = serverUrl;
        client = new ChessClient(serverUrl, this);
    }

    public void run() {
        System.out.println("Welcome to Chess! Type Help to get started.");
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (proceed) {
            printPrompt();
            String[] userInput = parseInput(scanner.nextLine());
            try {
                checkInput(userInput);
                eval(userInput);
            } catch (Throwable e) {
                var msg = e.getMessage();
                System.out.printf("\n" + SET_TEXT_ITALIC + "*** " + SET_TEXT_COLOR_RED + msg + RESET_TEXT_COLOR + " ***" + "\n\n");
                System.out.print(help());
            }
        }
        System.exit(1);
    }

    private void eval(String[] userInput) throws DataAccessException {
        switch (userInput[0].toLowerCase()) {
            case "register":
                System.out.printf(client.register(userInput) + "\n");
                new PostLoginRepl(serverUrl).run();
                break;
            case "login":
                System.out.printf(client.login(userInput) + "\n");
                new PostLoginRepl(serverUrl).run();
                break;
            case "help":
                System.out.printf(help());
                break;
            case "quit":
                System.out.printf(quit());
                break;
        }
    }

    private void printPrompt() {
        System.out.printf("[" + CurrentState.Logged_Out + "] >>> ");
    }

    private String[] parseInput(String userInput) {
        return userInput.split(" ");
    }

    private void checkInput(String [] arguments) throws IllegalArgumentException{
        int actualArgs = arguments.length;
        int expectedArgs = switch (arguments[0].toLowerCase()) {
            case "register" -> 4;
            case "login" -> 3;
            case "help", "quit" -> 1;
            default -> throw new IllegalArgumentException("Unknown command: " + arguments[0]);
        };
        if (actualArgs != expectedArgs) {
            throw new IllegalArgumentException("ERROR: Invalid Number of Arguments --" +
                    "Note: no spaces are allowed within arguments");
        }
    }

    private String help() {
        return (SET_TEXT_COLOR_BLUE + "register <USERNAME> <PASSWORD> <EMAIL> " +
                SET_TEXT_COLOR_MAGENTA + "- to create an account\n" +
                SET_TEXT_COLOR_BLUE + "login <USERNAME> <PASSWORD> " +
                SET_TEXT_COLOR_MAGENTA + "- to play chess\n" +
                SET_TEXT_COLOR_BLUE + "quit " +
                SET_TEXT_COLOR_MAGENTA + "- playing chess\n" +
                SET_TEXT_COLOR_BLUE + "help " +
                SET_TEXT_COLOR_MAGENTA + "- with possible commands\n" +
                RESET_TEXT_COLOR);
    }

    private String quit() {
        proceed = false;
        return (SET_TEXT_COLOR_BLUE + "Thanks for playing! Goodbye.");
    }

    @Override
    public void notify(ServerMessage notification) {
        System.out.println(SET_TEXT_COLOR_RED + notification.getServerMessageType() + RESET_TEXT_COLOR);
        printPrompt();
    }
}
