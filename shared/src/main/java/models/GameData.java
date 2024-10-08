package models;

public class GameData {
    private Integer gameID;
    private String whiteUsername;
    private String blackUsername;
    private String gameName;
    private String chessGame;
    private static int counter = 0;
    public GameData() {}
    public GameData(Integer id, String whiteUsername, String blackUsername, String gameName){
        this.gameID = id;
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.gameName = gameName;
    }
    public int getGameID() {
        return gameID;
    }
    public String getWhiteUsername() {
        return whiteUsername;
    }
    public String getBlackUsername() {
        return blackUsername;
    }
    public String getGameName() {
        return gameName;
    }
    public void setGameID() {
        counter += 1;
        this.gameID = counter;
    }
    public void setGameID(Integer id) {
        this.gameID = id;
    }
    public void setWhiteUsername(String whiteUsername) {
        this.whiteUsername = whiteUsername;
    }

    public void setBlackUsername(String blackUsername) {
        this.blackUsername = blackUsername;
    }

    public String getChessGame() { return chessGame; }

    public void setChessGame(String chessGame) { this.chessGame = chessGame; }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
    public void setColor(String color, String username){
        if (color == null) {return;}
        if (color.equals("BLACK")){
            setBlackUsername(username);
        } else if (color.equals("WHITE")){
           setWhiteUsername(username);
        }
    }
    public void resetCounter(){
        counter = 0;
    }
    @Override
    public String toString() {
        return "{gameID=" + this.gameID + ", gameName='" + this.gameName + "', whiteUsername='" + this.whiteUsername + "', blackUsername='" + this.blackUsername + "'}";
    }
}
