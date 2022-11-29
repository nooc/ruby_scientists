package yh.rubysci.spaceadventure;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;

public class GameController {
    @FXML
    private Canvas gameCanvas;
    @FXML
    private Label welcomeText;

    @FXML
    protected void onStartButtonClick() {

        var gfx = gameCanvas.getGraphicsContext2D();

//        Image iname;
//        gfx.drawImage();

        welcomeText.setText("Game started!");
    }
}