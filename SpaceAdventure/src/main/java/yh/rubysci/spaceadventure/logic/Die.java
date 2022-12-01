package yh.rubysci.spaceadventure.logic;

public class Die {


    /**
     * Roll a six sided die.
     *
     * @return
     */
    public static int roll() {
        return (int) (Math.random() * 6 + 1);

    }
}
