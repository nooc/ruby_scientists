package yh.rubysci.spaceadventure;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
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
    private static final double MAX_MUSIC_VOLUME = 0.3;
    private static final double MAX_MOVE_TIME = 5;
    private static final String MOVE_NORMAL = "normal";
    private static final String MOVE_POST = "post";
    private static final double ADJUST_PLAYER_HEIGHT_MULTIPLIER = 0.8;
    private static final double TEXT_START_X = 0;
    private static final double TEXT_START_Y = 0;
    private static final double TEXT_END_X = 100;
    private static final double TEXT_END_Y = 100;
    private final PlayerMover mover;
    private final Affine transform;
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
    @FXML
    private Slider volumeSlider;
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
    private int rollCount;
    private double soundFxVolume;
    private AudioClip thrustSound;
    private GameMessage gameMessage;

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
        // fx volume
        soundFxVolume = 1;
        rollCount = 0;
    }

    /**
     * Movement handler. Called for every movement frame.
     *
     * @param now   Current time in nanoseconds
     * @param x     Current x position
     * @param y     Current Y position
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
        rollCount = 0;
        postMove = 0;
        lastRolled = 0;
        startButton.setText("Restart Game");
        setButtonStates(GameButtonState.Working);
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
        setButtonStates(GameButtonState.Working);
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
        // slider
        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> value, Number oldValue, Number newValue) {
                setMasterVolume(newValue.doubleValue() / 100);
            }
        });

        // buttons
        setButtonStates(GameButtonState.Initial);

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
        music.setVolume(MAX_MUSIC_VOLUME);

        // Get graphics context for drawing
        gfx = gameCanvas.getGraphicsContext2D();

        // game alerts
        gameMessage = new GameMessage(gameCanvas,
                new Point2D(TEXT_START_X, TEXT_START_Y),
                new Point2D(TEXT_END_X, TEXT_END_Y));

        // Draw board
        drawBoard();
    }

    /**
     * Set game volume.
     *
     * @param v
     */
    private void setMasterVolume(double v) {
        soundFxVolume = v;
        music.setVolume(v * MAX_MUSIC_VOLUME);
        diePlayer.setVolume(soundFxVolume);
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
     * @param target   Target location
     */
    private void movePlayer(String moveType, Location target) {
        // get current location
        Location source = gameBoard.getLocation();
        // get target location
        var targetPointN = target.getNormal();
        var targetX = targetPointN.getX() * gameCanvas.getWidth();
        var targetY = targetPointN.getY() * gameCanvas.getHeight();
        // move if different
        if (source != target) {
            // seth movement path
            var src = source.getNormal();
            // start moving
            mover.move(
                    moveType,
                    src.distance(targetPointN) * MAX_MOVE_TIME,
                    src.getX() * gameCanvas.getWidth(), // normals to actual canvas coordinates
                    src.getY() * gameCanvas.getHeight(),
                    targetX,
                    targetY
            );
            // start playing thruster
            thrustSound.play(soundFxVolume);
        } else {
            // target is same
            onMoveFinished(moveType, targetX, targetY);
        }
    }

    /**
     * Finished moving handler.
     * Calls game logic.
     *
     * @param moveType The event type passed
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

    /**
     * Called when roll video finishes.
     * Does actual roll.
     */
    private void doDieRoll() {
        // stop video and reset time
        diePlayer.stop();
        diePlayer.seek(Duration.ZERO);
        // roll
        lastRolled = Die.roll();
        // log roll
        rollLog.getItems().add((++rollCount) + ": Rolled a " + lastRolled);
        // initiate move
        movePlayer("normal",
                BoardLocations.getLocation(gameBoard.getIndexWithOffset(lastRolled))
        );
    }

    /**
     * This handler is called from GameBoard in GameBoard.handleRoll()
     * with a game event argument.
     *
     * @param event Game event to handle.
     */
    private void onGameEventHandler(IGameEvent event) {
        // play event sound
        var sound = GameSounds.getSingleton().getSound(event.getEventSoundId(lastRolled));
        if (sound != null) {
            sound.play(soundFxVolume);
        }
        // show event message
        if (event.hasMessage(lastRolled)) {
            gameMessage.showMessage(event, lastRolled);
        }
        // test if this is a GameFinished event
        if (event instanceof GameFinished) {
            finishGame(event);
            setButtonStates(GameButtonState.Initial);
        } else {
            // handle anything else here
            postMove = event.getMovementOffset(lastRolled);
            if (postMove != 0) {
                movePlayer(MOVE_POST,
                        BoardLocations.getLocation(gameBoard.getIndexWithOffset(postMove))
                );
            }
            setButtonStates(GameButtonState.Idle);
        }
    }

    /**
     * Finish game.
     *
     * @param event Game event
     */
    private void finishGame(IGameEvent event) {
        setButtonStates(GameButtonState.Initial);
        music.stop();
        rollLog.getItems().add("Game won!");
    }

    /**
     * Set button states
     *
     * @param state Game button state
     */
    private void setButtonStates(GameButtonState state) {
        if (state.equals(GameButtonState.Initial)) {
            rollButton.setDisable(true);
            startButton.setDisable(false);
        } else if (state.equals(GameButtonState.Working)) {
            rollButton.setDisable(true);
            startButton.setDisable(true);
        } else if (state.equals(GameButtonState.Idle)) {
            rollButton.setDisable(false);
            startButton.setDisable(false);
        }
    }
}