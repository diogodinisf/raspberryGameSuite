package pt.dinis.common.messages.invite;

import pt.dinis.common.core.GameType;

import java.util.Collection;

/**
 * Created by tiago on 16-02-2017.
 */
public class Invite extends InviteMessage {

    GameType game;
    Collection<Integer> players;

    public Invite(GameType game, Collection<Integer> players) {
        if (game == null) {
            throw new IllegalArgumentException("Answer cannot be null");
        }
        this.game = game;
        if (players == null) {
            throw new IllegalArgumentException("Players cannot be null");
        }
        this.players = players;
    }

    public GameType getGame() {
        return game;
    }

    public Collection<Integer> getPlayers() {
        return players;
    }

    @Override
    public Direction getDirection() {
        return Direction.CLIENT_TO_SERVER;
    }

    @Override
    public String toString() {
        return "Invite{" +
                "game=" + game +
                ", players=" + players +
                "} " + super.toString();
    }
}