package pt.dinis.common.messages.basic;

/**
 * Created by tiago on 16-02-2017.
 */
public class CloseConnectionOrder extends BasicMessage {

    @Override
    public Direction getDirection() {
        return Direction.SERVER_TO_CLIENT;
    }

    @Override
    public String toString() {
        return "CloseConnectionOrder{} " + super.toString();
    }
}
