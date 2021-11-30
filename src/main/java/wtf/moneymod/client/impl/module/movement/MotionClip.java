package wtf.moneymod.client.impl.module.movement;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayer;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.world.EntityUtil;

@Module.Register( label = "MotionClip", desc = "Bypass Method For CC", cat = Module.Category.MOVEMENT )
public class MotionClip extends Module
{


    @Value(value = "Movement") public Movement movement = Movement.SNEAK;
    @Value(value = "Method") public Method method = Method.PACKETS;

    @Value(value = "Speed") @Bounds(min = 0, max = 8) public float speed = 1;
    @Value(value = "NoClip") public boolean noClip = true;
    @Value(value = "Set Position") public boolean setPosition = false;
    @Value(value = "Cancel Motion") public boolean cancelMotion = false;
    @Value(value = "Teleport Id") public boolean teleportingId = true;
    @Value(value = "Walk Bypass") public boolean walkBypass = true;

    public enum Method{
        MOTION, PACKETS
    }

    public enum Movement{
        SPRINT, SNEAK, NONE
    }

    public float getSpeed(){
        return 0.0008f;
    }

    int teleportId;

    @Override
    public void onToggle(){
        mc.player.noClip = false;
        teleportId = 0;
    }

    public void doWalkBypass(){
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.onGround));
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY - 1, mc.player.posZ, mc.player.onGround));
    }

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

            double[] forward = EntityUtil.forward(getSpeed() * speed);
            if (mc.player.collidedHorizontally) {
                mc.player.connection.sendPacket((Packet) new CPacketPlayer.Position(mc.player.posX + forward[0], mc.player.posY, mc.player.posZ + forward[1], true));
                mc.getConnection().sendPacket((Packet) new CPacketPlayer.Position(forward[0], -1337, forward[1], true));
                if (setPosition) mc.player.setPosition(mc.player.posX + forward[0] / 2, mc.player.posY, mc.player.posZ + forward[1] / 2);
            } else {
                if (!EntityUtil.INSTANCE.isMoving(mc.player) && walkBypass) {
                    for (int i = 0; i < 1; i++) {
                        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.onGround));
                        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY - 1, mc.player.posZ, mc.player.onGround));
                    }
                }
            }

            ++this.teleportId;
            if (teleportingId) {
                MotionClip.mc.player.connection.sendPacket((Packet) new CPacketConfirmTeleport(this.teleportId - 1));
                MotionClip.mc.player.connection.sendPacket((Packet) new CPacketConfirmTeleport(this.teleportId));
                MotionClip.mc.player.connection.sendPacket((Packet) new CPacketConfirmTeleport(this.teleportId + 1));
            }
        }

        if (method == Method.MOTION) {

            double[] sp = EntityUtil.forward(getSpeed() * speed);
            if (mc.player.collidedHorizontally) {
                mc.player.setLocationAndAngles(mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.rotationYaw, mc.player.rotationPitch);
                mc.player.setLocationAndAngles(mc.player.posX + sp[0], mc.player.posY, mc.player.posZ + sp[1], mc.player.rotationYaw, mc.player.rotationPitch);
            } else {

                if (!EntityUtil.INSTANCE.isMoving(mc.player) && walkBypass) doWalkBypass();


            }
        }
    }
}
