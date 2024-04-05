package ui;
import chess.*;
import com.google.gson.Gson;
import models.GameData;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;

import java.util.Collection;
import java.util.Map;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class Client {

    private ChessGame.TeamColor myColor = null;
    private ChessGame.TeamColor teamTurn = null;
    private ChessBoard chessBoard = null;

    private Integer gameID = null;

    private Boolean observer = false;
    ServerFacade serverFacade = new ServerFacade(this::receiveMessage);

    public static void main(String[] args) throws Exception {
        Client client = new Client();
        client.preLoginMenu();
    }

    private void receiveMessage(String message) {
        Gson gson = new Gson();
        ServerMessage serverMessage = gson.fromJson(message, ServerMessage.class);
        System.out.print("RECEVIED MESSAGE");

        switch (serverMessage.getServerMessageType()){
            case LOAD_GAME -> {
                System.out.print("LOAD GAME\n");
                LoadGame loadGame = gson.fromJson(message, LoadGame.class);
                GameData gameData = gson.fromJson(loadGame.getGame(), GameData.class);
                ChessGame chessGame = gson.fromJson(gameData.getChessGame(), ChessGame.class);
                teamTurn = chessGame.getTeamTurn();
                chessBoard = chessGame.getBoard();
                drawBoard(loadGame.getGame());
                if (!observer) gameplayUI();
            }
            case ERROR -> {
//                System.out.print("ERROR");
                Error error = gson.fromJson(message, Error.class);
                System.out.print(error.getErrorMessage());
            }
            case NOTIFICATION -> {
                System.out.print("NOTIFICATION");
                Notification notification = gson.fromJson(message, Notification.class);
                System.out.print(notification.getMessage());
            }
        }
    }

    public void preLoginMenu(){
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

    public void postLoginMenu(){
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
        } else if (menuLine.equals(Integer.toString(6))){
            postLoginMenu();
        } else {
            System.out.println("Enter an Integer, not a word");
            postLoginMenu();
        }
    }


    private String getDummyData(){
        ChessBoard chessBoard = new ChessBoard();
        chessBoard.resetBoard();
        Gson gson = new Gson();
        return gson.toJson(chessBoard);
    }

    public void drawBoard(String gameDataJson){

        String color = "BLACK";
        if (myColor == null || myColor.equals(ChessGame.TeamColor.WHITE)){
            color = "WHITE";
        }
        String[] args = new String[]{color, gameDataJson}; //GAME BOARD
        ChessBoardUI.main(args);
    }
    public void gameplayUI() {
//        private static final String GAMEPLAY_TEXT = "Enter an Integer:\n" +
//                "1 - Help\n\tDisplays This Menu\n" +
//                "2 - Redraw Chess Board\n\tRedraws the board\n" +
//                "3 - Leave\n\tRemove yourself from the game\n" +
//                "4 - Make Move\n\tMove a piece\n" +
//                "5 - Resign\n\tForfeit the game and Game is over\n" +
//                "6 - Highlight Legal Moves\n\tSee All Legal Moves for a piece\n>>> ";

        System.out.print(GAMEPLAY_TEXT);
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        if (input.equals(Integer.toString(1))) {
            gameplayUI();
        }
        else if (input.equals(Integer.toString(2))) {
            GameData printableGameData = new GameData(null, null, null, null);
            chess.ChessGame printableChessGame = new ChessGame();
            printableChessGame.setBoard(chessBoard);
            Gson gson = new Gson();
            printableGameData.setChessGame(gson.toJson(printableChessGame));
            String stringChessBoard = gson.toJson(printableGameData);
            drawBoard(stringChessBoard);
            gameplayUI();
        }
        else if (input.equals(Integer.toString(3))) {
            // Leave
            serverFacade.leave(gameID);
            System.out.print("Thanks for playing!");
            postLoginMenu();
        }
        else if (input.equals(Integer.toString(4))) {
            // Make Move
        }
        else if (input.equals(Integer.toString(5))) {
            // Resign
            System.out.print("Are you sure you want to leave resign??\n You will forfeit the game");
        }
        else if (input.equals(Integer.toString(6))) {
            // highlight Legal Moves
            Gson gson = new Gson();
//            highlightMovesUI(myColor, chessBoard);
            gameplayUI();
        }
    }

    public void highlightMovesUI(String color, ChessBoard chessBoard) {
        System.out.print("Enter the position of the piece you want to see the potential moves for\n");
        System.out.print("Enter a Row Number\n>>> ");
        Scanner rowScanner = new Scanner(System.in);
        String row = rowScanner.nextLine();
        //verify 1-8
        System.out.print("Enter a Column Letter\n>>> ");
        Scanner colScanner = new Scanner(System.in);
        String col = colScanner.nextLine();
        Integer colNum = colNumberMap.get(col);

        ChessPosition currentPosition = new ChessPosition(parseInt(row), colNum);
        ChessPiece currentPiece = chessBoard.getPiece(currentPosition);
        System.out.print(currentPiece);
        System.out.print(currentPosition);
        System.out.println();
        if (currentPiece.getTeamColor().toString().equals(color)) {
            Collection<ChessMove> moves = currentPiece.pieceMoves(chessBoard, currentPosition);
            System.out.print(moves);
            System.out.println();
            Gson gson = new Gson();
            String[] args = new String[]{color, gson.toJson(chessBoard), gson.toJson(moves)};
            ChessBoardUI.main(args);
        }

    }

    public void registerUI(){
        System.out.print("Enter a Username\n>>> ");
        Scanner usernameScanner = new Scanner(System.in);
        String username = usernameScanner.nextLine();

        System.out.print("Enter a Password\n>>> ");
        Scanner passwordScanner = new Scanner(System.in);
        String password = passwordScanner.nextLine();

        System.out.print("Enter an Email\n>>> ");
        Scanner emailScanner = new Scanner(System.in);
        String email = emailScanner.nextLine();

        String dbUsername = serverFacade.register(username, password, email);
        if (dbUsername.equals("User Already Exists")){
            System.out.println("User Already Exists");
            preLoginMenu();
        } else {
            System.out.println("Welcome " + dbUsername);
            postLoginMenu();
        }
    }

    public void logoutUI(){
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

    public void joinToPlayGameUI(){
        System.out.print("Enter a gameID\n>>> ");
        Scanner gameIDScanner = new Scanner(System.in);
        String currentGameID = gameIDScanner.nextLine();

        System.out.print("Choose a color\n\t1 - BLACK\n\t2 - WHITE\n>>> ");
        Scanner colorScanner = new Scanner(System.in);
        String colorChecker = colorScanner.nextLine();
        String currentPlayerColor = "";
        if ((colorChecker.equals(Integer.toString(1)))) {
            currentPlayerColor = "BLACK";
        } else {
            currentPlayerColor = "WHITE";
        }

        String result = serverFacade.joinPlayer(currentGameID, currentPlayerColor);
        if (result.isEmpty()) {
            System.out.format("Enjoy your game, you are %s\n", currentPlayerColor);
            gameID = parseInt(currentGameID);
            if (currentPlayerColor.equals("WHITE")) {
                myColor = ChessGame.TeamColor.WHITE;
                gameplayUI();
            } else {
                myColor = ChessGame.TeamColor.BLACK;
                gameplayUI();
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

    public void joinToObserveUI(){
        System.out.print("Enter a gameID\n>>> ");
        Scanner gameIDScanner = new Scanner(System.in);
        String gameID = gameIDScanner.nextLine();

        String result =  serverFacade.joinPlayer(gameID, null);
        if (result.isEmpty()) {
            System.out.format("Enjoy the show\n");
        } else if (result.equals("Start the Server")) {
            System.out.println("Start the Server");
            preLoginMenu();
        } else if (result.equals("Unauthorized")) {
            System.out.println("please log in before playing");
            preLoginMenu();
        } else if (result.equals("Error Game")) {
            System.out.println("Game Does not exist");
            postLoginMenu();
        }
    }

    public void loginUI() {
        System.out.print("Enter a Username\n>>> ");
        Scanner usernameScanner = new Scanner(System.in);
        String username = usernameScanner.nextLine();

        System.out.print("Enter a Password\n>>> ");
        Scanner passwordScanner = new Scanner(System.in);
        String password = passwordScanner.nextLine();

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

    public void createGameUI(){
        System.out.print("Enter a new Game Name\n>>> ");
        Scanner nameScanner = new Scanner(System.in);
        String gameName = nameScanner.nextLine();

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

    public void listGamesUI(){
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

    final Map<String, Integer> colNumberMap = Map.of(
            "A", 1,
            "B", 2,
            "C", 3,
            "D", 4,
            "E", 5,
            "F", 6,
            "G", 7,
            "H", 8);
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
    private static final String GAMEPLAY_TEXT = "Enter an Integer:\n" +
            "1 - Help\n\tDisplays This Menu\n" +
            "2 - Redraw Chess Board\n\tRedraws the board\n" +
            "3 - Leave\n\tRemove yourself from the game\n" +
            "4 - Make Move\n\tMove a piece\n" +
            "5 - Resign\n\tForfeit the game and Game is over\n" +
            "6 - Highlight Legal Moves\n\tSee All Legal Moves for a piece\n>>> ";
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
