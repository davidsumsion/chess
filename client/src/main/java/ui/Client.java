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
            //register
            registerUI();
        } else if (menuLine.equals(Integer.toString(2))) {
            //login
            loginUI();
        } else if (menuLine.equals(Integer.toString(3))) {
            System.out.print("Goodbye! Play again soon!");
        } else if (menuLine.equals(Integer.toString(4))) {
            //display menu again
            preLoginMenu();
        } else {
            System.out.println("Enter an Integer, not a word");
            preLoginMenu();
        }

    }

    public static void postLoginMenu(){
        System.out.print(LOGGED_IN_TEXT);
        System.out.print(POSTLOGIN_TEXT);
        Scanner menuScanner = new Scanner(System.in);
        String menuLine = menuScanner.nextLine();
        if (menuLine.equals(Integer.toString(1))) {
            logoutUI();
        } else if (menuLine.equals(Integer.toString(2))){
            createGameUI();
        } else if (menuLine.equals(Integer.toString(3))){
            listGamesUI();
        } else if (menuLine.equals(Integer.toString(4))){
            joinToPlayGameUI();
        } else if (menuLine.equals(Integer.toString(5))){
            joinToObserveUI();
//            System.out.print("JOIN OBSERVER REACHED");
        } else if (menuLine.equals(Integer.toString(6))){
            postLoginMenu();
        } else {
            System.out.println("Enter an Integer, not a word");
            postLoginMenu();
        }
    }

    public static void registerUI(){
        System.out.print("Enter a Username\n>>> ");
        Scanner usernameScanner = new Scanner(System.in);
        String username = usernameScanner.nextLine();

        System.out.print("Enter a Password\n>>> ");
        Scanner passwordScanner = new Scanner(System.in);
        String password = passwordScanner.nextLine();

        System.out.print("Enter an Email\n>>> ");
        Scanner emailScanner = new Scanner(System.in);
        String email = emailScanner.nextLine();

        ServerFacade serverFacade = new ServerFacade();
        String dbUsername = serverFacade.register(username, password, email);
        if (dbUsername.equals("User Already Exists")){
            System.out.println("User Already Exists");
            preLoginMenu();
        } else {
            System.out.println("Welcome " + dbUsername);
            postLoginMenu();
        }
    }

    public static void logoutUI(){
        ServerFacade serverFacade = new ServerFacade();
        String result = serverFacade.logout();
        if (result.equals("")){
            System.out.println("Goodbye, play again soon ");
            preLoginMenu();
        } else if (result.equals("Start the Server")){
            System.out.println("Start the Server");
            preLoginMenu();
        } else if (result.equals("Unauthorized: AuthToken not in Database")){
            System.out.println("Unauthorized: AuthToken not in Database");
            preLoginMenu();
        }

    }

    public static void joinToPlayGameUI(){
        System.out.print("Enter a gameID\n>>> ");
        Scanner gameIDScanner = new Scanner(System.in);
        String gameID = gameIDScanner.nextLine();

        System.out.print("Choose a color\n\t1 - BLACK\n\t2 - WHITE\n>>> ");
        Scanner colorScanner = new Scanner(System.in);
        String colorChecker = colorScanner.nextLine();
        String playerColor = "";
        if ((colorChecker.equals(Integer.toString(1)))) {
            playerColor = "BLACK";
        } else {
            playerColor = "WHITE";
        }

        ServerFacade serverFacade = new ServerFacade();
        String result = serverFacade.joinGamePlayer(gameID, playerColor);
        if (result.isEmpty()) {
            System.out.format("Enjoy your game, you are %s\n", playerColor);
            if (playerColor.equals("WHITE")) {
                String[] args = new String[]{"WHITE"};
                ChessBoardUI.main(args);
            } else {
                String[] args = new String[]{"BLACK"};
                ChessBoardUI.main(args);
            }

        } else if (result.equals("Start the Server")) {
            System.out.println("Start the Server");
            preLoginMenu();
        } else if (result.equals("Unauthorized")) {
            System.out.println("please log in before playing");
            preLoginMenu();
        } else if (result.equals("Error Game")) {
            System.out.println("Game Does not exist");
            postLoginMenu();
        } else if (result.equals("Error Color")) {
            System.out.println("Game Color already occupied");
            postLoginMenu();
        }
    }

    public static void joinToObserveUI(){
        System.out.print("Enter a gameID\n>>> ");
        Scanner gameIDScanner = new Scanner(System.in);
        String gameID = gameIDScanner.nextLine();

        ServerFacade serverFacade = new ServerFacade();
        serverFacade.joinGamePlayer(gameID, null);
        System.out.println("Enjoy your game ");
        String[] args = new String[]{"nothing"};
        ChessBoardUI.main(args);
    }

    public static void loginUI() {
        System.out.print("Enter a Username\n>>> ");
        Scanner usernameScanner = new Scanner(System.in);
        String username = usernameScanner.nextLine();

        System.out.print("Enter a Password\n>>> ");
        Scanner passwordScanner = new Scanner(System.in);
        String password = passwordScanner.nextLine();

        ServerFacade serverFacade = new ServerFacade();
        String dbUsername = serverFacade.login(username, password);
        if (dbUsername.equals("Username or Password Incorrect")) {
            System.out.println("Username or Password Incorrect");
            preLoginMenu();
        } else if (dbUsername.equals("Start the Server")) {
            System.out.println("Start the Server");
            preLoginMenu();
        } else {
            System.out.println("Welcome " + dbUsername);
            postLoginMenu();
        }

    }
    public static void createGameUI(){
        System.out.print("Enter a new Game Name\n>>> ");
        Scanner nameScanner = new Scanner(System.in);
        String gameName = nameScanner.nextLine();

        ServerFacade serverFacade = new ServerFacade();
        String dbGameID = serverFacade.createGame(gameName);
        if (dbGameID.equals("Game Already Exists or Unauthorized")) {
            System.out.println("Game Already Exists with this name or Unauthorized (need to login)");
            postLoginMenu();
        } else if (dbGameID.equals("Start the Server")) {
            System.out.println("Start the Server");
            postLoginMenu();
        } else {
            System.out.print("You created " + gameName + " with ID: "+ dbGameID);
            postLoginMenu();
        }
    }

    public static void listGamesUI(){
        ServerFacade serverFacade = new ServerFacade();
        String games = serverFacade.listGames();
        if (games.equals("Start the Server")) {
            System.out.println("Start the Server");
            preLoginMenu();
        } else if (games.equals("Unauthorized: AuthToken not in Database")) {
            System.out.println("Unauthorized");
            preLoginMenu();
        } else {
            System.out.print(games);
            postLoginMenu();
        }
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

    private static final String LOGGED_IN_TEXT = "\n" +
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
