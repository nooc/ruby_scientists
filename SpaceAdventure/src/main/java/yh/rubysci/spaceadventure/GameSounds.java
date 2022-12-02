package yh.rubysci.spaceadventure;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.HashMap;
import java.util.Map;

public class GameSounds {
    private static GameSounds singleton = null;
    private final Map<String, AudioClip> mediaMap;
    private final MediaPlayer background;

    private GameSounds() {
        mediaMap = new HashMap<>();
        mediaMap.put("positive", load("positive.wav"));
        mediaMap.put("negative", load("negative.wav"));
        mediaMap.put("neutral", load("neutral.wav"));
        mediaMap.put("finished", load("finished.wav"));
        mediaMap.put("thruster", load("thrusters-loop.mp4"));
        background = new MediaPlayer(new Media(
                GameApplication.class.getResource("background-music.mp4")
                        .toString()
        ));
        background.setCycleCount(Integer.MAX_VALUE);
    }

    public static GameSounds getSingleton() {
        if (singleton == null) {
            singleton = new GameSounds();
        }
        return singleton;
    }

    private static AudioClip load(String file) {
        return new AudioClip(GameApplication.class.getResource(file).toString());
    }

    public AudioClip getSound(String soundId) {
        if (soundId != null) {
            return mediaMap.get(soundId);
        }
        return null;
    }

    public MediaPlayer getMusic() {
        return background;
    }
}
