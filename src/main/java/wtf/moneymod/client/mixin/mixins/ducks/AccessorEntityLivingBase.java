package wtf.moneymod.client.mixin.mixins.ducks;

import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityLivingBase.class)
public interface AccessorEntityLivingBase {
    @Accessor(value = "jumpTicks") void setJumpTicks(int value);
}
