package wtf.moneymod.client.mixin.mixins;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.impl.module.render.NameTags;

@Mixin( EntityRenderer.class )
public class MixinEntityRenderer {

    @Inject( method = "drawNameplate", at = @At( "HEAD" ), cancellable = true )
    private static void drawNameplate( FontRenderer fontRendererIn, String str, float x, float y, float z, int verticalShift, float viewerYaw, float viewerPitch, boolean isThirdPersonFrontal, boolean isSneaking, CallbackInfo callbackInfo ) {
        if (Main.getMain().getModuleManager().get( NameTags.class ).isToggled( ) ) {
            callbackInfo.cancel( );
        }
    }

}