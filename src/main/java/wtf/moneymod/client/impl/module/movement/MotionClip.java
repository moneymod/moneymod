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

    @Value(value = "Clip Method") public Clip clip = Clip.SETPOS;
    @Value(value = "Cancel Motion") public boolean cancelMotion = false;
    @Value(value = "Speed Mode") public Speed speed = Speed.MEDIUM;
    @Value(value = "YSlow Updater") public boolean ySlowUpdater = false;
    @Value(value = "Glide") public boolean glide = false;
    @Value(value = "No Clip") public boolean noClip = false;
    @Value(value = "Reduction") public boolean reduction = false;
    @Value(value = "Reduction XZ") @Bounds(max = 5) public float reductionXZ = 0.1f;
    @Value(value = "Reduction Only Sneak") public boolean isReduction = false;

    public enum Speed{
        FAST, MEDIUM, SLOW, VERYSLOW;
    }

    public enum Clip{
        SETPOS, SETANGLES, OLDMETHOD,
    }

    public float getSpeed(){
        switch (speed){
            case MEDIUM:
                return 0.05F;
            case SLOW:
                return 0.01F;
            case FAST:
                return 0.1f;
            case VERYSLOW:
                return 0.003f;
        }
        return 0.1F;
    }

    @Override
    public void onTick() {
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
            if (clip == Clip.OLDMETHOD){
                mc.player.setPosition(mc.player.posX, mc.player.posY, mc.player.posZ);
                mc.player.setPosition(mc.player.posX + s[0], mc.player.posY + (ySlowUpdater ? 0.001 : 0), mc.player.posZ + s[1]);
            }
            if (clip == Clip.SETPOS){
                mc.player.setPosition(mc.player.posX, mc.player.posY, mc.player.posZ);
                mc.player.setPosition(mc.player.posX - Math.sin(yaw) * power, mc.player.posY, mc.player.posZ + Math.cos(yaw) * power);
            }
            if (clip == Clip.SETANGLES){
                mc.player.setLocationAndAngles(mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.rotationYaw, mc.player.rotationPitch);
                mc.player.setLocationAndAngles(mc.player.posX - Math.sin(yaw) * power, mc.player.posY, mc.player.posZ + Math.cos(yaw) * power, mc.player.rotationYaw, mc.player.rotationPitch);

            }
        }
    }

    @Handler
    public Listener<MoveEvent> onMove = new Listener<>(MoveEvent.class, e -> {
        if (nullCheck()) return;
        if (isReduction) {
            if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                if (reduction) {
                    e.motionX = e.motionX * reductionXZ / 10.0;
                    e.motionZ = e.motionZ * reductionXZ / 10.0;
                }
            }
        } else {
            if (reduction) {
                e.motionX = e.motionX * reductionXZ / 10.0;
                e.motionZ = e.motionZ * reductionXZ / 10.0;
            }
        }
    });
}
