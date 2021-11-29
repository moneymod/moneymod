package wtf.moneymod.client.mixin.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.settings.GameSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.management.impl.ConfigManagement;
import wtf.moneymod.client.impl.module.player.MultiTask;
import wtf.moneymod.client.mixin.mixins.ducks.AccessorEntityPlayerSP;
import wtf.moneymod.client.mixin.mixins.ducks.AccessorPlayerControllerMP;

@Mixin( Minecraft.class )
public abstract class MixinMinecraft
{
    @Shadow
    public GameSettings gameSettings;

    @Shadow
    public EntityPlayerSP player;

    @Shadow
    public PlayerControllerMP playerController;

    @Shadow
    protected abstract void clickMouse( );

    private boolean mt_handActive = false;
    private boolean mt_isHittingBlock = false;

    @Inject( method = "shutdown()V", at = @At( "HEAD" ) )
    public void shutdown(CallbackInfo callbackInfo) {
        ConfigManagement.getInstance().start();
    }

    // pasted from lambda who cares
    @Inject( method = "processKeyBinds", at = @At( value = "INVOKE", target = "Lnet/minecraft/client/settings/KeyBinding;isKeyDown()Z", shift = At.Shift.BEFORE, ordinal = 2 ) )
    public void mt_processKeyBinds( CallbackInfo info )
    {
        if( Main.getMain( ).getModuleManager( ).get( MultiTask.class ).isToggled( ) )
        {
            while( gameSettings.keyBindAttack.isPressed( ) )
                clickMouse( );
        }
    }

    @Inject( method = "rightClickMouse", at = @At( "HEAD" ) )
    public void mt_rightClickMouse( CallbackInfo info )
    {
        if( Main.getMain( ).getModuleManager( ).get( MultiTask.class ).isToggled( ) )
        {
            mt_isHittingBlock = playerController.getIsHittingBlock();
            ((AccessorPlayerControllerMP) playerController).mm_setIsHittingBlock(false);
        }
    }

    @Inject(method = "rightClickMouse", at = @At("RETURN"))
    public void mt_rightClickMousePost(CallbackInfo ci) {
        if (Main.getMain( ).getModuleManager( ).get( MultiTask.class ).isToggled( ) && !playerController.getIsHittingBlock()) {
            ((AccessorPlayerControllerMP) playerController).mm_setIsHittingBlock(mt_isHittingBlock);
        }
    }

    @Inject(method = "sendClickBlockToController", at = @At("HEAD"))
    public void mt_sendClickBlockToControllerPre(boolean leftClick, CallbackInfo ci) {
        if (Main.getMain( ).getModuleManager( ).get( MultiTask.class ).isToggled( )) {
            mt_handActive = player.isHandActive();
            ((AccessorEntityPlayerSP) player).mm_setHandActive(false);
        }
    }

    @Inject(method = "sendClickBlockToController", at = @At("RETURN"))
    public void mt_sendClickBlockToControllerPost(boolean leftClick, CallbackInfo ci) {
        if (Main.getMain( ).getModuleManager( ).get( MultiTask.class ).isToggled( ) && !player.isHandActive()) {
            ((AccessorEntityPlayerSP) player).mm_setHandActive(mt_handActive);
        }
    }
}
