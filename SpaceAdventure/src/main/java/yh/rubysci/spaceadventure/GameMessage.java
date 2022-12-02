package yh.rubysci.spaceadventure;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogEvent;
import yh.rubysci.spaceadventure.logic.IGameEvent;

import java.util.function.Consumer;

public class GameMessage {
    private final Canvas canvas;
    private final Alert alert;

    public GameMessage(Canvas canvas) {
        this.canvas = canvas;
        this.alert = new Alert(Alert.AlertType.INFORMATION);
    }

    /**
     * Show message.
     * TODO: Change from dialogue to canvas.
     * @param gameEvent Event containing message.
     * @param roll
     */
    public void showMessage(IGameEvent gameEvent, int roll) {
        if(gameEvent.hasMessage(roll)) {
            alert.setTitle(gameEvent.getEventTitle(roll));
            alert.setContentText(gameEvent.getEventMessage(roll));
            alert.show();
        }
    }
}
