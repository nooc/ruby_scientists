package yh.rubysci.spaceadventure.logic;

import yh.rubysci.spaceadventure.BoardLocations;

import java.util.function.Consumer;

public class GameBoard {
    private int currentPosition;
    private Consumer<IGameEvent> onGameEvent;

    public GameBoard() {
        initialize();
    }

    /**
     * Move after roll.
     * @param steps
     */
    public void move(int steps) {

    }

    /**
     * Move after effect.
     * @param steps
     */
    public void postMove(int steps) {

    }

    /**
     * Test if game has finished (game piece in last location).
     * @return
     */
    public boolean isFinished() {
        return currentPosition == (BoardLocations.LOCATIONS.length - 1);
    }

    public void setOnGameEvent(Consumer<IGameEvent> onGameEvent) {
        this.onGameEvent = onGameEvent;
    }



    public void initialize() {
        currentPosition = 0;
    }
}
