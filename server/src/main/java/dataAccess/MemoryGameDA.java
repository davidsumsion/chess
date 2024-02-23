package dataAccess;

import models.GameData;

import java.util.ArrayList;

public class MemoryGameDA {
    GameData game;

    static ArrayList<GameData> gameArr = new ArrayList<>();
    public MemoryGameDA(GameData game) {
        this.game = game;
    }

    public ArrayList<GameData> getListGames(){
        return gameArr;
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
