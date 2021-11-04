package wtf.moneymod.client.impl.utility.impl.world;

import net.minecraft.entity.EntityLivingBase;
import wtf.moneymod.client.impl.utility.Globals;

/**
 * @author cattyn
 * @since 11/02/21
 */

public enum EntityUtil implements Globals {
    INSTANCE;

    public boolean isMoving(EntityLivingBase entity) {
        return entity.moveStrafing != 0 || entity.moveForward != 0;
    }

    public static double[] forward(double d) {
        float f = mc.player.movementInput.moveForward;
        float f2 = mc.player.movementInput.moveStrafe;
        float f3 = mc.player.prevRotationYaw + (mc.player.rotationYaw - mc.player.prevRotationYaw) * mc.getRenderPartialTicks();
        if (f != 0.0f) {
            if (f2 > 0.0f) {
                f3 += (float)(f > 0.0f ? -45 : 45);
            } else if (f2 < 0.0f) {
                f3 += (float)(f > 0.0f ? 45 : -45);
            }
            f2 = 0.0f;
            if (f > 0.0f) {
                f = 1.0f;
            } else if (f < 0.0f) {
                f = -1.0f;
            }
        }
        double d2 = Math.sin(Math.toRadians(f3 + 90.0f));
        double d3 = Math.cos(Math.toRadians(f3 + 90.0f));
        double d4 = (double)f * d * d3 + (double)f2 * d * d2;
        double d5 = (double)f * d * d2 - (double)f2 * d * d3;
        return new double[]{d4, d5};
    }
    public static float getHealth ( final EntityLivingBase player ) {
        return player.getHealth( ) + player.getAbsorptionAmount( );
    }
}
