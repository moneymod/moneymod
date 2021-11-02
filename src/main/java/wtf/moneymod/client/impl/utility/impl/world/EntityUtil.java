package wtf.moneymod.client.impl.utility.impl.world;

import net.minecraft.entity.EntityLivingBase;
import wtf.moneymod.client.impl.utility.Globals;

public enum EntityUtil implements Globals {
    INSTANCE;

    public boolean isMoving(EntityLivingBase entity) {
        return entity.moveStrafing != 0 || entity.moveForward != 0;
    }

}
