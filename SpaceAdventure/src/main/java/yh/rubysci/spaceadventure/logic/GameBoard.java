package yh.rubysci.spaceadventure.logic;

import yh.rubysci.spaceadventure.BoardLocations;

public class GameBoard {
    private int currentPosition;
    private Location[] locations;

    public GameBoard() {
        locations = new Location[BoardLocations.LOCATIONS.length];
        currentPosition = 0;

        for(int i = 0; i< locations.length; i++) {
            locations[i] = new Location(BoardLocations.LOCATIONS[i]);
        }
    }

    private void move(int steps) {

    }
}
