package yh.rubysci.spaceadventure.logic.gameevent;

public class MissingHome extends GoBack {
    public MissingHome() {
        super(10);
    }

    @Override
    public String getEventMessage(int roll) {
        return "You miss you home. Go back 10 steps.";
    }
}
