package ui;

import java.util.Scanner;

public class Client {
    private String facadeAuthToken = "";
    public static void main(String[] args) throws Exception {
        preLoginMenu();
    }

    public static void preLoginMenu(){
        System.out.printf(WELCOME_TEXT);
        System.out.print(PRELOGIN_TEXT);
        Scanner menuScanner = new Scanner(System.in);
        String menuLine = menuScanner.nextLine();
        if (menuLine.equals(Integer.toString(1))) {
            System.out.print("REGISTER REACHED");
            postLoginMenu();
        } else if (menuLine.equals(Integer.toString(2))){
            System.out.print("LOGIN REACHED");
            postLoginMenu();
        } else if (menuLine.equals(Integer.toString(3))){
            System.out.print("QUIT REACHED");
        } else if (menuLine.equals(Integer.toString(4))){ preLoginMenu(); }
    }

    public static void postLoginMenu(){
        System.out.print(LOGGED_IN_TEXT);
        System.out.print(POSTLOGIN_TEXT);
        Scanner menuScanner = new Scanner(System.in);
        String menuLine = menuScanner.nextLine();
        if (menuLine.equals(Integer.toString(1))) {
            System.out.print("LOGOUT REACHED");
        } else if (menuLine.equals(Integer.toString(2))){
            System.out.print("CREATE GAME REACHED");
        } else if (menuLine.equals(Integer.toString(3))){
            System.out.print("LIST GAMES REACHED");
        } else if (menuLine.equals(Integer.toString(4))){
            System.out.print("JOIN GAME REACHED");
        } else if (menuLine.equals(Integer.toString(5))){
            System.out.print("JOIN OBSERVER REACHED");
        } else if (menuLine.equals(Integer.toString(6))){postLoginMenu(); }
    }


    private static final String WELCOME_TEXT = "\n\n\n\n" +
            EscapeSequences.BLACK_ROOK + EscapeSequences.BLACK_ROOK + EscapeSequences.BLACK_KING+ EscapeSequences.BLACK_ROOK + EscapeSequences.BLACK_ROOK + EscapeSequences.EMPTY +
            EscapeSequences.EMPTY + EscapeSequences.WHITE_ROOK + EscapeSequences.WHITE_ROOK + EscapeSequences.WHITE_KING+ EscapeSequences.WHITE_ROOK + EscapeSequences.WHITE_ROOK +
            String.format("\n%s%s%s Welcome to Chess %s%s%s\n\n", EscapeSequences.DARK_KNIGHT, EscapeSequences.BLACK_BISHOP, EscapeSequences.BLACK_QUEEN, EscapeSequences.WHITE_QUEEN, EscapeSequences.WHITE_BISHOP, EscapeSequences.WHITE_KNIGHT) +
            "Press 4 for help\n\n";

    private static final String PRELOGIN_TEXT = "Enter an Integer:\n" +
            "1 - REGISTER\n\tCreates an Account\n\tusername, password, email required\n" +
            "2 - LOGIN\n\tTo Play Chess\n\tusername, password required\n" +
            "3 - QUIT\n\tTo Stop Playing\n" +
            "4 - HELP\n\tDisplays this menu\n>>> ";

    private static final String LOGGED_IN_TEXT = "\n\n\n\n" +
            EscapeSequences.BLACK_ROOK + EscapeSequences.BLACK_ROOK + EscapeSequences.BLACK_KING+ EscapeSequences.BLACK_ROOK + EscapeSequences.BLACK_ROOK + EscapeSequences.EMPTY +
            EscapeSequences.EMPTY + EscapeSequences.WHITE_ROOK + EscapeSequences.WHITE_ROOK + EscapeSequences.WHITE_KING+ EscapeSequences.WHITE_ROOK + EscapeSequences.WHITE_ROOK +
            String.format("\n%s%s%s   Welcome User   %s%s%s\n\n", EscapeSequences.DARK_KNIGHT, EscapeSequences.BLACK_BISHOP, EscapeSequences.BLACK_QUEEN, EscapeSequences.WHITE_QUEEN, EscapeSequences.WHITE_BISHOP, EscapeSequences.WHITE_KNIGHT);

    private static final String POSTLOGIN_TEXT = "Enter an Integer:\n" +
            "1 - LOGOUT\n\tLogs you out\n" +
            "2 - CREATE GAME\n\tName a new Game\n" +
            "3 - LIST GAMES\n\tList all Games\n" +
            "4 - JOIN GAME\n\tSpecify Game to Join\n" +
            "5 - JOIN OBSERVER\n\tSpecify Game to Spectate\n" +
            "6 - HELP\n\tDisplays this menu\n>>> ";

}
