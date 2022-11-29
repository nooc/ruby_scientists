package yh.rubysci.spaceadventure.logic.gameevent;

public class MoveForward extends PositiveEventBase {
    @Override
    public int getMovementOffset() {
        return 0;
    }

    @Override
    public String getEventMessage() {
        return null;
    }
}
