package pt.dinis.common.messages.invite;

import pt.dinis.common.core.Game;

import java.util.List;

/**
 * Created by tiago on 16-02-2017.
 */
public class ListOfInvitesAnswer extends InviteMessage {

    AnswerType answer;
    // TODO: justification should be an enum
    String errorJustification;
    List<Game> games;

    public ListOfInvitesAnswer(AnswerType answer, String errorJustification, List<Game> games) {
        if (answer == null) {
            throw new IllegalArgumentException("Answer cannot be null");
        }
        this.answer = answer;
        this.errorJustification = errorJustification;
        if (games == null) {
            throw new IllegalArgumentException("Games cannot be null");
        }
        this.games = games;
    }

    public AnswerType getAnswer() {
        return answer;
    }

    public String getErrorJustification() {
        return errorJustification;
    }

    public List<Game> getGames() {
        return games;
    }

    @Override
    public Direction getDirection() {
        return Direction.SERVER_TO_CLIENT;
    }

    @Override
    public String toString() {
        return "ListOfGamesAnswer{" +
                "answer=" + answer +
                ", errorJustification='" + errorJustification + '\'' +
                ", games=" + games +
                "} " + super.toString();
    }
}
