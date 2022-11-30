package yh.rubysci.spaceadventure.logic;

import javafx.scene.image.Image;
import yh.rubysci.spaceadventure.BoardLocations;
import yh.rubysci.spaceadventure.GameApplication;

public class GameBoard {
    private int currentPosition;
    private Image boardBitmap;

    public GameBoard() {
        currentPosition = 0;
        /*
        boardBitmap = new Image(
                GameApplication.class.getResourceAsStream("board.png")
        );*/
    }

    private void move(int steps) {

    }

    /**
     * Test if game has finished (game piece in last location).
     * @return
     */
    public boolean isFinished() {
        return currentPosition == (BoardLocations.LOCATIONS.length - 1);
    }
}
