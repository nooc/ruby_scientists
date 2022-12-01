package yh.rubysci.spaceadventure.logic.gameevent;

import yh.rubysci.spaceadventure.logic.IGameEvent;

public class GameFinished implements IGameEvent {
    @Override
    public String getEventSoundId(int roll) {
        return "finished";
    }

    @Override
    public int getMovementOffset(int roll) {
        return 0;
    }

    @Override
    public String getEventMessage(int roll) {
        return "You have reached the Magellan Cloud!\nCongratulations";
    }

    @Override
    public String getEventTitle(int roll) {
        return "Finished!";
    }

    @Override
    public boolean hasMessage(int roll) {
        return true;
    }
}
