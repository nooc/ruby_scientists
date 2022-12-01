package yh.rubysci.spaceadventure.logic.gameevent;

import yh.rubysci.spaceadventure.logic.IGameEvent;

public abstract class NegativeEventBase implements IGameEvent {
    @Override
    public String getEventSoundId(int roll) {
        return "negative";
    }
}
