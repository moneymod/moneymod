package wtf.moneymod.client.mixin.mixins.ducks;

import net.minecraft.client.multiplayer.PlayerControllerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerControllerMP.class)
public interface AccessorPlayerControllerMP {
    @Accessor("isHittingBlock")
    void mm_setIsHittingBlock(boolean value);
}
