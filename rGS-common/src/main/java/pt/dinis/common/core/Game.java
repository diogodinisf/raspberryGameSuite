package pt.dinis.common.core;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by tiago on 02-08-2017.
 */
public class Game implements Serializable {

    Integer id;
    GameType game;
    Player host;
    DateTime date;

    public Game(Integer id, GameType game, Player host, DateTime date) {
        this.id = id;
        this.game = game;
        this.host = host;
        this.date = date;
    }

    public Player getHost() {
        return host;
    }

    public DateTime getDate() {
        return date;
    }

    public Integer getId() {
        return id;
    }

    public GameType getGame() {
        return game;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", game=" + game +
                ", host=" + host +
                ", date=" + date +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Game)) return false;
        Game game1 = (Game) o;
        return Objects.equals(getId(), game1.getId()) &&
                getGame() == game1.getGame() &&
                Objects.equals(getHost(), game1.getHost()) &&
                Objects.equals(getDate(), game1.getDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getGame(), getHost(), getDate());
    }
}
