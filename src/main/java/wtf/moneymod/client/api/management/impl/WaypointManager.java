package wtf.moneymod.client.api.management.impl;

import wtf.moneymod.client.api.management.IManager;
import wtf.moneymod.client.impl.waypoint.Waypoint;

import java.util.ArrayList;

public class WaypointManager extends ArrayList<Waypoint> {

    private static final WaypointManager INSTANCE = new WaypointManager();

    @Override public boolean add(Waypoint waypoint) {
        Waypoint toRemove = null;
        for (Waypoint w : this) {
            if (w.getName().equalsIgnoreCase(waypoint.getName()) && w.getServer().equalsIgnoreCase(waypoint.getServer())) toRemove = w;
        }
        if (toRemove != null) remove(toRemove);
        return super.add(waypoint);
    }

    public static WaypointManager getInstance() {
        return INSTANCE;
    }

}
