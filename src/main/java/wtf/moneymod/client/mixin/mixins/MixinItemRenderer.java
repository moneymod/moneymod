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
}
