package dataAccess;

import models.GameDAOModel;

import java.util.ArrayList;
import java.util.UUID;

public class MemoryGameDA {
    GameDAOModel game;

    static ArrayList<GameDAOModel> gameArr = new ArrayList<>();
    public MemoryGameDA(GameDAOModel game) {
        this.game = game;
    }

    public ArrayList<ArrayList<String>> getListGames(){
        ArrayList<ArrayList<String>> retArr = new ArrayList<>();
        for (GameDAOModel game: gameArr){
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
        GameDAOModel new_game = new GameDAOModel();
        new_game.setGameName(name);
        new_game.setGameID(UUID.randomUUID().toString());
        gameArr.add(new_game);
        return new_game.getGameID();
    }

    public void delete(){
        gameArr = new ArrayList<>();
    }
    public GameDAOModel findGame(String gameID){
        for (GameDAOModel game: gameArr){
            if (game.getGameID().equals(gameID)){
                return game;
            }
        }
        return null;
    }
}
