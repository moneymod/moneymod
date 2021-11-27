package wtf.moneymod.client.api.management.impl;

import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import wtf.moneymod.client.api.events.PacketEvent;
import wtf.moneymod.client.impl.utility.Globals;
import wtf.moneymod.client.mixin.mixins.ducks.AccessorCPacketPlayer;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

public class RotationManagement implements Globals {

    private float yaw, pitch;

    @Handler public Listener<PacketEvent.Send> packetEventSend = new Listener<>(PacketEvent.Send.class, e -> {
        if (e.getPacket() instanceof CPacketPlayer) {
            (( AccessorCPacketPlayer ) e.getPacket()).setYaw(yaw);
            (( AccessorCPacketPlayer ) e.getPacket()).setPitch(pitch);
        }
    });

    public void update() {
        yaw = mc.player.rotationYaw;
        pitch = mc.player.rotationPitch;
    }

    public void reset() {
        mc.player.rotationYaw = yaw;
        mc.player.rotationPitch = pitch;
        mc.player.rotationYawHead = yaw;
    }

    public void set(float yaw, float pitch, boolean packet) {
        if (packet) {
            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(yaw, pitch, mc.player.onGround));
        } else {
            mc.player.rotationYaw = yaw;
            mc.player.rotationYawHead = yaw;
            mc.player.rotationPitch = pitch;
        }
    }

    public float[] look(BlockPos bp, boolean packet) {
        return look(bp, packet, true);
    }

    public float[] look(Entity bp, boolean packet) {
        return look(bp, packet, true);
    }

    public float[] look(BlockPos bp, boolean packet, boolean set) {
        float[] angles = calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d(bp.getX() + .5f, bp.getY() + .5f, bp.getZ() + .5f));
        if (set) set(angles[ 0 ], angles[ 1 ], packet);
        return angles;
    }

    public float[] look(Entity entity, boolean packet, boolean set) {
        float[] angles = calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), entity.getPositionEyes(mc.getRenderPartialTicks()));
        if (set) set(angles[ 0 ], angles[ 1 ], packet);
        return angles;
    }

    //credits to 3arthqu4ke cuz im bad at math :)
    public static float[] calcAngle(Vec3d from, Vec3d to) {
        final double difX = to.x - from.x;
        final double difY = (to.y - from.y) * -1.0F;
        final double difZ = to.z - from.z;
        final double dist = MathHelper.sqrt(difX * difX + difZ * difZ);
        return new float[] { ( float ) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0f), ( float ) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difY, dist))) };
    }

}