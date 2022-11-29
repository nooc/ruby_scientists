package yh.rubysci.spaceadventure.logic;

public interface IGameEvent {

    /**
     * Get string id for sound event.
     * @return string id
     */
    String getEventSoundId();

    /**
     * Get resulting movement.
     * @return steps
     */
    int getMovementOffset();

    /**
     * Get event message.
     * @return
     */
    String getEventMessage();
}
