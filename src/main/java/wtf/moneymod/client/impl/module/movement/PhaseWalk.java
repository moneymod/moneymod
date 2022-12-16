package wtf.moneymod.client.impl.module.movement;

import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import org.apache.logging.log4j.core.jmx.Server;
import org.lwjgl.input.Keyboard;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.events.MoveEvent;
import wtf.moneymod.client.api.events.PacketEvent;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.module.misc.ServerFlag;
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
    @Value(value = "Delay") @Bounds(min = 0,max = 20) public int delay = 1;
    @Value("Flag") public boolean flag = false;
    @Value("Flag Delay") @Bounds(min = 100, max = 4000) public int flagDelay = 500;
    @Value(value = "Fall Packet") public boolean fallPacket = true;
    @Value(value = "Teleport Id") public boolean teleportId = true;
    @Value(value = "Cancel Motion") public boolean cancelMotion = true;
    @Value(value = "WalkBypass") public boolean walkBypass = true;
    @Value(value = "Collided Timer") public boolean collidedTimer = false;
    @Value(value = "Timer Speed") @Bounds(min = 1,max = 8) public float timerSpeed = 2;

    private int walkDelay = 0;
    private int tpId = 0;
    private Timer timer = new Timer();
    private Timer flagger = new Timer();

    @Override
    public void onToggle(){
        timer.reset();
        walkDelay = 0;
        tpId = 0;
        if (mc.player != null)mc.player.noClip = false;
    }

    public void doWalkBypas(){
        if (walkDelay >= 1){
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX,mc.player.posY,mc.player.posZ, mc.player.onGround));
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX,mc.player.posY - 1,mc.player.posZ, mc.player.onGround));
        }
    }

    public void doPackets(double x, double y, double z) {
        mc.player.connection.sendPacket(new CPacketPlayer.Position(x, y, z, mc.player.onGround));
        switch (teleport) {
            case FULL:
                mc.player.connection.sendPacket(new CPacketPlayer.Position(x, -1337, z, mc.player.onGround));
                break;

            case SEMI:
                mc.player.connection.sendPacket(new CPacketPlayer.Position(x, 0, z, mc.player.onGround));
                break;
        }
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;

        walkDelay++;
        mc.player.noClip = true;

        if (movement == Movement.SHIFT && !mc.gameSettings.keyBindSneak.isKeyDown()) return;

        if (flag && flagger.passed(flagDelay)) {
            Main.getMain().getModuleManager().get(ServerFlag.class).setToggled(true);
            flagger.reset();
            return;
        }

        if (cancelMotion) {
            mc.player.motionX = 0;
            mc.player.motionZ = 0;
        }

        double[] forw = EntityUtil.forward(speed / 100);

        if (mc.player.collidedHorizontally){

            if (teleportId) {
                tpId++;
                mc.player.connection.sendPacket(new CPacketConfirmTeleport(tpId - 1));
                mc.player.connection.sendPacket(new CPacketConfirmTeleport(tpId));
                mc.player.connection.sendPacket(new CPacketConfirmTeleport(tpId + 1));
            }

            if (collidedTimer) Main.TICK_TIMER = timerSpeed;

            if (mode == Mode.PACKET) {
                if (timer.passed(delay * 100L)) {
                    for (int i = 0; i < factor; i++) doPackets(mc.player.posX + forw[0], mc.player.posY, mc.player.posZ + forw[1]);
                    if (fallPacket) mc.player.connection.sendPacket((Packet) new CPacketEntityAction((Entity) mc.player, CPacketEntityAction.Action.STOP_RIDING_JUMP));
                    timer.reset();
                }
            }
            if (mode == Mode.MOTION) {
                mc.player.setLocationAndAngles(mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.rotationYaw, mc.player.rotationPitch);
                for (int i = 0; i < factor; i++) mc.player.setLocationAndAngles(mc.player.posX + forw[0], mc.player.posY, mc.player.posZ + forw[1], mc.player.rotationYaw, mc.player.rotationPitch);
            }

        } else {
            Main.TICK_TIMER = 1;
            if (!EntityUtil.INSTANCE.isMoving(mc.player) && walkBypass) {
                for (int i = 0; i < 1; i++) {
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.onGround));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY - 1, mc.player.posZ, mc.player.onGround));
                }
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
