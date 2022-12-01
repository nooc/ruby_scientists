package yh.rubysci.spaceadventure;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Affine;
import javafx.util.Duration;
import yh.rubysci.spaceadventure.logic.Die;
import yh.rubysci.spaceadventure.logic.GameBoard;
import yh.rubysci.spaceadventure.logic.IGameEvent;
import yh.rubysci.spaceadventure.logic.Location;
import yh.rubysci.spaceadventure.logic.gameevent.GameFinished;

import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements javafx.fxml.Initializable {
    private static final double MAX_MOVE_TIME = 5;
    @FXML
    private Canvas gameCanvas;
    @FXML
    private Button startButton;
    @FXML
    private MediaView rollVideoView;

    private GameBoard gameBoard;
    private MediaPlayer diePlayer;
    private Image boardBackground;
    private Image passiveImage;
    private Image thrustImage1;
    private Image thrustImage2;
    private GraphicsContext gfx;
    private final PlayerMover mover;
    private final Paint debugPaint;
    private Affine transform;
    public GameController() {
        mover = new PlayerMover(this::onMoveFinished, this::onMoving);
        debugPaint = Color.BLUE;
        transform = new Affine();
    }

    private void onMoving(long now, double x, double y, double angle) {
        drawBoard();
        // rocket
        var img = ((now/100000000)&1) == 0 ? thrustImage1 : thrustImage2;
        transform.setToIdentity();
        transform.appendTranslation(x- img.getWidth()/2, y - img.getHeight() * 0.8);
        transform.appendRotation(angle);
        gfx.setTransform(transform);
        gfx.drawImage(img, 0, 0);
    }
    private void finishedMoving(double x, double y) {
        drawBoard();
        // rocket
        transform.setToIdentity();
        transform.appendTranslation(x- passiveImage.getWidth()/2, y - passiveImage.getHeight() * 0.8);
        gfx.setTransform(transform);
        gfx.drawImage(passiveImage, 0, 0);
    }


    private void onMoveFinished(Object payload, double x, double y) {
        finishedMoving(x,y);
        // TODO: stop thruster
    }

    @FXML
    private void onStartButtonClick() {
        startButton.setText("Restart Game");
        gameBoard.initialize();
        var loc = gameBoard.getLocation();
        movePlayer(BoardLocations.OFF_SCREEN_LOCATION, loc);
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

        passiveImage = new Image(GameApplication.class.getResourceAsStream("rocket_hull.png"));
        thrustImage1 = new Image(GameApplication.class.getResourceAsStream("rocket_hull_a.png"));
        thrustImage2 = new Image(GameApplication.class.getResourceAsStream("rocket_hull_b.png"));

        // Get graphics context for drawing
        gfx = gameCanvas.getGraphicsContext2D();
        // Draw board
        drawBoard();
    }

    private void drawBoard() {

        // Draw background
        transform.setToIdentity();
        gfx.setTransform(transform);
        gfx.drawImage(boardBackground, 0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
    }

    private void movePlayer(Location source, Location target) {
        // seth movement path
        var src = source.getNormal();
        var tgt = target.getNormal();
        mover.move(
            target,
            src.distance(tgt) * MAX_MOVE_TIME,
            src.getX() * gameCanvas.getWidth(),
            src.getY() * gameCanvas.getHeight(),
            tgt.getX() * gameCanvas.getWidth(),
            tgt.getY() * gameCanvas.getHeight()
        );
    }

    private void doDieRoll() {
        diePlayer.stop();
        diePlayer.seek(Duration.ZERO);
        var steps = Die.roll();
        gameBoard.handleRoll(steps);
    }

    private void onGameEventHandler(IGameEvent event) {
        var sound = GameSounds.getSingleton().getSound(event.getEventSoundId());
        if(sound!=null) {
            sound.play();
        }

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