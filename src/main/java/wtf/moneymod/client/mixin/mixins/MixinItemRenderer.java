package wtf.moneymod.client.mixin.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.impl.module.render.CustomModel;
import wtf.moneymod.client.impl.module.render.NoBob;
import wtf.moneymod.client.impl.module.render.NoRender;
import wtf.moneymod.client.impl.utility.Globals;

@Mixin( value = { ItemRenderer.class } )
public class MixinItemRenderer implements Globals {

    @Shadow @Final private Minecraft mc;

    @Inject( method = "renderItemSide", at = @At( value = "HEAD" ) )
    public void renderItemSide(EntityLivingBase entityLivingBase, ItemStack stack, ItemCameraTransforms.TransformType transform, boolean leftHanded, CallbackInfo info) {
        CustomModel view = ( CustomModel ) Main.getMain().getModuleManager().get(CustomModel.class);
        if (view.isToggled() && entityLivingBase == mc.player) {
            GlStateManager.scale(view.scaleX, view.scaleY, view.scaleZ);
            if (mc.player.getActiveItemStack() != stack) {
                GlStateManager.translate((view.translateX * 0.1f) * (leftHanded ? -1 : 1), view.translateY * 0.1f, view.translateZ * 0.1);
            }
        }
    }

    @Inject( method = { "renderFireInFirstPerson" }, at = { @At( value = "HEAD" ) }, cancellable = true )
    public void renderFireInFirstPersonHook(CallbackInfo info) {
        NoRender nr = ( NoRender ) Main.getMain().getModuleManager().get(NoRender.class);

        if (nr.isToggled() && nr.noFire) {
            info.cancel();
        }
    }

    @Inject( method = { "renderSuffocationOverlay" }, at = { @At( value = "HEAD" ) }, cancellable = true )
    public void renderSuffocationOverlay(CallbackInfo ci) {
        NoRender nr = ( NoRender ) Main.getMain().getModuleManager().get(NoRender.class);

        if (nr.isToggled() && nr.noBlocks) {
            ci.cancel();
        }
    }

    @Inject( method = "rotateArm", at = @At( "HEAD" ), cancellable = true )
    private void rotateArm(float f, CallbackInfo info) {
        NoBob nobob = ( NoBob ) Main.getMain().getModuleManager().get(NoBob.class);
        if (nobob.isToggled() && nobob.nosway)
            info.cancel();
    }

}
