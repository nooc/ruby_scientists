package yh.rubysci.spaceadventure;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.util.Duration;

public class PlayerMover {
    private final FinishedHandler finishedHandler;
    private final InterpolationHandler movementHandler;
    private final DoubleProperty xPos;
    private final DoubleProperty yPos;
    private final DoubleProperty angle;
    private final AnimationManager manager;
    private Timeline timeline;
    private boolean _isMoving;

    public PlayerMover(AnimationManager manager, FinishedHandler finishedHandler, InterpolationHandler movementHandler) {
        this.manager = manager;
        this.finishedHandler = finishedHandler;
        this.movementHandler = movementHandler;
        xPos = new SimpleDoubleProperty();
        yPos = new SimpleDoubleProperty();
        angle = new SimpleDoubleProperty();
        _isMoving = false;
    }

    /**
     * Initiate a timed transform for ship.
     *
     * @param moveType String type passed to event handler.
     * @param seconds  Number of seconds to animate.
     * @param x0       Starting x.
     * @param y0       Starting y.
     * @param x1
     * @param y1
     */
    public void move(String moveType, double seconds, double x0, double y0, double x1, double y1) {
        double tilt = x1 > x0 ? 45 : -45; // calculate tilt
        var key0 = new KeyFrame(
                Duration.ZERO,
                new KeyValue(xPos, x0),
                new KeyValue(yPos, y0),
                new KeyValue(angle, 0)
        );
        var key1 = new KeyFrame(
                Duration.seconds(seconds / 2),
                new KeyValue(angle, tilt)
        );
        var key2 = new KeyFrame(
                Duration.seconds(seconds),
                new KeyValue(xPos, x1),
                new KeyValue(yPos, y1),
                new KeyValue(angle, 0)
        );
        timeline = new Timeline(key0, key1, key2);
        _isMoving = true;
        timeline.play();
        manager.addAnimation(now -> {
            _isMoving = timeline.statusProperty().get().equals(Animation.Status.RUNNING);
            if (_isMoving) {
                movementHandler.currentValue(now, xPos.get(), yPos.get(), angle.get());
            } else {
                finishedHandler.handleMoveFinished(moveType, x1, y1);
                return false;
            }
            return true;
        });
    }

    public boolean isMoving() {
        return _isMoving;
    }

    public interface InterpolationHandler {
        void currentValue(long now, double x, double y, double angle);
    }

    public interface FinishedHandler {
        void handleMoveFinished(String moveType, double x, double y);
    }
}
