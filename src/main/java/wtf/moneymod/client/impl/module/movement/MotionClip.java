package wtf.moneymod.client.impl.module.movement;

import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketSoundEffect;
import wtf.moneymod.client.api.events.MoveEvent;
import wtf.moneymod.client.api.events.PacketEvent;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.world.EntityUtil;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

import javax.sound.sampled.Clip;

@Module.Register( label = "MotionClip", desc = "Bypass Method For CC", cat = Module.Category.MOVEMENT )
public class MotionClip extends Module
{

    @Value(value = "Cancel Motion") public boolean cancelMotion = false;
    @Value(value = "Speed Mode") public Clip clip = Clip.FIRST;
    @Value(value = "Sync Delay") @Bounds(max = 10) public int delay = 1;
    @Value(value = "Glide") public boolean glide = false;
    @Value(value = "YUpdater") public boolean yUpdater = false;
    @Value(value = "No Clip") public boolean noClip = false;

    public enum Clip{
        FIRST,SECOND
    }

    int syncdelay;

    public float getSpeed(){
        return 0.008F;
    }

    @Override
    public void onTick() {
        syncdelay++;
        if (nullCheck()) return;
        double[] s = EntityUtil.forward(getSpeed());
        mc.player.noClip = this.noClip;
        if (glide) mc.player.motionY = 0;
        double yaw = mc.player.rotationYaw * 0.017453292;
        double power = getSpeed();


        if (cancelMotion){
            mc.player.motionZ = 0;
            mc.player.motionX = 0;
        }

        if (mc.gameSettings.keyBindSneak.isKeyDown()) {
            if (clip == Clip.SECOND) {
                if (mc.player.collidedHorizontally) {
                    mc.player.setLocationAndAngles(mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.rotationYaw, mc.player.rotationPitch);
                    mc.player.setLocationAndAngles(mc.player.posX - Math.sin(yaw) * power, mc.player.posY + (yUpdater ? 0.08 : 0), mc.player.posZ + Math.cos(yaw) * power, mc.player.rotationYaw, mc.player.rotationPitch);
                } else {
                    if (!EntityUtil.INSTANCE.isMoving(mc.player)) {
                        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.onGround));
                        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY - 1, mc.player.posZ, mc.player.onGround));
                    }
                }
            }

            if (clip == Clip.FIRST) {
                mc.player.setPosition(mc.player.posX, mc.player.posY, mc.player.posZ);
                mc.player.setPosition(mc.player.posX + s[0], mc.player.posY + (yUpdater ? 0.01 : 0), mc.player.posZ + s[1]);
            }
        }
    }

    public void sendPackets(double q, double w, double r) {
        mc.getConnection().sendPacket(new CPacketPlayer.Position(q, w, r, mc.player.onGround));
        mc.getConnection().sendPacket(new CPacketPlayer.Position(0,767, 0, mc.player.onGround));
    }

}
