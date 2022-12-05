package yh.rubysci.spaceadventure;

import javafx.animation.AnimationTimer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class AnimationManager {
    private static AnimationManager singleton = null;
    private Runnable preAnimationFrame = null;
    private Runnable postAnimationFrame = null;
    private final List<Function<Long, Boolean>> activeList;
    private final AnimationTimer timer;

    private AnimationManager() {
        // handler list
        activeList = new ArrayList<>();
        // timer instance
        timer = new AnimationTimer() {
            /**
             * 1. Do pre-animation frame.
             * 2. Do all animations frames.
             * 3. Stop if no activity left.
             * @param now
             */
            @Override
            public void handle(long now) {
                if (preAnimationFrame != null) {
                    preAnimationFrame.run();
                }
                synchronized (AnimationManager.class) {
                    // start from tail in order to safely remove items
                    for (var i = activeList.size() - 1; i >= 0; i--) {
                        if (!activeList.get(i).apply(now)) {
                            activeList.remove(i);
                        }
                    }
                }
                if (postAnimationFrame != null) {
                    postAnimationFrame.run();
                }
                synchronized (AnimationManager.class) {
                    if(activeList.isEmpty()) {
                        timer.stop();
                    }
                }
            }
        };
    }

    public static AnimationManager getSingleton() {
        if (singleton == null) {
            singleton = new AnimationManager();
        }
        return singleton;
    }

    public void addAnimation(Function<Long, Boolean> handler) {
        synchronized (AnimationManager.class) {
            activeList.add(handler);
        }
        timer.start();
    }

    public void setPreAnimationFrame(Runnable preAnimationFrame) {
        this.preAnimationFrame = preAnimationFrame;
    }

    public void setPostAnimationFrame(Runnable postAnimationFrame) {
        this.postAnimationFrame = postAnimationFrame;
    }
}
