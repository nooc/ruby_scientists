package yh.rubysci.spaceadventure.logic.gameevent;

/**
 * Move 1-3 steps in random direction.
 */
public class WildCardEvent extends NeutralEventBase {

    @Override
    public int getMovementOffset(int roll) {
        return (int) (Math.signum(Math.random() - 0.5) * (Math.random() * 3 + 1));
    }

    @Override
    public String getEventMessage(int roll) {
        return "You entered a disorientating space anomaly.\nYou are not sure what happened!";
    }

    @Override
    public String getEventTitle(int roll) {
        return "Space Anomaly";
    }

    @Override
    public boolean hasMessage(int roll) {
        return true;
    }
}
