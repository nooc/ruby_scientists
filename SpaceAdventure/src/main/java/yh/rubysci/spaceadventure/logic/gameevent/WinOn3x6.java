package yh.rubysci.spaceadventure.logic.gameevent;

public class WinOn3x6 extends GameFinished {
    @Override
    public String getEventMessage(int roll) {

        return "Yoy rolled a 6 three times in a row!\n" + super.getEventMessage(roll);
    }
}
