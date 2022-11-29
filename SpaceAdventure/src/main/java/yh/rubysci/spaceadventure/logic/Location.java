package yh.rubysci.spaceadventure.logic;

import javafx.geometry.Point2D;

public class Location {
    private IGameEvent event;
    private Point2D position;

    public Location(Point2D position) {
        this.position = position;
    }
}
