package wtf.moneymod.client.api.management.impl;

import wtf.moneymod.client.impl.waypoint.Waypoint;

import java.util.ArrayList;

public class WaypointManagement extends ArrayList<Waypoint> {

    private static final WaypointManagement INSTANCE = new WaypointManagement();

    @Override public boolean add(Waypoint waypoint) {
        Waypoint toRemove = null;
        for (Waypoint w : this) {
            if (w.getName().equalsIgnoreCase(waypoint.getName()) && w.getServer().equalsIgnoreCase(waypoint.getServer())) toRemove = w;
        }
        if (toRemove != null) remove(toRemove);
        return super.add(waypoint);
    }

    public static WaypointManagement getInstance() {
        return INSTANCE;
    }

}
