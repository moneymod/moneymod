package wtf.moneymod.client.impl.module.player;

import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import wtf.moneymod.client.api.events.PacketEvent;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.module.misc.ChatTweaks;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

@Module.Register(label = "Velocity", cat = Module.Category.PLAYER)
public class Velocity extends Module {

    private static Velocity INSTANCE;

    public Velocity() {
        INSTANCE = this;
    }

    public static Velocity getInstance() {
        return INSTANCE;
    }


    @Value(value = "No Push") public boolean noPush = true;

    @Handler
    public Listener<PacketEvent.Receive> packetEventReceive = new Listener<>(PacketEvent.Receive.class, e -> {

        if (e.getPacket() instanceof SPacketEntityVelocity && ((SPacketEntityVelocity) e.getPacket()).getEntityID() == this.mc.player.getEntityId() || e.getPacket() instanceof SPacketExplosion || e.getPacket() instanceof EntityFishHook) {
            e.setCancelled(true);
        }
    });

}
