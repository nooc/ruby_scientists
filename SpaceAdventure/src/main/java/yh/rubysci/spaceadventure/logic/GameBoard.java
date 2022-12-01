package yh.rubysci.spaceadventure.logic;

import yh.rubysci.spaceadventure.BoardLocations;

import java.util.function.Consumer;

public class GameBoard {
    private static final int SPECIAL_DIE_VALUE = 6;
    private static final int SPECIAL_WIN_COUNT = 3;
    private int currentPosition;
    private int consecutiveSpecial;
    private Consumer<IGameEvent> onGameEvent;

    public GameBoard() {
        initialize();
    }

    /**
     * Move after roll.
     * @param die
     */
    public void handleRoll(int die) {
        consecutiveSpecial = (die == SPECIAL_DIE_VALUE) ? consecutiveSpecial+1 : 0;
        var lastIndex = BoardLocations.getLastIndex();
        currentPosition += die;
        if(currentPosition > lastIndex) {
            currentPosition -= currentPosition % lastIndex;
        }
        if(currentPosition == lastIndex || consecutiveSpecial >= SPECIAL_WIN_COUNT) {
            currentPosition = BoardLocations.getLastIndex();
            onGameEvent.accept(BoardLocations.getLocation(lastIndex).getEvent());
        } else {
            onGameEvent.accept(BoardLocations.getLocation(currentPosition).getEvent());
        }
    }

    /**
     * Post move wont trigger events.
     * @param steps
     */
    public void postMove(int steps) {
        var lastIndex = BoardLocations.getLastIndex();
        currentPosition += steps;
        if(currentPosition > lastIndex) {
            currentPosition -= (lastIndex-currentPosition);
        } else if(currentPosition < 0) {
            currentPosition += currentPosition*(-1);
        }
    }

    public void setOnGameEvent(Consumer<IGameEvent> onGameEvent) {
        this.onGameEvent = onGameEvent;
    }

    public void initialize() {
        currentPosition = 0;
        consecutiveSpecial = 0;
    }

    public Location getLocation() {
        return BoardLocations.getLocation(currentPosition);
    }
}
