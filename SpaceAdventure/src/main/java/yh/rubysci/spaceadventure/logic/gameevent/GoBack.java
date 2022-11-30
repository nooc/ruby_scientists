package yh.rubysci.spaceadventure.logic.gameevent;

public class GoBack extends NegativeEventBase {
    private int steps;

    public GoBack(int steps) {
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
