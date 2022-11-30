package yh.rubysci.spaceadventure;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import yh.rubysci.spaceadventure.logic.Die;
import yh.rubysci.spaceadventure.logic.GameBoard;
import yh.rubysci.spaceadventure.logic.IGameEvent;
import yh.rubysci.spaceadventure.logic.Location;
import yh.rubysci.spaceadventure.logic.gameevent.GameFinished;

import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements javafx.fxml.Initializable {
    @FXML
    private Canvas gameCanvas;
    @FXML
    private Button startButton;
    @FXML
    private MediaView rollVideoView;

    private GameBoard gameBoard;
    private MediaPlayer diePlayer;
    private Image boardBackground;
    private GraphicsContext gfx;
    private final AnimationTimer moveTimer = new AnimationTimer() {
        @Override
        public void handle(long l) {
            handleMove(l);
        }
    };

    @FXML
    private void onStartButtonClick() {
        startButton.setText("Restart Game");
        gameBoard.initialize();
    }

    @FXML
    private void onQuitButtonClick() {

    }

    @FXML
    private void onRollButtonClick() {
        diePlayer.play();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // create board
        gameBoard = new GameBoard();
        gameBoard.setOnGameEvent(this::onGameEventHandler);

        // load die animation into player and view
        var media = new Media(GameApplication.class.getResource("die_roll.mp4").toString());
        diePlayer = new MediaPlayer(media);
        diePlayer.setAutoPlay(false);
        rollVideoView.setMediaPlayer(diePlayer);
        diePlayer.setOnEndOfMedia(() -> { doDieRoll(); });
        boardBackground = new Image(GameApplication.class.getResourceAsStream("board.png"));

        // Get graphics context for drawing
        gfx = gameCanvas.getGraphicsContext2D();

        // Draw board
        drawBoard();
        movePlayer(null, BoardLocations.getLocation(0).getNormal());
    }

    private void drawBoard() {
        // Draw background
        gfx.drawImage(boardBackground, 0, 0);
    }

    private void movePlayer(Point2D source, Point2D target) {
        if(source == null) {
            source = new Point2D(-0.2, -0.2);
        }

    }

    private void handleMove(long l) {
    }


    private void doDieRoll() {
        diePlayer.stop();
        diePlayer.seek(Duration.ZERO);
        var steps = Die.roll();
        gameBoard.move(steps);
    }

    private void onGameEventHandler(IGameEvent event) {
        //TODO: Play sound
        var soundId = event.getEventSoundId();

        //TODO: Show message
        var message = event.getEventMessage();

        if(event instanceof GameFinished) {
            finishGame(event);
        } else {
            var move = event.getMovementOffset();
            if(move != 0) {
                gameBoard.postMove(move);
            }
        }
    }

    private void finishGame(IGameEvent event) {
    }
}