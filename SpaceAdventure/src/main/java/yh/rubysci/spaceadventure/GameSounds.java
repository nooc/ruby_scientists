package yh.rubysci.spaceadventure;

import java.util.Map;

public class GameSounds {
    private static GameSounds singleton = null;

    private final Map<String,Media>

    private GameSounds() {

    }

    public static GameSounds getSingleton() {
        if(singleton==null) {
            singleton = new GameSounds();
        }
        return singleton;
    }
}
