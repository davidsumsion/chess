package dataAccess;

import models.GameDAOModel;

import java.util.ArrayList;

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

}
