package wtf.moneymod.client.api.management.impl;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import wtf.moneymod.client.impl.utility.Globals;
import wtf.moneymod.client.impl.utility.impl.math.Rotation;

import javax.annotation.Nonnull;

public class RotationHandler implements Globals {

    private static final RotationHandler INSTANCE = new RotationHandler();

    public static RotationHandler getInstance() {
        return INSTANCE;
    }

    Rotation rotation;
    boolean set;

    public void reset() {
        this.rotation = new Rotation(mc.player.rotationYaw, mc.player.rotationPitch);
        set = false;
    }

    public void setRotation(float yaw, float pitch) {
        setRotation(new Rotation(yaw, pitch));
    }

    public void setRotation(Rotation rotation) {
        this.rotation = rotation;
        this.set = true;
    }

    public Rotation getRotation() {
        return rotation;
    }

    public boolean isSet() {
        return set;
    }

    public static Rotation calcRotation(@Nonnull BlockPos bp) {
        return calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d(bp.getX() + .5f, bp.getY() + .5f, bp.getZ() + .5f));
    }

    public static Rotation calcRotation(@Nonnull Entity e) {
        return calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), e.getPositionEyes(mc.getRenderPartialTicks()));
    }

    public static Rotation calcAngle(Vec3d from, Vec3d to) {
        final double difX = to.x - from.x;
        final double difY = (to.y - from.y) * -1.0F; //lol nah nad)
        final double difZ = to.z - from.z;
        final double dist = MathHelper.sqrt(difX * difX + difZ * difZ);
        return new Rotation(( float ) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0f), ( float ) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difY, dist))));
    }

}
