package yh.rubysci.spaceadventure.logic;

public interface IGameEvent {

    /**
     * Get string id for sound event.
     *
     * @return string id
     */
    String getEventSoundId(int roll);

    /**
     * Get resulting movement.
     *
     * @return steps
     */
    int getMovementOffset(int roll);

    /**
     * Get event message.
     *
     * @return
     */
    String getEventMessage(int roll);

    /**
     * Get event title.
     *
     * @return event title
     */
    String getEventTitle(int roll);

    /**
     * Get if event has message.
     *
     * @return boolean
     */
    boolean hasMessage(int roll);
}
