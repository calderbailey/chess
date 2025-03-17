package ui;

import exceptionhandling.DataAccessException;

import java.util.Scanner;
import static ui.EscapeSequences.*;

public class PreLoginRepl {
    private final ChessClient client;
    private boolean proceed = true;


    public PreLoginRepl(String serverUrl) {
        client = new ChessClient(serverUrl);
    }

    public void run() {
        System.out.println(WHITE_QUEEN + "Welcome to Chess! Type Help to get started." + WHITE_QUEEN + "\n");
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (proceed) {
            printPrompt();
            String[] userInput = parseInput(scanner.nextLine());
            try {
                checkInput(userInput);
                result = eval(userInput);
                System.out.print(result + RESET_TEXT_COLOR  + "\n");
            } catch (Throwable e) {
                var msg = e.getMessage();
                System.out.printf("\n" + SET_TEXT_ITALIC + "*** " + SET_TEXT_COLOR_RED + msg + RESET_TEXT_COLOR + " ***" + "\n\n");
                System.out.print(help());
            }
        }
        System.exit(1);
    }

    private String eval(String[] userInput) throws DataAccessException {
        switch (userInput[0].toLowerCase()) {
            case "register":
                return client.Register(userInput);
            case "login":
                return client.Login(userInput);
            case "help":
                return help();
            case "quit":
                return quit();
        }
        return null;
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
            throw new IllegalArgumentException("ERROR: Invalid Number of Arguments.");
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
}
