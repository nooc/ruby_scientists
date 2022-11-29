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
        mediaMap.put("music", load("659747__seth-makes-sounds__free-background-music.mp4"));
        mediaMap.put("thruster", load("thrusters-loop.mp4"));
        background = new MediaPlayer(new Media(
                GameApplication.class.getResource("659747__seth-makes-sounds__free-background-music.mp4")
                        .toString()
        ));
    }

    public static GameSounds getSingleton() {
        if(singleton==null) {
            singleton = new GameSounds();
        }
        return singleton;
    }

    private static AudioClip load(String file) {
        return new AudioClip(GameApplication.class.getResource(file).toString());
    }
}
