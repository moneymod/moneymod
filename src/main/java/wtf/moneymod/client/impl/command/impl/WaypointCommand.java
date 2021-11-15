package wtf.moneymod.client.impl.command.impl;

import net.minecraft.util.math.BlockPos;
import wtf.moneymod.client.api.management.impl.WaypointManager;
import wtf.moneymod.client.impl.command.Command;
import wtf.moneymod.client.impl.utility.impl.world.ChatUtil;
import wtf.moneymod.client.impl.waypoint.Waypoint;

import java.util.Arrays;
import java.util.stream.Collectors;

public class WaypointCommand extends Command {

    public WaypointCommand() {
        super("waypoint <add|del|list> <name> <x> <y> <z> <server> <dimension>", "waypoint", "waypoints", "w");
    }

    @Override public void execute(String[] args) {
        if (args.length < 2) {
            sendUsage();
            return;
        }
        switch (args[ 1 ]) {
            case "add":
                if (args.length < 3) return;
                add(Arrays.copyOfRange(args, 2, args.length));
                break;
            case "del":
                if (args.length < 3) return;
                WaypointManager.getInstance().forEach(w -> {
                    if (w.getName().equalsIgnoreCase(args[ 1 ])) {
                        WaypointManager.getInstance().remove(w);
                        print("removed waypoint " + args[ 1 ]);
                    }
                });
                break;
            case "list":
                String waypoints = WaypointManager.getInstance().stream().map(w -> String.format("%s(%s, %s, %s)", w.getName(), w.getPos().getX(), w.getPos().getY(), w.getPos().getZ())).collect(Collectors.joining(", "));
                print(String.format("Waypoints(%s): %s", WaypointManager.getInstance().size(), waypoints));
                break;
        }
    }

    private void add(String[] args) {
        System.out.println(Arrays.toString(args));
        String server = "singleplayer";
        if (mc.getCurrentServerData() != null) server = mc.getCurrentServerData().serverIP ;
        Waypoint waypoint = null;
        try {
            switch (args.length) {
                case 1:
                    waypoint = new Waypoint(args[ 0 ], mc.player.getPosition(), server, mc.world.provider.getDimension());
                    break;
                case 3:
                    waypoint = new Waypoint(args[ 0 ], new BlockPos(Integer.parseInt(args[ 1 ]), mc.player.posY, Integer.parseInt(args[ 2 ])), server, mc.world.provider.getDimension());
                    break;
                case 4:
                    waypoint = new Waypoint(args[ 0 ], new BlockPos(Integer.parseInt(args[ 1 ]), Integer.parseInt(args[ 2 ]), Integer.parseInt(args[ 3 ])), server, mc.world.provider.getDimension());
                    break;
                case 5:
                    waypoint = new Waypoint(args[ 0 ], new BlockPos(Integer.parseInt(args[ 1 ]), Integer.parseInt(args[ 2 ]), Integer.parseInt(args[ 3 ])), args[ 4 ], mc.world.provider.getDimension());
                    break;
                case 6:
                    waypoint = new Waypoint(args[ 0 ], new BlockPos(Integer.parseInt(args[ 1 ]), Integer.parseInt(args[ 2 ]), Integer.parseInt(args[ 3 ])), args[ 4 ], Integer.parseInt(args[ 5 ]));
                    break;
            }
        } catch (Exception e) {
            sendUsage();
        }

        if (waypoint != null) {
            WaypointManager.getInstance().add(waypoint);
            print("added waypoint " + args[ 0 ]);
        }
    }

}
