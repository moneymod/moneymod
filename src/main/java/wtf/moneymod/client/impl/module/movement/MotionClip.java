package wtf.moneymod.client.impl.module.movement;

import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.BlockPos;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.events.PacketEvent;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.world.EntityUtil;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

import java.util.Random;

@Module.Register( label = "MotionClip", desc = "Bypass Method For CC", cat = Module.Category.MOVEMENT )
public class MotionClip extends Module {


    @Value(value = "Movement") public Movement movement = Movement.SNEAK;
    @Value(value = "Method") public Method method = Method.PACKETS;
    @Value(value = "Fall") public FallMethod fallMethod = FallMethod.PLAYERSEND;
    @Value(value = "Speed") @Bounds(min = 0, max = 8) public float speed = 1;
    @Value(value = "Updater") @Bounds(min = 0, max = 1) public float updater = 0.2f;
    @Value(value = "NoClip") public boolean noClip = true;
    @Value(value = "Set Pos") public boolean setPos = false;
    @Value(value = "Cancel Motion") public boolean cancelMotion = false;
    @Value(value = "Teleport Id") public boolean teleportingId = true;
    @Value(value = "Extra Teleport") public boolean extraTelepor = true;
    @Value(value = "Fall Teleport") public boolean fallTeleport = true;
    @Value(value = "Walk Bypass") public boolean walkBypass = true;
    @Value(value = "Timer") public boolean timer = false;
    @Value(value = "Ticks") @Bounds(min = 0, max = 16) public float timertick = 8;
    public enum Method {
        MOTION, PACKETS
    }

    public enum Movement {
        SPRINT, SNEAK, NONE
    }

    public enum FallMethod {
        PLAYERSEND, CONNECTIONSEND
    }


    int t;
    int teleportId;

    @Override
    public void onToggle() {
        Main.TICK_TIMER = 1;    
        mc.player.noClip = false;
        teleportId = 0;
        t = 0;
    }
    private Random random = new Random();

    @Override
    public void onTick() {
        if (noClip) mc.player.noClip = true;

        if (movement == Movement.SNEAK && !mc.gameSettings.keyBindSneak.isKeyDown()) return;
        if (movement == Movement.SPRINT && !mc.gameSettings.keyBindSprint.isKeyDown()) return;

        if (cancelMotion) {
            mc.player.motionX = 0;
            mc.player.motionZ = 0;
        }

        if (method == Method.PACKETS) {
            double[] forward = EntityUtil.forward(0.0001);
            double[] posSpeed = EntityUtil.forward(0.0000000001);

            if (mc.player.collidedHorizontally) {
                mc.player.connection.sendPacket((Packet) new CPacketPlayer.Position(mc.player.posX + forward[0], mc.player.posY, mc.player.posZ + forward[1], mc.player.onGround));
                if (t++ >= 1) {
                    if (fallMethod == FallMethod.PLAYERSEND)
                        mc.player.connection.sendPacket((Packet) new CPacketPlayer.Position(mc.player.posX, 0, mc.player.posZ, mc.player.onGround));
                    else mc.getConnection().sendPacket((Packet) new CPacketPlayer.Position(mc.player.posX, 0, mc.player.posZ, mc.player.onGround));
                    t = 0;
                }
                mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)mc.player, CPacketEntityAction.Action.STOP_RIDING_JUMP));
            } else {
                if (timer) Main.TICK_TIMER = 1;
                if (!EntityUtil.INSTANCE.isMoving(mc.player) && walkBypass) {
                    for (int i = 0; i < 1; i++) {
                        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.onGround));
                        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY - 1, mc.player.posZ, mc.player.onGround));

                    }
                }
            }
            if (teleportingId) {
                teleportId++;
                if (extraTelepor) mc.player.connection.sendPacket(new CPacketConfirmTeleport(teleportId - 1));
                mc.player.connection.sendPacket(new CPacketConfirmTeleport(teleportId));
                if (extraTelepor) mc.player.connection.sendPacket(new CPacketConfirmTeleport(teleportId + 1));
            }
        }

        if (method == Method.MOTION) {
            double[] sp = EntityUtil.forward(0.0008);
            if (mc.player.collidedHorizontally) {
                mc.player.setLocationAndAngles(mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.rotationYaw, mc.player.rotationPitch);
                mc.player.setLocationAndAngles(mc.player.posX + sp[0], mc.player.posY, mc.player.posZ + sp[1], mc.player.rotationYaw, mc.player.rotationPitch);
            } else {
                if (!EntityUtil.INSTANCE.isMoving(mc.player) && walkBypass){
                    for (int i = 0; i < 1; i++) {
                        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.onGround));
                        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY - 1, mc.player.posZ, mc.player.onGround));
                    }
                }
            }
        }
    }


    @Handler
    public Listener<PacketEvent.Receive> packetEventReceive = new Listener<>(PacketEvent.Receive.class, e -> {
        if (e.getPacket() instanceof SPacketPlayerPosLook) {
            if (!(mc.currentScreen instanceof GuiDownloadTerrain)) {
                SPacketPlayerPosLook packet = (SPacketPlayerPosLook) e.getPacket();
                if (mc.player.isEntityAlive()) {
                    if (this.teleportId <= 0) {
                        this.teleportId = ((SPacketPlayerPosLook) e.getPacket()).getTeleportId();
                    }
                }
                teleportId = packet.getTeleportId();
            } else {
                teleportId = 0;
            }
        }
    });
}
