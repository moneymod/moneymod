package wtf.moneymod.client.impl.module.movement;

import net.minecraft.network.play.client.CPacketPlayer;
import org.lwjgl.input.Keyboard;
import wtf.moneymod.client.api.events.MoveEvent;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.world.EntityUtil;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

@Module.Register( label = "ClipTest", cat = Module.Category.MOVEMENT)
public class ClipTest extends Module {

    @Value(value = "Speed") public Speed speed = Speed.SLOW;
    @Value(value = "Mode") public Mode mode = Mode.SETPOS;
    @Value(value = "Cancel Move") public boolean cancelMove = true;
    @Value(value = "No Clip") public boolean noClip = true;
    @Value(value = "YPos") public boolean ypos = true;
    @Value(value = "YPosMax") @Bounds(max = 200) public int yposmax = 200;
    public enum Speed{
        FAST,SLOW,VERYSLOW
    }
    public enum Mode{
        SETPOS, SETPOS2, PACKET, PACKET2, SETPOS3METHOD, SHITPACKETLOL
    }

    public float getSpeed(){
        if (speed == Speed.SLOW) return 0.001f;
        if (speed == Speed.FAST) return 0.008f;
        if (speed == Speed.VERYSLOW) return 0.0005f;
        return 1;
    }

    public void sendPackets(double q, double w, double r) {
        mc.getConnection().sendPacket(new CPacketPlayer.Position(q, w, r, mc.player.onGround));
        if (ypos) mc.getConnection().sendPacket(new CPacketPlayer.Position(0,yposmax, 0, mc.player.onGround));
    }

    @Override
    public void onTick(){

        if (nullCheck()) return;

        if (cancelMove){
            mc.player.motionX = 0.0;
            mc.player.motionY = 0.0;
            mc.player.motionZ = 0.0;
        }

        mc.player.noClip = noClip;

        double[] t = EntityUtil.forward(getSpeed());

        System.out.println(getSpeed());
        if (mc.gameSettings.keyBindSneak.isKeyDown()) {
            if (mode == Mode.SETPOS) {
                mc.player.setPosition(mc.player.posX, mc.player.posY, mc.player.posZ);
                mc.player.setPosition(mc.player.posX + t[0], mc.player.posY, mc.player.posZ + t[1]);
            }
            if (mode == Mode.SETPOS2) {
                mc.player.setLocationAndAngles(mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.rotationYaw, mc.player.rotationPitch);
                mc.player.setLocationAndAngles(mc.player.posX + t[0], mc.player.posY, mc.player.posZ + t[1], mc.player.rotationYaw, mc.player.rotationPitch);
            }
            if (mode == Mode.SETPOS3METHOD) {
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY - 3, mc.player.posZ, false));
                mc.player.setLocationAndAngles(mc.player.posX + t[0], mc.player.posY, mc.player.posZ + t[1], mc.player.rotationYaw, mc.player.rotationPitch);
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 3, mc.player.posZ, false));
            }
            if (mode == Mode.PACKET) {
                sendPackets(mc.player.posX + t[0], mc.player.posY, mc.player.posZ + t[1]);
            }
            if (mode == Mode.PACKET2) {
                sendPackets(mc.player.posX, mc.player.posY, mc.player.posZ);
                sendPackets(mc.player.posX + t[0], mc.player.posY, mc.player.posZ + t[1]);
            }
            if (mode == Mode.SHITPACKETLOL) {
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY - 3, mc.player.posZ, false));
                sendPackets(mc.player.posX, mc.player.posY, mc.player.posZ);
                sendPackets(mc.player.posX + t[0], mc.player.posY, mc.player.posZ + t[1]);
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 3, mc.player.posZ, false));
            }
        }
    }
}