package wtf.moneymod.client.mixin.mixins;

import com.google.common.base.Predicate;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.events.RenderNameTagEvent;
import wtf.moneymod.client.impl.module.render.NameTags;
import wtf.moneymod.client.impl.module.render.NoRender;
import wtf.moneymod.client.impl.utility.Globals;
import wtf.moneymod.client.mixin.accessors.IEntityRenderer;

@Mixin( EntityRenderer.class )
public class MixinEntityRenderer implements IEntityRenderer, Globals {

    @Shadow private void setupCameraTransform(float partialTicks, int pass) {}
    @Inject( method = "drawNameplate", at = @At( "HEAD" ), cancellable = true )
    private static void drawNameplate(FontRenderer fontRendererIn, String str, float x, float y, float z, int verticalShift, float viewerYaw, float viewerPitch, boolean isThirdPersonFrontal, boolean isSneaking, CallbackInfo callbackInfo) {
        RenderNameTagEvent event = new RenderNameTagEvent();
        Main.EVENT_BUS.dispatch(event);
        if (Main.getMain().getModuleManager().get(NameTags.class).isToggled() || event.isCancelled()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"hurtCameraEffect"}, at={@At(value="HEAD")}, cancellable=true)
    public void hurtCameraEffectHook(float ticks, CallbackInfo info) {
        NoRender nr = (NoRender) Main.getMain().getModuleManager().get(NoRender.class);
        if (nr.isToggled() && nr.noHurtcam) info.cancel();
    }

    @Override public void setupCamera(float partialTicks, int pass) {
        setupCameraTransform(partialTicks, pass);
    }

}