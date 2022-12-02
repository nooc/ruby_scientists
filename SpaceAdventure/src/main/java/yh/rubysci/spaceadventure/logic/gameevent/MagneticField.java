package yh.rubysci.spaceadventure.logic.gameevent;

public class MagneticField extends NeutralEventBase {
    @Override
    public int getMovementOffset(int roll) {
        return isNegative(roll) ? -1 : 2;
    }

    @Override
    public String getEventMessage(int roll) {
        return isNegative(roll) ?
                "You have entered a magnetic field and are thrown back a step." :
                "A magnetic field has given you a push. Advance by 2.";
    }

    @Override
    public String getEventSoundId(int roll) {
        return isNegative(roll) ? "negative" : "positive";
    }

    @Override
    public String getEventTitle(int roll) {
        return "Magnetic Field";
    }

    @Override
    public boolean hasMessage(int roll) {
        return true;
    }

    private boolean isNegative(int roll) {
        return roll % 2 == 1;
    }
}
