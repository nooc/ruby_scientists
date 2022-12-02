package yh.rubysci.spaceadventure.logic.gameevent;

public class GoBack extends NegativeEventBase {
    private final int steps;

    public GoBack(int steps) {
        this.steps = steps;
    }

    @Override
    public int getMovementOffset(int roll) {
        return -steps;
    }

    @Override
    public String getEventMessage(int roll) {
        return "The pull of a black hole has thrown you back " + steps + " steps.";
    }

    @Override
    public String getEventTitle(int roll) {
        return "Go Back";
    }

    @Override
    public boolean hasMessage(int roll) {
        return true;
    }
}
