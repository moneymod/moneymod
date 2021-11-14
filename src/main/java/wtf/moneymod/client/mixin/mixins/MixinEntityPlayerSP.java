package wtf.moneymod.client.mixin.mixins;


import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.MoverType;
import net.minecraft.util.MovementInput;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.events.MoveEvent;
import wtf.moneymod.client.api.events.UpdateWalkingPlayerEvent;
import wtf.moneymod.client.impl.module.movement.ElytraFly;

@Mixin( value = EntityPlayerSP.class, priority = 9999 )
public class MixinEntityPlayerSP extends AbstractClientPlayer {

    public MixinEntityPlayerSP(World worldIn, GameProfile playerProfile) {
        super(worldIn, playerProfile);
    }

    @Redirect( method = "move", at = @At( value = "INVOKE", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;move(Lnet/minecraft/entity/MoverType;DDD)V" ) )
    public void move(AbstractClientPlayer player, MoverType type, double x, double y, double z) {
        MoveEvent event = new MoveEvent(x, y, z);
        Main.EVENT_BUS.dispatch(event);
        super.move(type, event.motionX, event.motionY, event.motionZ);
    }

    @Inject( method = "onUpdateWalkingPlayer", at = @At( "HEAD" ) )
    public void pre( CallbackInfo info ) {
        UpdateWalkingPlayerEvent event = new UpdateWalkingPlayerEvent( 0 );
        Main.EVENT_BUS.dispatch( event );
    }

    @Inject( method = "onUpdateWalkingPlayer", at = @At( "RETURN" ) )
    public void post( CallbackInfo info ) {
        UpdateWalkingPlayerEvent event = new UpdateWalkingPlayerEvent( 1 );
        Main.EVENT_BUS.dispatch( event );
    }

    @Override public boolean isElytraFlying() {
        if(Main.getMain().getModuleManager().get(ElytraFly.class).isToggled()) return false;
        return super.isElytraFlying();
    }

}