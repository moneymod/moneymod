package wtf.moneymod.client.impl.module.misc;

import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketVehicleMove;
import org.lwjgl.input.Keyboard;
import wtf.moneymod.client.api.events.PacketEvent;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.world.EntityUtil;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

@Module.Register( label = "PacketCanceller", cat = Module.Category.MISC)
public class PacketCanceller extends Module {

    @Value(value = "PacketPlayer") public boolean packetPlayer = false;
    @Value(value = "PacketPlayerPos") public boolean packetPlayerPos = false;
    @Value(value = "PacketPlayerPosRot") public boolean packetPlayerPosRot = false;
    @Value(value = "PacketPlayerRot") public boolean packetPlayerRot = false;
    @Value(value = "PacketVehicleMove") public boolean packetVehicleMove = false;
    @Value(value = "PacketTryUseItemBlock") public boolean packetTryuseitemOnBlock = false;
    @Value(value = "PacketTryUseItem") public boolean packetTryUseItem = false;

    @Handler
    public Listener<PacketEvent.Send> packetEventSend = new Listener<>(PacketEvent.Send.class, e -> {

        if (packetPlayer && e.getPacket() instanceof CPacketPlayer) e.setCancelled(true);
        if (packetPlayerPos && e.getPacket() instanceof CPacketPlayer.Position) e.setCancelled(true);
        if (packetPlayerPosRot && e.getPacket() instanceof CPacketPlayer.PositionRotation) e.setCancelled(true);
        if (packetPlayerRot && e.getPacket() instanceof CPacketPlayer.Rotation) e.setCancelled(true);
        if (packetVehicleMove && e.getPacket() instanceof CPacketVehicleMove) e.setCancelled(true);
        if (packetTryuseitemOnBlock && e.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) e.setCancelled(true);
        if (packetTryUseItem && e.getPacket() instanceof CPacketPlayerTryUseItem) e.setCancelled(true);
    });
}
