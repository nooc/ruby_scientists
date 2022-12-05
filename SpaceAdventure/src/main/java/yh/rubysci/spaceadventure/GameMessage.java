package yh.rubysci.spaceadventure;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Affine;
import javafx.util.Duration;
import yh.rubysci.spaceadventure.logic.IGameEvent;

import java.util.LinkedList;
import java.util.function.Function;

public class GameMessage {

    private static final String FONT_NAME = "Arial Bold";
    private static final int FONT_TITLE_SIZE = 48;
    private static final int FONT_BODY_SIZE = 24;
    private static final double LIFETIME_SECONDS = 5;
    private final LinkedList<CanvasMessage> activeMessages;
    private final Point2D startPos;
    private final Point2D endPos;
    private final Font titleFont;
    private final Font bodyFont;
    private final AnimationManager animationManager;
    private final Canvas canvas;
    public GameMessage(AnimationManager animationManager, Canvas canvas, Point2D startPos, Point2D endPos) {
        this.canvas = canvas;
        this.animationManager = animationManager;
        this.startPos = new Point2D(startPos.getX() * canvas.getWidth(), startPos.getY() * canvas.getHeight());
        this.endPos = new Point2D(endPos.getX() * canvas.getWidth(), endPos.getY() * canvas.getHeight());
        this.titleFont = Font.font(FONT_NAME, FONT_TITLE_SIZE);
        this.bodyFont = Font.font(FONT_NAME, FONT_BODY_SIZE);
        this.activeMessages = new LinkedList<>();
    }

    /**
     * Show message.
     *
     * @param gameEvent Event containing message.
     * @param roll
     */
    public void showMessage(IGameEvent gameEvent, int roll) {
        if (!gameEvent.hasMessage(roll)) {
            return;
        }
        var text = new CanvasMessage(
                gameEvent.getEventTitle(roll),
                gameEvent.getEventMessage(roll),
                startPos,
                endPos);
        text.setOnFinished(this::clearMessage);
        activeMessages.add(text);
        text.play();
        animationManager.addAnimation(text);
    }

    /**
     * Remove completed message using timeline as ref.
     *
     * @param evt
     */
    private void clearMessage(ActionEvent evt) {
        var ref = evt.getSource();
        activeMessages.remove(ref);
    }

    private class CanvasMessage implements Function<Long, Boolean> {
        final DoubleProperty xPos;
        final DoubleProperty yPos;
        final DoubleProperty alpha;
        final String title;
        final String message;
        private final Affine identity;
        private final Timeline timeline;

        public CanvasMessage(String title, String message, Point2D startPos, Point2D endPos) {
            this.identity = new Affine();
            this.xPos = new SimpleDoubleProperty();
            this.yPos = new SimpleDoubleProperty();
            this.alpha = new SimpleDoubleProperty();
            this.title = title;
            this.message = message;
            var key0 = new KeyFrame(
                    Duration.ZERO,
                    new KeyValue(xPos, startPos.getX()),
                    new KeyValue(yPos, startPos.getY()),
                    new KeyValue(alpha, 1.0)
            );
            var key1 = new KeyFrame(
                    Duration.seconds(LIFETIME_SECONDS*0.7),
                    new KeyValue(alpha, 0.9)
            );
            var key2 = new KeyFrame(
                    Duration.seconds(LIFETIME_SECONDS),
                    new KeyValue(xPos, endPos.getX()),
                    new KeyValue(yPos, endPos.getY()),
                    new KeyValue(alpha, 0.0)
            );
            timeline = new Timeline(key0, key1, key2);
            identity.setToIdentity();
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

        /**
         * Draw messages.
         *
         * @param now unused
         */
        @Override
        public Boolean apply(Long now) {
            var gfx = canvas.getGraphicsContext2D();
            gfx.save();
            // Draw all active texts.
            double currentX = xPos.get();
            double currentY = yPos.get();
            double currentAlpha = alpha.get();

            // Draw text
            gfx.setTransform(identity);
            gfx.setGlobalAlpha(currentAlpha);
            gfx.setStroke(Color.BLACK);

            var textY = currentY - titleFont.getSize() - 5;
            gfx.setFont(titleFont);
            gfx.setFill(Color.ORANGE);
            gfx.fillText(title, currentX, textY);
            gfx.strokeText(title, currentX, textY);

            gfx.setFont(bodyFont);
            gfx.setFill(Color.YELLOW);
            gfx.fillText(message, currentX, currentY);
            gfx.strokeText(message, currentX, currentY);

            gfx.restore();
            return timeline.statusProperty().get().equals(Animation.Status.RUNNING);
        }
    }
}
