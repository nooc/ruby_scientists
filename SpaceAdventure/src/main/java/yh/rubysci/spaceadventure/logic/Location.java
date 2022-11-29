package yh.rubysci.spaceadventure.logic;

import javafx.geometry.Point2D;

public class Location {
    private IGameEvent event;
    private Point2D position;

    public Location( double normalX, double normalY, IGameEvent event) {

        this.position = new Point2D(normalX, normalY);
        this.event = event;
    }
}
