package yh.rubysci.spaceadventure.logic.gameevent;

import yh.rubysci.spaceadventure.logic.IGameEvent;

public abstract class NeutralEventBase implements IGameEvent {
    @Override
    public String getEventSoundId() {
        return "neutral_space.ogg";
    }
}
