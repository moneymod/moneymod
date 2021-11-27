package wtf.moneymod.client.mixin.mixins;

import com.google.common.base.Predicate;
import net.minecraft.client.Minecraft;
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
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.events.CameraClipEvent;
import wtf.moneymod.client.api.events.Render3DEvent;
import wtf.moneymod.client.api.events.RenderNameTagEvent;
import wtf.moneymod.client.impl.module.player.NoEntityTrace;
import wtf.moneymod.client.impl.module.render.NameTags;
import wtf.moneymod.client.impl.module.render.NoRender;
import wtf.moneymod.client.impl.utility.Globals;
import wtf.moneymod.client.mixin.accessors.IEntityRenderer;

@Mixin( EntityRenderer.class )
public class MixinEntityRenderer implements IEntityRenderer {

    @Shadow
    private Minecraft mc;

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

    @ModifyVariable( method = "orientCamera", at = @At( "STORE" ), ordinal = 3 )
    private double orientCameraX( double distance )
    {
        CameraClipEvent event = new CameraClipEvent( distance );
        Main.EVENT_BUS.dispatch( event );
        return event.distance;
    }

    @ModifyVariable( method = "orientCamera", at = @At( "STORE" ), ordinal = 7 )
    private double orientCameraZ( double distance )
    {
        CameraClipEvent event = new CameraClipEvent( distance );
        Main.EVENT_BUS.dispatch( event );
        return event.distance;
    }

    @Inject( method = "renderWorldPass",
            at = @At( value = "INVOKE", target = "net/minecraft/client/renderer/GlStateManager.clear(I)V", ordinal = 1, shift = At.Shift.AFTER ) )
    private void renderWorldPass_Pre( int pass, float partialTicks, long finishTimeNano, CallbackInfo info )
    {
        Render3DEvent event = new Render3DEvent( partialTicks );
        Main.EVENT_BUS.dispatch( event );
    }

    // pasted from lambda who cares
    @Inject( method = "getMouseOver", at = @At( value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getPositionEyes(F)Lnet/minecraft/util/math/Vec3d;", shift = At.Shift.BEFORE ), cancellable = true )
    public void nt_getMouseOver( float partialTicks, CallbackInfo info )
    {
        if( Main.getMain( ).getModuleManager( ).get( NoEntityTrace.class ).isToggled( ) )
        {
            info.cancel( );
            mc.profiler.endSection( );
        }
    }
}