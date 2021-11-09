package wtf.moneymod.client.mixin.mixins.ducks;

import net.minecraft.client.settings.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin( KeyBinding.class )
public interface AccessorKeyBinding {

    @Accessor("pressed")
    void setPressed(boolean pressed);

}
