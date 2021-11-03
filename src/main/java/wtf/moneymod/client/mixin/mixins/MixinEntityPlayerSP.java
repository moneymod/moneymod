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
import wtf.moneymod.client.api.events.MoveEvent;

@Mixin( value = EntityPlayerSP.class, priority = 9999 )
public class MixinEntityPlayerSP extends AbstractClientPlayer {
    public MixinEntityPlayerSP( World worldIn, GameProfile playerProfile ) {
        super( worldIn, playerProfile );
    }
    @Redirect( method = "move", at = @At( value = "INVOKE", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;move(Lnet/minecraft/entity/MoverType;DDD)V" ) )
    public void move( AbstractClientPlayer player, MoverType type, double x, double y, double z ) {
        MoveEvent event = new MoveEvent( x, y, z );
        MinecraftForge.EVENT_BUS.post( event );
        super.move( type, event.motionX, event.motionY, event.motionZ );
    }
}