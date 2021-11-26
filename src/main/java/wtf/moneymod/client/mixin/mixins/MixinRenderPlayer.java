package wtf.moneymod.client.mixin.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.events.RenderPlayerRotationsEvent;

@Mixin( RenderPlayer.class )
public class MixinRenderPlayer
{
    private float yaw;
    private float pitch;
    private float yawoffset;
    private float prevyaw;
    private float prevpitch;
    private float prevyawoffset;

    @Inject( method = "doRender", at = @At( "HEAD" ) )
    public void doRenderPre( AbstractClientPlayer player, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo info )
    {
        Minecraft mc = Minecraft.getMinecraft( );

        if( player.equals( ( Object )mc.player ) && mc.gameSettings.thirdPersonView != 0 && !( mc.currentScreen instanceof GuiInventory ) )
        {
            RenderPlayerRotationsEvent event = new RenderPlayerRotationsEvent( mc.player.rotationYawHead, mc.player.rotationPitch );
            Main.EVENT_BUS.dispatch( event );

            yaw = player.rotationYawHead;
            pitch = player.rotationPitch;
            prevyaw = player.prevRotationYawHead;
            prevpitch = player.prevRotationPitch;
            yawoffset = player.renderYawOffset;
            prevyawoffset = player.prevRenderYawOffset;

            if( yaw != event.getYaw( ) || pitch != event.getPitch( ) )
            {
                player.rotationYawHead = event.getYaw( );
                player.prevRotationYawHead = event.getYaw( );
                player.renderYawOffset = event.getYaw( );
                player.prevRenderYawOffset = event.getYaw( );
                player.rotationPitch = event.getPitch( );
                player.prevRotationPitch = event.getPitch( );
            }
        }
    }

    @Inject( method = "doRender", at = @At( "RETURN" ) )
    public void doRenderPost( AbstractClientPlayer player, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo info )
    {
        Minecraft mc = Minecraft.getMinecraft( );

        if( player.equals( ( Object )mc.player ) && mc.gameSettings.thirdPersonView != 0 && !( mc.currentScreen instanceof GuiInventory ) )
        {
            player.rotationYawHead = yaw;
            player.prevRotationYawHead = prevyaw;
            player.renderYawOffset = yawoffset;
            player.prevRenderYawOffset = prevyawoffset;
            player.rotationPitch = pitch;
            player.prevRotationPitch = prevpitch;
        }
    }
}
