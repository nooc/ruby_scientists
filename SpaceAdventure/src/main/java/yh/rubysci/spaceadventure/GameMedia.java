package yh.rubysci.spaceadventure;

import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;

import java.util.HashMap;
import java.util.Map;

public class GameMedia {
    private static GameMedia singleton = null;
    private final Map<String, AudioClip> audioClipMap;
    private final Map<String, Media> mediaMap;
    private final Map<String, Image> imageMap;

    private GameMedia() {
        audioClipMap = new HashMap<>();
        audioClipMap.put("positive", loadAudioClip("positive.wav"));
        audioClipMap.put("negative", loadAudioClip("negative.wav"));
        audioClipMap.put("neutral", loadAudioClip("neutral.wav"));
        audioClipMap.put("finished", loadAudioClip("finished.wav"));
        audioClipMap.put("thruster", loadAudioClip("thrusters-loop.mp4"));

        mediaMap = new HashMap<>();
        mediaMap.put("music", loadMedia("music.mp4"));
        mediaMap.put("roll", loadMedia("die_roll.mp4"));

        imageMap = new HashMap<>();
        imageMap.put("board", loadImage("board.png"));
        imageMap.put("hull", loadImage("rocket_hull.png"));
        imageMap.put("hull flame1", loadImage("rocket_hull_a.png"));
        imageMap.put("hull flame2", loadImage("rocket_hull_b.png"));
    }

    public static GameMedia getSingleton() {
        if (singleton == null) {
            singleton = new GameMedia();
        }
        return singleton;
    }

    private AudioClip loadAudioClip(String fileName) {
        return new AudioClip(GameApplication.class.getResource(fileName).toString());
    }

    private Media loadMedia(String fileName) {
        return new Media(GameApplication.class.getResource(fileName).toString());
    }

    private Image loadImage(String fileName) {
        return new Image(GameApplication.class.getResourceAsStream(fileName));
    }

    public AudioClip getAudioClip(String id) {
        return audioClipMap.getOrDefault(id, null);
    }

    public Media getMedia(String id) {
        return mediaMap.getOrDefault(id, null);
    }

    public Image getImage(String id) {
        return imageMap.getOrDefault(id, null);
    }
}
