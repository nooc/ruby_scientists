package yh.rubysci.spaceadventure.logic.gameevent;

import yh.rubysci.spaceadventure.logic.IGameEvent;

public abstract class PositiveEventBase implements IGameEvent {
    @Override
    public String getEventSoundId() {
        return "positive";
    }
}
