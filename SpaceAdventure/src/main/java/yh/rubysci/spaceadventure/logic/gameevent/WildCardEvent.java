package yh.rubysci.spaceadventure.logic.gameevent;

/**
 * Move 1-3 steps in random direction.
 */
public class WildCardEvent extends NeutralEventBase {

    @Override
    public int getMovementOffset() {
        return (int) (Math.signum(Math.random()-0.5) * (Math.random() * 3 + 1));
    }

    @Override
    public String getEventMessage() {
        return null;
    }
}
