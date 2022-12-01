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
        mediaMap.put("positive",load("457518__graham-makes__chord-alert-notification.wav"));
        mediaMap.put("negative",load("569903__bigdino1995__scificannon.wav"));
        mediaMap.put("neutral",load("657945__matrixxx__scifi-inspect-sound-ui-or-in-game-notification-01.wav"));
        mediaMap.put("finished",load("341984__unadamlar__winning.wav"));
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

    public AudioClip getSound(String soundId) {
        if(soundId != null) {
            return mediaMap.get(soundId);
        }
        return null;
    }

    public MediaPlayer getMusic() {
        return background;
    }
}
