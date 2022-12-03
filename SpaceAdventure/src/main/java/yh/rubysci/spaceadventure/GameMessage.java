package yh.rubysci.spaceadventure;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.util.Duration;
import yh.rubysci.spaceadventure.logic.IGameEvent;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class GameMessage {

    private static final double LIFETIME_SECONDS = 5;
    private class CanvasMessage {
        final DoubleProperty xPos;
        final DoubleProperty yPos;
        final DoubleProperty alpha;
        private Timeline timeline;
        final String title;
        final String message;

        public CanvasMessage(String title, String message, Point2D startPos, Point2D endPos) {
            this.xPos = new SimpleDoubleProperty();
            this.yPos = new SimpleDoubleProperty();
            this.alpha = new SimpleDoubleProperty();
            this.title = title;
            this.message = message;
            var key0 = new KeyFrame(
                    Duration.ZERO,
                    new KeyValue(xPos,startPos.getX()),
                    new KeyValue(yPos, startPos.getY()),
                    new KeyValue(alpha, 1.0)
            );
            var key1 = new KeyFrame(
                    Duration.seconds(LIFETIME_SECONDS),
                    new KeyValue(xPos, endPos.getX()),
                    new KeyValue(yPos, endPos.getY()),
                    new KeyValue(alpha, 0.0)
            );
            timeline = new Timeline(key0, key1);
        }

        @Override
        public boolean equals(Object obj) {
            return timeline.equals(obj);
        }

        public void setOnFinished(EventHandler<ActionEvent> handler) {
            timeline.setOnFinished(handler);
        }
        void play() {
            timeline.play();
        }
    }

    private final Canvas canvas;
    private final LinkedList<CanvasMessage> activeMessages;
    private final AnimationTimer timer;
    private final Point2D startPos;
    private final Point2D endPos;

    public GameMessage(Canvas canvas, Point2D startPos, Point2D endPos) {
        this.canvas = canvas;
        this.startPos = startPos;
        this.endPos = endPos;

        this.activeMessages = new LinkedList<>();
        timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                drawMessages(l);
            }
        };
    }

    /**
     * Show message.
     * TODO: Change from dialogue to canvas.
     *
     * @param gameEvent Event containing message.
     * @param roll
     */
    public void showMessage(IGameEvent gameEvent, int roll) {
        if (!gameEvent.hasMessage(roll)) { return; }
        var text = new CanvasMessage(
                gameEvent.getEventTitle(roll),
                gameEvent.getEventMessage(roll),
                startPos,
                endPos);
        text.setOnFinished(this::clearMessage);
        activeMessages.add(text);
        text.play();
        timer.start();
    }

    /**
     * Remove completed message using timeline as ref.
     * @param evt
     */
    private void clearMessage(ActionEvent evt) {
        var ref = evt.getSource();
        activeMessages.remove(ref);
    }

    /**
     * Draw messages.
     * @param l unused
     */
    private void drawMessages(long l) {

        // Draw all active texts.
        for (var message: activeMessages) {
            var title = message.title;
            var body = message.message;
            double currentX = message.xPos.get();
            double currentY = message.xPos.get();
            double currentAlpha = message.alpha.get();

            //TODO: Draw text
        }

        // Stop timer if no more messages.
        if (activeMessages.isEmpty()) {
            timer.stop();
        }
    }
}
