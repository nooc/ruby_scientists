package yh.rubysci.spaceadventure.logic.gameevent;

public class NullEvent extends NeutralEventBase {

    @Override
    public int getMovementOffset(int roll) {
        return 0;
    }

    @Override
    public String getEventMessage(int roll) {
        return null;
    }

    @Override
    public String getEventTitle(int roll) {
        return null;
    }

    @Override
    public boolean hasMessage(int roll) {
        return false;
    }
}
