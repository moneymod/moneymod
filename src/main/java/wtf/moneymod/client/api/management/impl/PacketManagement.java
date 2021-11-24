package wtf.moneymod.client.api.management.impl;

import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.impl.module.global.Global;
import wtf.moneymod.client.impl.utility.Globals;

import java.util.ArrayDeque;

public class PacketManagement extends ArrayDeque<Packet> implements Globals {

    private static final PacketManagement INSTANCE = new PacketManagement();

    @SubscribeEvent public void onTick(TickEvent.ClientTickEvent event) {
        if (mc.world == null || mc.player == null) return;
        for (int j = 0; j < 6; j++) {
            if (peekFirst() != null) mc.player.connection.sendPacket(pollFirst());
        }
    }

    public static void send(Packet packet) {
        getInstance().add(packet);
    }

    public static PacketManagement getInstance() { return INSTANCE; }

}
