package yh.rubysci.spaceadventure.logic.gameevent;

public class MoveForward extends PositiveEventBase {
    private final int steps;

    public MoveForward(int steps) {
        this.steps = steps;
    }

    @Override
    public int getMovementOffset(int roll) {
        return steps;
    }

    @Override
    public String getEventMessage(int roll) {
        return "Yuo found a wormhole. Move forward " + steps + " steps!";
    }

    @Override
    public String getEventTitle(int roll) {
        return "Move Forward";
    }

    @Override
    public boolean hasMessage(int roll) {
        return true;
    }
}
