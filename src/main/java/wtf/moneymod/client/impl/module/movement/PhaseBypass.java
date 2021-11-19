package wtf.moneymod.client.impl.module.movement;

import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.math.BlockPos;
import wtf.moneymod.client.api.events.MoveEvent;
import wtf.moneymod.client.api.events.PacketEvent;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.misc.Timer;
import wtf.moneymod.client.impl.utility.impl.world.ChatUtil;
import wtf.moneymod.client.impl.utility.impl.world.EntityUtil;
import wtf.moneymod.client.mixin.mixins.ducks.AccessorCPacketUseEntity;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

@Module.Register( label = "BlockPhase", cat = Module.Category.MOVEMENT )
public class PhaseBypass extends Module {

    @Value( value = "Attempts" ) @Bounds( min = 1, max = 5 ) public int attempts = 1;
    @Value( value = "Speed" ) @Bounds( min = 1, max = 6 ) public int speed = 1;
    @Value( value = "Updater" ) @Bounds( min = 1, max = 5 ) public int updater = 5;
    @Value( value = "Debug" ) public boolean debug = false;
    @Value( "Vertical" ) public boolean vertical = true;
    @Value( value = "Motion" ) public boolean motion = false;
    @Value( value = "NoClip" ) public boolean noclip = false;

    Timer timer = new Timer();

    int delay;


    @Override
    public void onToggle() {
        mc.player.noClip = false;
        timer.reset();
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;

        if (mc.player.collidedHorizontally || (vertical && mc.player.collidedVertically)) {
            if (debug) ChatUtil.INSTANCE.sendMessageId("1 - c0llided", true, 8554);

            double vertically = 0;
            if(vertical && (mc.gameSettings.keyBindSneak.isKeyDown() || mc.gameSettings.keyBindJump.isKeyDown())) {
                vertically = get(Type.UPPOS);
            }
            double[] forward = EntityUtil.forward(get(Type.SPEED));
            if (noclip) mc.player.noClip = true;
            for (int i = 0; i < this.attempts; ++i) {
                this.sendPackets(mc.player.posX + forward[ 0 ], mc.player.posY + vertically, mc.player.posZ + forward[ 1 ]);
            }

            if (motion) {
                mc.player.motionX = 0.0;
                mc.player.motionZ = 0.0;
                mc.player.motionY = 0.0;
            }
        } else {
            delay++;
            System.out.println(delay);
            if (debug) ChatUtil.INSTANCE.sendMessageId("2 - not c0llided", true, 8554);
            if (noclip) mc.player.noClip = false;
            for (int i = 0; i < 129; i++) {
                if (i >= 128 && mc.player.onGround && !mc.gameSettings.keyBindSprint.isKeyDown() && !(mc.player.collidedHorizontally || (mc.player.collidedVertically && vertical))) {
                    if (debug) ChatUtil.INSTANCE.sendMessageId("Sync", true, 34442);
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY - updater, mc.player.posZ, false));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
                }
            }
        }
    }

    public void sendPackets(double q, double w, double r) {
        mc.getConnection().sendPacket(new CPacketPlayer.Position(q, w, r, mc.player.onGround));
        mc.getConnection().sendPacket(new CPacketPlayer.Position(q, 1337, w, mc.player.onGround));
    }

    double get(Type type) {
        if (type == Type.SPEED) {
            return this.speed / 100.0;
        } else
            return ( double ) (mc.gameSettings.keyBindJump.isKeyDown() ? 1 : (mc.gameSettings.keyBindSneak.isKeyDown() ? -1 : 0)) * speed / 100;
    }

    public enum Type {
        SPEED,
        UPPOS
    }

}