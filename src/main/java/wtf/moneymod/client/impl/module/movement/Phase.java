package wtf.moneymod.client.impl.module.movement;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wtf.moneymod.client.GitInfo;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.events.MoveEvent;
import wtf.moneymod.client.api.events.PacketEvent;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.misc.Timer;
import wtf.moneymod.client.impl.utility.impl.render.fonts.FontRender;
import wtf.moneymod.client.impl.utility.impl.world.ChatUtil;
import wtf.moneymod.client.impl.utility.impl.world.EntityUtil;
import wtf.moneymod.client.mixin.mixins.ducks.AccessorKeyBinding;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

@Module.Register( label = "Phase", cat = Module.Category.MOVEMENT)
public class Phase extends Module {

    @Value(value = "Mode") public Mode mode = Mode.DEFAULT;
    @Value(value = "Attempts") @Bounds(min = 1, max = 5) public int attempts = 1;
    @Value(value = "Speed") @Bounds(min = 1, max = 5) public int speed = 1;

    //BLOCK PHASE
    @Value(value = "Updater") @Bounds(min = 0, max = 3) public float updater = 3;
    @Value(value = "Sync Delay") @Bounds(min = 1, max = 10) public int syncDelay = 10;
    @Value(value = "Motion") public boolean motion = false;
    @Value(value = "No Clip") public boolean noclip = false;
    @Value(value = "Teleport Id") public boolean teleportId = false;
    @Value(value = "Only Moving") public boolean onlyMoving = false;
    @Value(value = "Reduction") public boolean reduction = false;
    @Value(value = "Auto") public boolean auto = false;
    //VSE DRYGOE
    @Value(value = "Debug Panel") public boolean info = false;
    @Value(value = "Position Y") @Bounds(min = 1, max = 100) public int posY = 40;
    Timer timer = new Timer();
    int teleportID = 0;
    int delay;
    int wTapDelay;
    boolean wtap;
    boolean pressed;
    @Override
    public void onToggle(){
        timer.reset();
        delay = 0;
        wTapDelay = 0;
        wtap = false;
        pressed = false;
    }

    String collided = "none";
    String bypass = "none";
    String syncs = "none";


    @Override public void onRender2D() {
        int offset = 0;

        double[] t = EntityUtil.forward(get(Type.SPEED));
        String[] array = new String[]{
                "Collided: " + collided,
                "Bypass: " + bypass,
                "Speed: " +  String.format("%.3f", t[0]) + " : " + String.format("%.3f", t[1]),
                "Sync: " + syncs,
                "Delay: " + delay
        };
        if (info) {
            for (int i = 0; i < array.length; ++i) {
                String s = array[i];
                FontRender.drawStringWithShadow(s, 2, posY + offset, -1);
                offset += 9;
            }
        }
    }
    @Handler
    public Listener<PacketEvent.Receive> packeEventReceive = new Listener<>(PacketEvent.Receive.class, e -> {
        if (nullCheck()) return;
        if (e.getPacket() instanceof SPacketPlayerPosLook && teleportId) {
            System.out.println("1");
            if (auto){
                wtap = true;
            }
            teleportID = ((SPacketPlayerPosLook) ((Object) e.getPacket())).getTeleportId();
            mc.getConnection().sendPacket(new CPacketConfirmTeleport(teleportID + 1));
        }
    });

    @Override
    public void onTick() {
        if (nullCheck()) return;


        if (mode == Mode.BYPASS){
            double[] t = EntityUtil.forward(get(Type.SPEED));
            if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                mc.player.setPosition(mc.player.posX, mc.player.posY, mc.player.posZ);
                mc.player.setPosition(mc.player.posX + t[0], mc.player.posY, mc.player.posZ + t[1]);
                if (reduction) {
                    mc.player.motionX = mc.player.motionX * 0.001 / 10.0;
                    mc.player.motionZ = mc.player.motionZ * 0.001 / 10.0;
                    mc.player.motionY = mc.player.motionY * 0.001 / 10.0;
                }
                if (motion){
                    mc.player.motionX = 0.0; mc.player.motionZ = 0.0; mc.player.motionY = 0.0;
                }
            }
        }


