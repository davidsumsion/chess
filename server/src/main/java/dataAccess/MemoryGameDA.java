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

    public ArrayList<ArrayList<String>> getListGames(){
        ArrayList<ArrayList<String>> retArr = new ArrayList<>();
        for (GameData game: gameArr){
            ArrayList<String> gameInf = new ArrayList<String>();
            gameInf.add(game.getGameID());
            gameInf.add(game.getWhiteUsername());
            gameInf.add(game.getBlackUsername());
            gameInf.add(game.getGameName());
            retArr.add(gameInf);
        }
        return retArr;
    }

    public String createGame(String name){
        GameData new_game = new GameData();
        new_game.setGameName(name);
        new_game.setGameID(UUID.randomUUID().toString());
        gameArr.add(new_game);
        return new_game.getGameID();
    }

    public void delete(){
        gameArr = new ArrayList<>();
    }
    public GameData findGame(String gameID){
        for (GameData game: gameArr){
            if (game.getGameID().equals(gameID)){
                return game;
            }
        }
        return null;
    }
}
