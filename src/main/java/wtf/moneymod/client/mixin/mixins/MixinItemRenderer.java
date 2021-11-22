package wtf.moneymod.client.mixin.mixins;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.impl.module.render.CustomModel;
import wtf.moneymod.client.impl.module.render.NoRender;
import wtf.moneymod.client.impl.utility.Globals;

@Mixin(value={ItemRenderer.class})
public class MixinItemRenderer implements Globals {
    @Inject(method = {"renderItemSide"}, at = {@At(value = "HEAD")})
    public void renderItemSide(EntityLivingBase entityLivingBase, ItemStack stack, ItemCameraTransforms.TransformType transform, boolean leftHanded, CallbackInfo info) {
        CustomModel view = (CustomModel) Main.getMain().getModuleManager().get(CustomModel.class);
        if (view.isToggled()) {
            GlStateManager.scale(view.scaleX,view.scaleX,view.scaleX);
        }
    }

    @Inject(method={"renderFireInFirstPerson"}, at={@At(value="HEAD")}, cancellable=true)
    public void renderFireInFirstPersonHook(CallbackInfo info) {
        NoRender nr = (NoRender) Main.getMain().getModuleManager().get(NoRender.class);

        if (nr.isToggled() && nr.noFire) {
            info.cancel();
        }
    }

    @Inject(method={"renderSuffocationOverlay"}, at={@At(value="HEAD")}, cancellable=true)
    public void renderSuffocationOverlay(CallbackInfo ci) {
        NoRender nr = (NoRender) Main.getMain().getModuleManager().get(CustomModel.class);

        if (nr.isToggled() && nr.noBlocks) {
            ci.cancel();
        }
    }
}
