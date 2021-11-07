package wtf.moneymod.client.mixin.mixins.ducks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.Minecraft;
@Mixin(Minecraft.class)
public interface IMinecraft {

    @Accessor("rightClickDelayTimer")
    void setRightClickDelayTimer(int rightClickDelayTimer);

}