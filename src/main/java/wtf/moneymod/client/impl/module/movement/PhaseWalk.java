package wtf.moneymod.client.impl.module.movement;

import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import wtf.moneymod.client.api.events.MoveEvent;
import wtf.moneymod.client.api.events.PacketEvent;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.misc.Timer;
import wtf.moneymod.client.impl.utility.impl.world.EntityUtil;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

@Module.Register( label = "PhaseWalk", cat = Module.Category.MOVEMENT)
public class PhaseWalk extends Module {

    @Value(value = "Attempts") @Bounds(min = 1, max = 5) public int attempts = 1;
    @Value(value = "Speed") @Bounds(min = 1, max = 5) public int speed = 1;
    @Value(value = "Skip") public boolean skiper = false;
    @Value(value = "SkipMode") public Skip skip = Skip.NPC1;

    public enum Skip{
        NPC1, NCP2, NCP3;
    }

    Timer timer = new Timer();
    boolean cancel = false;
    int teleportID = 0;

    @Handler
    public Listener<PacketEvent.Send> packeEventSend = new Listener<>(PacketEvent.Send.class, e -> {
        if (nullCheck()) return;
        if (e.getPacket() instanceof SPacketPlayerPosLook) {
            teleportID = ((SPacketPlayerPosLook) ((Object) e.getPacket())).getTeleportId();
            mc.getConnection().sendPacket(new CPacketConfirmTeleport(teleportID + 1));
        }
    });


    @Handler
    public Listener<MoveEvent> onMove = new Listener<>(MoveEvent.class, e -> {
        if (nullCheck()) return;

//        mc.player.motionX = 0.0;
//        mc.player.motionY = 0.0;
//        mc.player.motionZ = 0.0;
        double[] dArray = EntityUtil.forward(speed / 150.0);
        for (int i = 0; i < attempts; ++i) {
            sendPackets(mc.player.posX + dArray[0], mc.player.posY, mc.player.posZ + dArray[1]);
        }
        if (mc.player.collidedHorizontally) {
            e.motionX = 0.0; e.motionY = 0.0; e.motionZ = 0.0;
        }
    });

    @Override
    public void onTick() {
        if (nullCheck()) return;
        mc.player.motionX = 0.0; mc.player.motionY = 0.0; mc.player.motionZ = 0.0;
        if (mc.player.collidedHorizontally || mc.player.collidedVertically) {
            double y = mc.player.rotationYaw * 0.017453292;
            double p = 0.01;
            for (int i = 0; i < attempts; ++i) {
                if (mc.gameSettings.keyBindSprint.isKeyDown()) {
                    mc.player.movementInput.moveForward = 0.1f;
                    sendPackets(mc.player.posX - Math.sin(y) * p, mc.player.posY + (mc.gameSettings.keyBindSneak.isKeyDown() ? -1 : 0) * speed / 100.0, mc.player.posZ + Math.cos(y) * p);
                    mc.player.setPosition(mc.player.posX - Math.sin(y) * p, mc.player.posY + (mc.gameSettings.keyBindSneak.isKeyDown() ? -1 : 0) * speed / 100.0, mc.player.posZ + Math.cos(y) * p);

                }
            }
        }
    }

    public void sendPackets(double x, double y, double z) {
        mc.getConnection().sendPacket(new CPacketPlayer.Position(x, y, z, mc.player.onGround));
        mc.getConnection().sendPacket(new CPacketPlayer.Position(0.0, 666, 0.0, mc.player.onGround));
    }
}
