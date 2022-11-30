package yh.rubysci.spaceadventure.logic.gameevent;

import yh.rubysci.spaceadventure.logic.IGameEvent;

public class GameFinished implements IGameEvent {
    @Override
    public String getEventSoundId() {
        return "game_finished.mp4";
    }

    @Override
    public int getMovementOffset() {
        return 0;
    }

    @Override
    public String getEventMessage() {
        return "Finished!";
    }
}
