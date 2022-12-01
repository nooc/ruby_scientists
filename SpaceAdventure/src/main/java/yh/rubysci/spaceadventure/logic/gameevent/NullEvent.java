package yh.rubysci.spaceadventure.logic.gameevent;

import yh.rubysci.spaceadventure.logic.IGameEvent;

public class NullEvent extends NeutralEventBase {

    @Override
    public int getMovementOffset() {
        return 0;
    }

    @Override
    public String getEventMessage() {
        return null;
    }
}
