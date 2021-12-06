package wtf.moneymod.client.mixin.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.events.StepEvent;
import wtf.moneymod.client.mixin.accessors.IEntity;
import wtf.moneymod.eventhandler.event.enums.Era;

@Mixin( Entity.class )
public class MixinEntity implements IEntity
{
    @Shadow
    public boolean isInWeb;

    public boolean isInWeb( )
    {
        return this.isInWeb;
    }

    public void setInWeb( boolean state ) { this.isInWeb = state; }

    @Inject( method = "move",
            at = @At( value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getEntityBoundingBox()Lnet/minecraft/util/math/AxisAlignedBB;", ordinal = 12, shift = At.Shift.BEFORE ))
    public void onPreStep(MoverType type, double x, double y, double z, CallbackInfo info )
    {
        if( Minecraft.getMinecraft( ).player != null && Minecraft.getMinecraft( ).player.equals( Entity.class.cast( this ) ) )
        {
            StepEvent event = new StepEvent();
            event.setEra(Era.PRE);
            Main.EVENT_BUS.dispatch( event );
        }
    }

    @Inject( method = "move",
            at = @At( value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setEntityBoundingBox(Lnet/minecraft/util/math/AxisAlignedBB;)V", ordinal = 7, shift = At.Shift.AFTER ))
    public void onPostStep( MoverType type, double x, double y, double z, CallbackInfo info )
    {
        if( Minecraft.getMinecraft( ).player != null && Minecraft.getMinecraft( ).player.equals( Entity.class.cast( this ) ) )
        {
            StepEvent event = new StepEvent();
            event.setEra(Era.POST);
            Main.EVENT_BUS.dispatch( event );
        }
    }

}