        //best bypass

        if (mode == Mode.PHASEBLOCK){
            delay++;
            if (mc.player.collidedHorizontally) {
                collided = ChatFormatting.GREEN + "true";
                if (noclip) mc.player.noClip = true;
                if (timer.passed(50)) {
                    double[] forward = EntityUtil.forward(get(Type.SPEED));
                    for (int i = 0; i < this.attempts; ++i) {
                        bypass = ChatFormatting.RED + "false";
                        this.sendPackets(mc.player.posX + forward[0], mc.player.posY, mc.player.posZ + forward[1]);
                    }
                }
                timer.reset();
                if (motion) {
                    mc.player.motionX = 0.0; mc.player.motionZ = 0.0; mc.player.motionY = 0.0;
                }
            } else {
                collided = ChatFormatting.RED + "false";
                if (noclip) mc.player.noClip = false;
                if (delay >= syncDelay) {
                    if (!EntityUtil.INSTANCE.isMoving(mc.player) && onlyMoving) return;
                    syncs = ChatFormatting.GREEN + "true"; bypass = ChatFormatting.GREEN + "true";
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.onGround));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY - updater, mc.player.posZ, mc.player.onGround));
                } else syncs = ChatFormatting.RED + "false";
                if (delay >= syncDelay){
                    delay = 0;
                }
            }
        }

        //DEFAULT MODE:
        if (mode == Mode.DEFAULT) {
            mc.player.motionX = 0.0;
            mc.player.motionY = 0.0;
            mc.player.motionZ = 0.0;
            if (mc.player.collidedHorizontally) {
                if (timer.passed(50)) {
                    double[] move = EntityUtil.forward(get(Type.SPEED));
                    for (int i = 0; i < attempts; ++i) {
                        sendPackets(mc.player.posX + move[0], mc.player.posY + get(Type.UPPOS), mc.player.posZ + move[1]);
                    }
                }
                timer.reset();
            }
        }
    }

    @Handler
    public Listener<MoveEvent> onMove = new Listener<>(MoveEvent.class, e -> {
        if (nullCheck()) return;
        //eto only bypass and default
        if (mode == Mode.TELEPORT) {
            double[] forward = EntityUtil.forward(get(Type.SPEED));
            for (int i = 0; i < this.attempts; ++i) {
                 this.sendPackets(mc.player.posX + forward[0], mc.player.posY + get(Type.UPPOS), mc.player.posZ + forward[1]);
            }
            e.motionX = e.motionX * 0.0001 / 10.0; e.motionZ = e.motionZ * 0.0001 / 10.0; e.motionY = e.motionY * 0.0001 / 10.0;
        } else {
            if (mc.player.collidedHorizontally && mode == Mode.DEFAULT){
                e.motionX = 0; e.motionZ = 0; e.motionY = 0;
            }
        }

    });

    public void sendPackets(double q, double w, double r) {
        mc.getConnection().sendPacket(new CPacketPlayer.Position(q, w, r, mc.player.onGround));
        mc.getConnection().sendPacket(new CPacketPlayer.Position(0,767, 0, mc.player.onGround));

    }

    double get(Type type){
        if (type == Type.SPEED) {
            return this.speed / 150.0;
        } else if (type == Type.UPPOS){
            return (double)(mc.gameSettings.keyBindJump.isKeyDown() ? 1 : (mc.gameSettings.keyBindSneak.isKeyDown() ? -1 : 0)) * speed / 100;
        }
        return 0;
    }

    public enum Mode{DEFAULT, TELEPORT, PHASEBLOCK, BYPASS, TESTMETHOD}
    public enum Type{SPEED,UPPOS}

}