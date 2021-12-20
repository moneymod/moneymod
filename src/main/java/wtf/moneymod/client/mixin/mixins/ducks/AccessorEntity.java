package wtf.moneymod.client.mixin.mixins.ducks;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Entity.class)
public interface AccessorEntity {
    @Accessor(value = "nextStepDistance") void setNextStepDistance(int value);

}
