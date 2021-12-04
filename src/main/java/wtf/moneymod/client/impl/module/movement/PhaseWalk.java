package wtf.moneymod.client.impl.module.movement;

import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import org.lwjgl.input.Keyboard;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.events.MoveEvent;
import wtf.moneymod.client.api.events.PacketEvent;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.misc.Timer;
import wtf.moneymod.client.impl.utility.impl.world.EntityUtil;
import wtf.moneymod.eventhandler.event.Event;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

@Module.Register( label = "PhaseWalk", cat = Module.Category.MOVEMENT)
public class PhaseWalk extends Module {

    @Value(value = "Mode") public Mode mode = Mode.PACKET;
    @Value(value = "Teleport") public Teleport teleport = Teleport.FULL;
    @Value(value = "Movement") public Movement movement = Movement.SHIFT;
    @Value(value = "Speed") @Bounds(min = 0,max = 4) public float speed = 0.1f;
    @Value(value = "Factor") @Bounds(min = 0,max = 8) public int factor = 1;
    @Value(value = "Updater") @Bounds(min = 0,max = 2) public float updater = 0.1f;
    @Value(value = "Delay") @Bounds(min = 0,max = 100) public int delay = 10;

    @Value(value = "No Clip") public boolean noClip = true;
    @Value(value = "Fall Packet") public boolean fallPacket = true;
    @Value(value = "Teleport Id") public boolean teleportId = true;
    @Value(value = "Extra Teleport") public boolean extraTeleport = true;
    @Value(value = "Cancel Motion") public boolean cancelMotion = true;
    @Value(value = "WalkBypass") public boolean walkBypass = true;
    @Value(value = "Collided Timer") public boolean collidedTimer = false;
    @Value(value = "Timer Speed") @Bounds(min = 1,max = 8) public float timerSpeed = 2;

    int walkDelay = 0;
    private Timer timer = new Timer();
    private Timer downTimer = new Timer();
    private Timer packettimer = new Timer();
    int tpId = 0;

    @Override
    public void onToggle(){
        walkDelay = 0;
        timer.reset();
        downTimer.reset();
        packettimer.reset();
        tpId = 0;
        mc.player.noClip = false;
    }

    public void doWalkBypas(){
        if (walkDelay >= 1){
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX,mc.player.posY,mc.player.posZ, mc.player.onGround));
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX,mc.player.posY - updater,mc.player.posZ, mc.player.onGround));
        }
    }

    public void doPackets(double x, double y, double z){
        mc.player.connection.sendPacket(new CPacketPlayer.Position(x,y,z,mc.player.onGround));
        if (downTimer.passed(delay)) {
            switch (teleport) {
                case FULL:
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(x, -1888, z, mc.player.onGround));
                    break;

                case SEMI:
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(x, 0, z, mc.player.onGround));
                    break;
            }
            downTimer.reset();
        }
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;
        walkDelay++;
        double[] forw = EntityUtil.forward(speed / 100);
        mc.player.noClip = this.noClip;

        if (movement == Movement.SHIFT && !mc.gameSettings.keyBindSneak.isKeyDown()) return;

        if (cancelMotion) {
            mc.player.motionX = 0;
            mc.player.motionZ = 0;
        }
        //???

        if (teleportId) {
            tpId++;
            if (extraTeleport) mc.player.connection.sendPacket(new CPacketConfirmTeleport(tpId - 1));
            mc.player.connection.sendPacket(new CPacketConfirmTeleport(tpId));
            if (extraTeleport) mc.player.connection.sendPacket(new CPacketConfirmTeleport(tpId + 1));
        }

        if (mc.player.collidedHorizontally && collidedTimer) {
            Main.TICK_TIMER = timerSpeed;
        } else Main.TICK_TIMER = 1;

        if (mode == Mode.PACKET) {
            if (mc.player.collidedHorizontally) {
                for (int i = 0; i < factor; i++) {
                    doPackets(mc.player.posX + forw[0], mc.player.posY, mc.player.posZ + forw[1]);
                }
                if (fallPacket) mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)mc.player, CPacketEntityAction.Action.STOP_RIDING_JUMP));
            } else {
                if (!EntityUtil.INSTANCE.isMoving(mc.player) && walkBypass) {
                    for (int i = 0; i < 1; i++) {
                        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.onGround));
                        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY - 1, mc.player.posZ, mc.player.onGround));

                    }
                }
            }
        }

        if (mode == Mode.MOTION){
            if (mc.player.collidedHorizontally){
                mc.player.setLocationAndAngles(mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.rotationYaw, mc.player.rotationPitch);
                for (int i = 0; i < factor; i++) {
                    mc.player.setLocationAndAngles(mc.player.posX + forw[0], mc.player.posY, mc.player.posZ + forw[1], mc.player.rotationYaw, mc.player.rotationPitch);
                }
            } else {
                if (walkBypass && !EntityUtil.INSTANCE.isMoving(mc.player)) doWalkBypas();
            }
        }

        if (mode == Mode.TELEPORT){
             for (int i = 0; i < factor; i++) {
                doPackets(mc.player.posX + forw[0], mc.player.posY, mc.player.posZ + forw[1]);
            }
        }
    }

    
    @Handler
    public Listener<PacketEvent.Receive> packetEventReceive = new Listener<>(PacketEvent.Receive.class, e -> {
        if (e.getPacket() instanceof SPacketPlayerPosLook) {
            if (!(mc.currentScreen instanceof GuiDownloadTerrain)) {
                SPacketPlayerPosLook packet = (SPacketPlayerPosLook) e.getPacket();
                if (mc.player.isEntityAlive()) {
                    if (this.tpId <= 0) {
                        this.tpId = ((SPacketPlayerPosLook) e.getPacket()).getTeleportId();
                    }
                }
                tpId = packet.getTeleportId();
            } else tpId = 0;

        }
    });
    
    public enum Teleport{
        FULL, SEMI
    }
    public enum Mode{
        TELEPORT, PACKET, MOTION
    }
    public enum Movement{
        SHIFT, NONE
    }
    
}
