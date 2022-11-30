package yh.rubysci.spaceadventure;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

public class PlayerMover {

    public interface InterpolationHandler {
        void currentValue(long now, double x, double y, double angle);
    }
    public interface FinishedHandler {
        void handleMoveFinisdhed(Object payload, double x, double y);
    }

    private static final double FPS = 10;
    private DoubleProperty xPos;
    private DoubleProperty yPos;
    private DoubleProperty angle;
    private Timeline timeline;
    private final AnimationTimer timer;
    private final FinishedHandler finishedHandler;
    private final InterpolationHandler movementHandler;

    public PlayerMover(FinishedHandler finishedHandler, InterpolationHandler movementHandler) {
        this.finishedHandler = finishedHandler;
        this.movementHandler = movementHandler;
        xPos  = new SimpleDoubleProperty();
        yPos  = new SimpleDoubleProperty();
        angle  = new SimpleDoubleProperty();
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                movementHandler.currentValue(now, xPos.get(), yPos.get(), angle.get());
            }
        };
    }

    public void move(Object payload, double seconds, double x0, double y0, double x1, double y1) {
        double tilt = 45; // TODO: calculate tilt
        var key0 = new KeyFrame(
                Duration.ZERO,
                new KeyValue(xPos, x0),
                new KeyValue(yPos, y0),
                new KeyValue(angle, 0)
        );
        var key1 = new KeyFrame(
                Duration.seconds(seconds/2),
                new KeyValue(angle, tilt)
        );
        var key2 = new KeyFrame(
                Duration.seconds(seconds),
                new KeyValue(xPos, x1),
                new KeyValue(yPos, y1),
                new KeyValue(angle, 0)
        );
        timeline = new Timeline(key0, key1, key2);
        timeline.setOnFinished(evt -> {
            timer.stop();
            finishedHandler.handleMoveFinisdhed(payload, x1, y1);
        });
        timeline.play();
        timer.start();
    }
}
