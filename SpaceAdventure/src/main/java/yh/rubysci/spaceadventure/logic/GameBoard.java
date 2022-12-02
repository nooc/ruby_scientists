package yh.rubysci.spaceadventure.logic;

import yh.rubysci.spaceadventure.BoardLocations;
import yh.rubysci.spaceadventure.logic.gameevent.WinOn3x6;

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
     * Handle die roll.
     *
     * @param die
     */
    public void handleRoll(int die) {
        // fix initial
        if (currentPosition < 0) currentPosition = 0;
        // test for 3x6
        consecutiveSpecial = (die == SPECIAL_DIE_VALUE) ? consecutiveSpecial + 1 : 0;
        // last index on the board
        var maxIndex = BoardLocations.getMaxIndex();
        currentPosition += die; // new position
        // fix overflow
        if (currentPosition > maxIndex) {
            currentPosition = 2 * maxIndex - currentPosition;
        }
        // test for special
        if (consecutiveSpecial == SPECIAL_WIN_COUNT) {
            onGameEvent.accept(new WinOn3x6());
        } else {
            // continue
            onGameEvent.accept(BoardLocations.getLocation(currentPosition).getEvent());
        }
    }

    /**
     * Post move won't trigger events unless on last location.
     *
     * @param steps
     */
    public void postMove(int steps) {
        currentPosition = getIndexWithOffset(steps);
        if (currentPosition == BoardLocations.getMaxIndex()) {
            onGameEvent.accept(BoardLocations.getLocation(currentPosition).getEvent());
        }
    }

    /**
     * set the event handler
     *
     * @param onGameEvent
     */
    public void setOnGameEvent(Consumer<IGameEvent> onGameEvent) {
        this.onGameEvent = onGameEvent;
    }

    /**
     * Initialize to starting state
     */
    public void initialize() {
        currentPosition = -1;
        consecutiveSpecial = 0;
    }

    /**
     * get current location
     *
     * @return
     */
    public Location getLocation() {
        return getLocation(currentPosition);
    }

    /**
     * get specific location
     *
     * @param position
     * @return
     */
    public Location getLocation(int position) {
        return position < 0 ? BoardLocations.OFF_SCREEN_LOCATION : BoardLocations.getLocation(position);
    }

    /**
     * Get new index with offset applied.
     * Will bounce on bounds.
     *
     * @param offset
     * @return offset
     */
    public int getIndexWithOffset(int offset) {
        var maxIndex = BoardLocations.getMaxIndex();
        var newIndex = currentPosition + offset;
        if (newIndex > maxIndex) {
            newIndex = maxIndex*2 - newIndex;
        } else if (newIndex < 0) {
            newIndex = -newIndex;
        }
        return newIndex;
    }
}
