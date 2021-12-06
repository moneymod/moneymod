package wtf.moneymod.client.mixin.mixins;

import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityMetadata;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.events.EntityDeathEvent;
import wtf.moneymod.client.impl.utility.Globals;

@Mixin( NetHandlerPlayClient.class )
public class MixinNetHandlerPlayClient implements Globals {

    @Inject( method = "handleEntityMetadata", at = @At( "RETURN" ), cancellable = true )
    private void handleEntityMetadataHook(SPacketEntityMetadata packetIn, CallbackInfo info) {
        Entity entity;
        if (mc.world != null && (entity = mc.world.getEntityByID(packetIn.getEntityId())) instanceof EntityPlayer) {
            EntityPlayer player = ( EntityPlayer ) entity;
            if (player.getHealth() <= 0.0f ) {
                Main.EVENT_BUS.dispatch(new EntityDeathEvent(null, player));
                if (entity == mc.player){
                    Main.getMain().getSessionManagement().addDeath();
                }
            }
        }
    }

}
