package wtf.moneymod.client.mixin.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.events.CameraClipEvent;
import wtf.moneymod.client.api.events.Render3DEvent;
import wtf.moneymod.client.api.events.RenderNameTagEvent;
import wtf.moneymod.client.impl.module.player.NoEntityTrace;
import wtf.moneymod.client.impl.module.render.Ambience;
import wtf.moneymod.client.impl.module.render.NameTags;
import wtf.moneymod.client.impl.module.render.NoRender;
import wtf.moneymod.client.mixin.accessors.IEntityRenderer;

import javax.vecmath.Vector3f;
import java.awt.*;

@Mixin( EntityRenderer.class )
public class MixinEntityRenderer implements IEntityRenderer {

    @Shadow
    private Minecraft mc;

    @Shadow @Final private int[] lightmapColors;

    @Shadow private void setupCameraTransform(float partialTicks, int pass) {}

    @Inject( method = "drawNameplate", at = @At( "HEAD" ), cancellable = true )
    private static void drawNameplate(FontRenderer fontRendererIn, String str, float x, float y, float z, int verticalShift, float viewerYaw, float viewerPitch, boolean isThirdPersonFrontal, boolean isSneaking, CallbackInfo callbackInfo) {
        RenderNameTagEvent event = new RenderNameTagEvent();
        Main.EVENT_BUS.dispatch(event);
        if (Main.getMain().getModuleManager().get(NameTags.class).isToggled() || event.isCancelled()) {
            callbackInfo.cancel();
        }
    }

    @Inject( method = { "hurtCameraEffect" }, at = { @At( value = "HEAD" ) }, cancellable = true )
    public void hurtCameraEffectHook(float ticks, CallbackInfo info) {
        NoRender nr = ( NoRender ) Main.getMain().getModuleManager().get(NoRender.class);
        if (nr.isToggled() && nr.noHurtcam) info.cancel();
    }

    @Override public void setupCamera(float partialTicks, int pass) {
        setupCameraTransform(partialTicks, pass);
    }

    @ModifyVariable( method = "orientCamera", at = @At( "STORE" ), ordinal = 3 )
    private double orientCameraX(double distance) {
        CameraClipEvent event = new CameraClipEvent(distance);
        Main.EVENT_BUS.dispatch(event);
        return event.distance;
    }

    @ModifyVariable( method = "orientCamera", at = @At( "STORE" ), ordinal = 7 )
    private double orientCameraZ(double distance) {
        CameraClipEvent event = new CameraClipEvent(distance);
        Main.EVENT_BUS.dispatch(event);
        return event.distance;
    }

    @Inject( method = "renderWorldPass",
            at = @At( value = "INVOKE", target = "net/minecraft/client/renderer/GlStateManager.clear(I)V", ordinal = 1, shift = At.Shift.AFTER ) )
    private void renderWorldPass_Pre(int pass, float partialTicks, long finishTimeNano, CallbackInfo info) {
        Render3DEvent event = new Render3DEvent(partialTicks);
        Main.EVENT_BUS.dispatch(event);
    }

    // pasted from lambda who cares
    @Inject( method = "getMouseOver", at = @At( value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getPositionEyes(F)Lnet/minecraft/util/math/Vec3d;", shift = At.Shift.BEFORE ), cancellable = true )
    public void nt_getMouseOver(float partialTicks, CallbackInfo info) {
        NoEntityTrace net = ( NoEntityTrace ) Main.getMain().getModuleManager().get(NoEntityTrace.class);
        if (net.isToggled() && net.check()) {
            info.cancel();
            mc.profiler.endSection();
        }
    }

    @Inject( method = "updateLightmap", at = @At( value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/DynamicTexture;updateDynamicTexture()V", shift = At.Shift.BEFORE ) )
    private void updateTextureHook(float partialTicks, CallbackInfo ci) {
        Ambience ambience = ( Ambience ) Main.getMain().getModuleManager().get(Ambience.class);
        if (ambience.isToggled()) {
            for (int i = 0; i < this.lightmapColors.length; ++i) {
                Color ambientColor = ambience.color.getColor();
                int alpha = ambientColor.getAlpha();
                float modifier = ( float ) alpha / 255.0f;
                int color = this.lightmapColors[ i ];
                int[] bgr = toRGBAArray(color);
                Vector3f values = new Vector3f(( float ) bgr[ 2 ] / 255.0f, ( float ) bgr[ 1 ] / 255.0f, ( float ) bgr[ 0 ] / 255.0f);
                Vector3f newValues = new Vector3f(( float ) ambientColor.getRed() / 255.0f, ( float ) ambientColor.getGreen() / 255.0f, ( float ) ambientColor.getBlue() / 255.0f);
                Vector3f finalValues = mix(values, newValues, modifier);
                int red = ( int ) (finalValues.x * 255.0f);
                int green = ( int ) (finalValues.y * 255.0f);
                int blue = ( int ) (finalValues.z * 255.0f);
                this.lightmapColors[ i ] = 0xFF000000 | red << 16 | green << 8 | blue;
            }
        }
    }

    private int[] toRGBAArray(int colorBuffer) {
        return new int[] { colorBuffer >> 16 & 0xFF, colorBuffer >> 8 & 0xFF, colorBuffer & 0xFF };
    }

    private Vector3f mix(Vector3f first, Vector3f second, float factor) {
        return new Vector3f(first.x * (1.0f - factor) + second.x * factor, first.y * (1.0f - factor) + second.y * factor, first.z * (1.0f - factor) + first.z * factor);
    }

}