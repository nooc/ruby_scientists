package yh.rubysci.spaceadventure;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
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
    private static final int BUTTON_STATE_INITIAL = 0;
    private static final int BUTTON_STATE_WORKING = 1;
    private static final int BUTTON_STATE_IDLE = 2;
    private static final String MOVE_NORMAL = "normal";
    private static final String MOVE_POST = "post";
    private final PlayerMover mover;
    @FXML
    private Canvas gameCanvas;
    @FXML
    private Button startButton;
    @FXML
    private Button rollButton;
    @FXML
    private MediaView rollVideoView;
    @FXML
    private ListView<String> rollLog;
    private GameBoard gameBoard;
    private MediaPlayer diePlayer;
    private MediaPlayer music;
    private Image boardBackground;
    private Image passiveImage;
    private Image thrustImage1;
    private Image thrustImage2;
    private GraphicsContext gfx;
    private int postMove;
    private int lastRolled;
    private Location moveTarget;
    private AudioClip thrustSound;
    private final Affine transform;
    private final Alert gameMessage;

    public GameController() {
        mover = new PlayerMover(this::onMoveFinished, this::onMoving);
        transform = new Affine();
        postMove = 0;
        moveTarget = null;
        gameMessage = new Alert(Alert.AlertType.INFORMATION);
    }

    private void onMoving(long now, double x, double y, double angle) {
        drawBoard();
        // rocket
        var img = ((now / 100000000) & 1) == 0 ? thrustImage1 : thrustImage2;
        transform.setToIdentity();
        transform.appendTranslation(x - img.getWidth() / 2, y - img.getHeight() * 0.8);
        transform.appendRotation(angle);
        gfx.setTransform(transform);
        gfx.drawImage(img, 0, 0);
    }

    private void drawPlayerAt(double x, double y) {
        drawBoard();
        // rocket
        transform.setToIdentity();
        transform.appendTranslation(x - passiveImage.getWidth() / 2, y - passiveImage.getHeight() * 0.8);
        gfx.setTransform(transform);
        gfx.drawImage(passiveImage, 0, 0);
    }

    @FXML
    private void onStartButtonClick() {
        startButton.setText("Restart Game");
        setButtonStates(BUTTON_STATE_WORKING);
        gameBoard.initialize();
        music.play();
        movePlayer("normal", gameBoard.getLocation(0));
    }

    @FXML
    private void onQuitButtonClick() {
        System.exit(0);
    }

    @FXML
    private void onRollButtonClick() {
        setButtonStates(BUTTON_STATE_WORKING);
        diePlayer.play();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setButtonStates(BUTTON_STATE_INITIAL);

        // create board
        gameBoard = new GameBoard();
        gameBoard.setOnGameEvent(this::onGameEventHandler);

        // load die animation into player and view
        var media = new Media(GameApplication.class.getResource("die_roll.mp4").toString());
        diePlayer = new MediaPlayer(media);
        diePlayer.setAutoPlay(false);
        rollVideoView.setMediaPlayer(diePlayer);
        diePlayer.setOnEndOfMedia(() -> {
            doDieRoll();
        });

        boardBackground = new Image(GameApplication.class.getResourceAsStream("board.png"));
        // player images
        passiveImage = new Image(GameApplication.class.getResourceAsStream("rocket_hull.png"));
        thrustImage1 = new Image(GameApplication.class.getResourceAsStream("rocket_hull_a.png"));
        thrustImage2 = new Image(GameApplication.class.getResourceAsStream("rocket_hull_b.png"));
        // sounds
        thrustSound = GameSounds.getSingleton().getSound("thruster");
        music = GameSounds.getSingleton().getMusic();
        music.setVolume(0.5);

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

    private void movePlayer(String moveType, Location target) {
        Location source = gameBoard.getLocation();
        moveTarget = target;
        if (source != target) {

            // seth movement path
            var src = source.getNormal();
            var tgt = target.getNormal();
            mover.move(
                    moveType,
                    src.distance(tgt) * MAX_MOVE_TIME,
                    src.getX() * gameCanvas.getWidth(),
                    src.getY() * gameCanvas.getHeight(),
                    tgt.getX() * gameCanvas.getWidth(),
                    tgt.getY() * gameCanvas.getHeight()
            );
            thrustSound.play();
        }
    }

    private void onMoveFinished(String moveType, double x, double y) {
        drawPlayerAt(x, y);
        thrustSound.stop();
        if (moveType.equals(MOVE_NORMAL)) {
            gameBoard.handleRoll(lastRolled);
        } else if (moveType.equals(MOVE_POST)) {
            gameBoard.postMove(postMove);
        }
    }

    private void doDieRoll() {
        diePlayer.stop();
        diePlayer.seek(Duration.ZERO);
        lastRolled = Die.roll();
        rollLog.getItems().add("Rolled a " + lastRolled);
        movePlayer("normal",
                BoardLocations.getLocation(gameBoard.getIndexWithOffset(lastRolled))
        );
    }

    private void onGameEventHandler(IGameEvent event) {
        var sound = GameSounds.getSingleton().getSound(event.getEventSoundId(lastRolled));
        if (sound != null) {
            sound.play();
        }

        if(event.hasMessage(lastRolled)) {
            gameMessage.setTitle(event.getEventTitle(lastRolled));
            gameMessage.setContentText(event.getEventMessage(lastRolled));
            gameMessage.setOnHiding(evt -> {
                setButtonStates(BUTTON_STATE_IDLE);
            });
            gameMessage.show();
        }

        if (event instanceof GameFinished) {
            finishGame(event);
            setButtonStates(BUTTON_STATE_INITIAL);
            return;
        } else {
            postMove = event.getMovementOffset(lastRolled);
            if (postMove != 0) {
                movePlayer(MOVE_POST,
                        BoardLocations.getLocation(gameBoard.getIndexWithOffset(postMove))
                );
            }
        }
        setButtonStates(BUTTON_STATE_IDLE);
    }

    private void finishGame(IGameEvent event) {
        setButtonStates(BUTTON_STATE_INITIAL);
        music.stop();
        rollLog.getItems().add("Game won!");
    }

    private void setButtonStates(int buttonsState) {
        if (buttonsState == BUTTON_STATE_INITIAL) {
            rollButton.setDisable(true);
            startButton.setDisable(false);
        } else if (buttonsState == BUTTON_STATE_WORKING) {
            rollButton.setDisable(true);
            startButton.setDisable(true);
        } else if (buttonsState == BUTTON_STATE_IDLE) {
            rollButton.setDisable(false);
            startButton.setDisable(false);
        }
    }
}