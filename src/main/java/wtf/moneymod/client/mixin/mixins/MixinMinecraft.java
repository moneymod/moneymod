package wtf.moneymod.client.mixin.mixins;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.moneymod.client.api.management.impl.ConfigManager;

@Mixin( Minecraft.class )
public class MixinMinecraft {

    @Inject( method = "shutdown()V", at = @At( "HEAD" ) )
    public void shutdown(CallbackInfo callbackInfo) {
        ConfigManager.getInstance().start();
    }

}
