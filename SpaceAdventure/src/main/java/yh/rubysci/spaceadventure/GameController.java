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
    private static final double ADJUST_PLAYER_HEIGHT_MULTIPLIER = 0.8;
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

    private final PlayerMover mover;
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
    private AudioClip thrustSound;
    private final Affine transform;
    private final Alert gameMessage;

    /**
     * Default constructor
     */
    public GameController() {
        // create mover with handlers
        mover = new PlayerMover(this::onMoveFinished, this::onMoving);
        // matrix for drawing on canvas
        transform = new Affine();
        // post move steps
        postMove = 0;
        // game alerts
        gameMessage = new Alert(Alert.AlertType.INFORMATION);
    }

    /**
     * Movement handler. Called for every movement frame.
     * @param now Current time in nanoseconds
     * @param x Current x position
     * @param y Current Y position
     * @param angle Current tilt angle
     */
    private void onMoving(long now, double x, double y, double angle) {
        // draw board
        drawBoard();
        // get rocket image
        var img = ((now / 100000000) & 1) == 0 ? thrustImage1 : thrustImage2;
        // calculate transform: translate -> rotate
        transform.setToIdentity();
        transform.appendTranslation(x - img.getWidth() / 2,
                y - img.getHeight() * ADJUST_PLAYER_HEIGHT_MULTIPLIER);
        transform.appendRotation(angle);
        // set transform and draw
        gfx.setTransform(transform);
        gfx.drawImage(img, 0, 0);
    }

    /**
     * Draw "passive" player at position.
     *
     * @param x x position
     * @param y y position
     */
    private void drawPlayerAt(double x, double y) {
        // draw board
        drawBoard();
        // calculate transform: translate -> rotate
        transform.setToIdentity();
        transform.appendTranslation(x - passiveImage.getWidth() / 2,
                y - passiveImage.getHeight() * ADJUST_PLAYER_HEIGHT_MULTIPLIER);
        // set transform and draw
        gfx.setTransform(transform);
        gfx.drawImage(passiveImage, 0, 0);
    }

    /**
     * FXML: Handle start button click.
     * Start or restart the game.
     */
    @FXML
    private void onStartButtonClick() {
        startButton.setText("Restart Game");
        setButtonStates(BUTTON_STATE_WORKING);
        gameBoard.initialize();
        music.play();
        movePlayer("normal", gameBoard.getLocation(0));
    }

    /**
     * FXML: Quit the app.
     */
    @FXML
    private void onQuitButtonClick() {
        System.exit(0);
    }

    /**
     * FXML: Roll die by starting the dia animation.
     */
    @FXML
    private void onRollButtonClick() {
        setButtonStates(BUTTON_STATE_WORKING);
        diePlayer.play();
    }

    /**
     * Initialize the game controller.
     *
     * @param url
     * @param resourceBundle
     */
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
            doDieRoll(); // end of play event
        });
        // background image
        boardBackground = new Image(GameApplication.class.getResourceAsStream("board.png"));
        // player images
        passiveImage = new Image(GameApplication.class.getResourceAsStream("rocket_hull.png"));
        thrustImage1 = new Image(GameApplication.class.getResourceAsStream("rocket_hull_a.png"));
        thrustImage2 = new Image(GameApplication.class.getResourceAsStream("rocket_hull_b.png"));
        // sounds
        thrustSound = GameSounds.getSingleton().getSound("thruster");
        music = GameSounds.getSingleton().getMusic();
        music.setVolume(0.1);
        // Get graphics context for drawing
        gfx = gameCanvas.getGraphicsContext2D();
        // Draw board
        drawBoard();
    }

    /**
     * Draw the board (canvas background)
     */
    private void drawBoard() {
        // Draw background with identity
        transform.setToIdentity();
        gfx.setTransform(transform);
        gfx.drawImage(boardBackground, 0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
    }

    /**
     * Initiate player movement.
     *
     * @param moveType Type string
     * @param target Target location
     */
    private void movePlayer(String moveType, Location target) {
        // get current location
        Location source = gameBoard.getLocation();
        if (source != target) {
            // seth movement path
            var src = source.getNormal();
            var tgt = target.getNormal();
            // start moving
            mover.move(
                    moveType,
                    src.distance(tgt) * MAX_MOVE_TIME,
                    src.getX() * gameCanvas.getWidth(), // normals to actual canvas coordinates
                    src.getY() * gameCanvas.getHeight(),
                    tgt.getX() * gameCanvas.getWidth(),
                    tgt.getY() * gameCanvas.getHeight()
            );
            // start playing thruster
            thrustSound.play();
        }
    }

    /**
     * Finished moving handler.
     * Calls game logic.
     * @param moveType
     * @param x
     * @param y
     */
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