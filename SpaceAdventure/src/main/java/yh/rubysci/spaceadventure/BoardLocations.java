package yh.rubysci.spaceadventure;

import yh.rubysci.spaceadventure.logic.Location;
import yh.rubysci.spaceadventure.logic.gameevent.*;

public final class BoardLocations {

    private static final NullEvent NULL_EVT = new NullEvent();
    public static final Location OFF_SCREEN_LOCATION = new Location(-0.1, 0.0, NULL_EVT);
    private static final Location[] LOCATIONS = new Location[]{
            new Location(0.246, 0.297, NULL_EVT),
            new Location(0.327, 0.297, NULL_EVT),
            new Location(0.391, 0.25, NULL_EVT),
            new Location(0.442, 0.188, NULL_EVT),
            new Location(0.510, 0.168, NULL_EVT),
            new Location(0.572, 0.201, NULL_EVT),
            new Location(0.6, 0.276, NULL_EVT),
            new Location(0.576, 0.355, NULL_EVT),
            new Location(0.515, 0.4, new WildCardEvent()), // 9
            new Location(0.436, 0.395, NULL_EVT),
            new Location(0.354, 0.379, NULL_EVT),
            new Location(0.292, 0.376, NULL_EVT),
            new Location(0.222, 0.408, NULL_EVT),
            new Location(0.18, 0.492, NULL_EVT),
            new Location(0.203, 0.576, new MagneticField()), // 15
            new Location(0.272, 0.628, NULL_EVT),
            new Location(0.349, 0.634, NULL_EVT),
            new Location(0.426, 0.595, new WildCardEvent()), // 18
            new Location(0.496, 0.533, NULL_EVT),
            new Location(0.558, 0.459, new MagneticField() ), // 20
            new Location(0.622, 0.412, NULL_EVT),
            new Location(0.693, 0.386, NULL_EVT),
            new Location(0.765, 0.413, NULL_EVT),
            new Location(0.799, 0.503, NULL_EVT),
            new Location(0.764, 0.587, new MissingHome()), // 25
            new Location(0.695, 0.625, NULL_EVT),
            new Location(0.616, 0.621, NULL_EVT),
            new Location(0.542, 0.621, NULL_EVT),
            new Location(0.464, 0.649, NULL_EVT),
            new Location(0.423, 0.713, new GoBack(29)), // 30
            new Location(0.436, 0.789, NULL_EVT),
            new Location(0.498, 0.838, NULL_EVT),
            new Location(0.568, 0.826, NULL_EVT),
            new Location(0.62, 0.764, NULL_EVT),
            new Location(0.67, 0.711, new WildCardEvent()), // 35
            new Location(0.745, 0.705, NULL_EVT),
            new Location(0.795, 0.763, new GameFinished()), // 37
    };

    public static Location getLocation(int index) {

        return index == (-1) ? OFF_SCREEN_LOCATION : LOCATIONS[index];
    }

    public static int getMaxIndex() {
        return LOCATIONS.length - 1;
    }
}
