package WSLogic;
import org.eclipse.jetty.websocket.api.Session;

import java.util.ArrayList;
import java.util.List;


public class PlayerHolder {
    List<Session> observers = new ArrayList<>();
    List<Session> players = new ArrayList<>();

    public PlayerHolder() {}

    public void addPlayer(Session session) {
        players.add(session);
    }

    public void addObserver(Session session) {
        observers.add(session);
    }

    public List<Session> getObservers() {
        return observers;
    }

    public void setObservers(List<Session> observers) {
        this.observers = observers;
    }

    public List<Session> getPlayers() {
        return players;
    }

    public void setPlayers(List<Session> players) {
        this.players = players;
    }
}
