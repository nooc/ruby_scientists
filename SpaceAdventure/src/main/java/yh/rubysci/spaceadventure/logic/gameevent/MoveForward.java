package yh.rubysci.spaceadventure.logic.gameevent;

public class MoveForward extends PositiveEventBase {
    private int steps;

    public MoveForward(int steps) {
        this.steps = steps;
    }

    @Override
    public int getMovementOffset() {
        return steps;
    }

    @Override
    public String getEventMessage() {
        return null;
    }
}
