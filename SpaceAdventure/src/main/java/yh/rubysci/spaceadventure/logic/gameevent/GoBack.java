package yh.rubysci.spaceadventure.logic.gameevent;

public class GoBack extends NegativeEventBase{
    @Override
    public int getMovementOffset() {
        return 0;
    }

    @Override
    public String getEventMessage() {
        return null;
    }
}
