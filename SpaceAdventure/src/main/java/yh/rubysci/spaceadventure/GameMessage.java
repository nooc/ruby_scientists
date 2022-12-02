package yh.rubysci.spaceadventure;

import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogEvent;
import yh.rubysci.spaceadventure.logic.IGameEvent;

public class GameMessage {
    private final Canvas canvas;
    private final Alert alert;

    public GameMessage(Canvas canvas) {
        this.canvas = canvas;
        this.alert = new Alert(Alert.AlertType.INFORMATION);
    }

    public void showMessage(IGameEvent gameEvent, int roll, EventHandler<DialogEvent> closeHandler) {
        alert.setTitle(gameEvent.getEventTitle(roll));
        alert.setContentText(gameEvent.getEventMessage(roll));
        alert.setOnHiding(closeHandler);
        alert.show();

        var ctx = canvas.getGraphicsContext2D();
        ctx.fillText(gameEvent.getEventMessage(roll), 0, 0);
    }
}
