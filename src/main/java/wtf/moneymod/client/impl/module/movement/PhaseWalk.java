package wtf.moneymod.client.impl.module.movement;

import club.cafedevelopment.reflectionsettings.annotation.Clamp;
import club.cafedevelopment.reflectionsettings.annotation.Setting;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import wtf.moneymod.client.api.events.MoveEvent;
import wtf.moneymod.client.api.events.PacketEvent;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.Timer;
import wtf.moneymod.client.impl.utility.impl.world.EntityUtil;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

@Module.Register( label = "PhaseWalk", cat = Module.Category.MOVEMENT)
public class PhaseWalk extends Module {

    @Setting(id = "Attempts", clamp = @Clamp(min = 1, max = 10)) public int attempts = 10;
    @Setting(id = "Speed", clamp = @Clamp(min = 1, max = 10)) public int speed = 3;

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

    @Override public void onTick() {
        if (nullCheck()) return;
        PhaseWalk.mc.player.motionX = 0.0;
        PhaseWalk.mc.player.motionY = 0.0;
        PhaseWalk.mc.player.motionZ = 0.0;
        if (mc.player.collidedHorizontally) {
            if (timer.isPassed()) {
                double[] dArray = EntityUtil.forward(speed / 100.0);
                for (int i = 0; i < attempts; ++i) {
                    sendPackets(PhaseWalk.mc.player.posX + dArray[0], PhaseWalk.mc.player.posY + (PhaseWalk.mc.gameSettings.keyBindJump.isKeyDown() ? 1 : (PhaseWalk.mc.gameSettings.keyBindSneak.isKeyDown() ? -1 : 0)) * speed / 100.0, PhaseWalk.mc.player.posZ + dArray[1]);
                }
                timer.reset();
            }
        } else {
            cancel = false;
        }
    }

    public void sendPackets(double d, double d2, double d3) {
        cancel = false;
        mc.getConnection().sendPacket(new CPacketPlayer.Position(d, d2, d3, PhaseWalk.mc.player.onGround));
        mc.getConnection().sendPacket(new CPacketPlayer.Position(0.0, 1337.0, 0.0, PhaseWalk.mc.player.onGround));
        cancel = true;
    }

    @Handler
    public Listener<MoveEvent> onMove = new Listener<>(MoveEvent.class, e -> {
        if (nullCheck()) return;
        if (mc.player.collidedHorizontally) {
            e.motionX = 0.0;
            e.motionY = 0.0;
            e.motionZ = 0.0;
        }
    });
}
