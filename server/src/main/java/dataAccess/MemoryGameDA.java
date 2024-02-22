package dataAccess;

import models.GameData;

import java.util.ArrayList;
import java.util.UUID;

public class MemoryGameDA {
    GameData game;

    static ArrayList<GameData> gameArr = new ArrayList<>();
    public MemoryGameDA(GameData game) {
        this.game = game;
    }

    public ArrayList<String> getListGames(){
//        ArrayList<ArrayList<String>> retArr = new ArrayList<>();
        ArrayList<String> retArr = new ArrayList<>();
        for (GameData game: gameArr){
            retArr.add(game.toString());
//            ArrayList<String> gameInf = new ArrayList<String>();
////            gameInf.add(game.getGameID());
//            gameInf.add(String.format("gameID: %d", game.getGameID()));
//            gameInf.add(game.getWhiteUsername());
//            gameInf.add(game.getBlackUsername());
//            gameInf.add(game.getGameName());
//            retArr.add(gameInf);
        }
        return retArr;
    }

    public boolean createGame(){
        for (GameData gameData: gameArr){
            if (gameData.getGameName().equals(game.getGameName())){
                return false;
            }
        }
        gameArr.add(game);
        return true;
    }

    public void delete(){
        gameArr = new ArrayList<>();
    }
    public GameData findGame(Integer gameID){
        for (GameData game: gameArr){
            if (game.getGameID() == gameID){
                return game;
            }
        }
        return null;
    }
}
