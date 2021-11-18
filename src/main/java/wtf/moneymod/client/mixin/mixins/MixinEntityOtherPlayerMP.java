package wtf.moneymod.client.mixin.mixins;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.impl.module.render.NoInterp;

@Mixin( EntityOtherPlayerMP.class )
public class MixinEntityOtherPlayerMP extends AbstractClientPlayer
{
    @Shadow
    private int otherPlayerMPPosRotationIncrements;

    @Shadow
    private double otherPlayerMPX;

    @Shadow
    private double otherPlayerMPY;

    @Shadow
    private double otherPlayerMPZ;

    @Shadow
    private double otherPlayerMPYaw;

    @Shadow
    private double otherPlayerMPPitch;

    public MixinEntityOtherPlayerMP( World worldIn, GameProfile gameProfileIn )
    {
        super( worldIn, gameProfileIn );
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */

    @Overwrite
    public void onLivingUpdate()
    {
        if (this.otherPlayerMPPosRotationIncrements > 0)
        {
            double d0, d1, d2;
            double d3;

            if( Main.getMain().getModuleManager().get(NoInterp.class).isToggled())
            {
                d0 = serverPosX / 4096.0D;
                d1 = serverPosY / 4096.0D;
                d2 = serverPosZ / 4096.0D;
            }
            else
            {
                d0 = this.posX + (this.otherPlayerMPX - this.posX) / otherPlayerMPPosRotationIncrements;
                d1 = this.posY + (this.otherPlayerMPY - this.posY) / otherPlayerMPPosRotationIncrements;
                d2 = this.posZ + (this.otherPlayerMPZ - this.posZ) / otherPlayerMPPosRotationIncrements;
            }

            for (d3 = this.otherPlayerMPYaw - (double)this.rotationYaw; d3 < -180.0D; d3 += 360.0D) {}

            while (d3 >= 180.0D) {d3 -= 360.0D;}

            this.rotationYaw = (float)((double)this.rotationYaw + d3 / (double)this.otherPlayerMPPosRotationIncrements);
            this.rotationPitch = (float)((double)this.rotationPitch + (this.otherPlayerMPPitch - (double)this.rotationPitch) / (double)this.otherPlayerMPPosRotationIncrements);
            --this.otherPlayerMPPosRotationIncrements;
            this.setPosition(d0, d1, d2);
            this.setRotation(this.rotationYaw, this.rotationPitch);
        }

        this.prevCameraYaw = this.cameraYaw;
        this.updateArmSwingProgress();
        float f1 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        float f = (float)Math.atan(-this.motionY * 0.20000000298023224D) * 15.0F;
        if (f1 > 0.1F) {f1 = 0.1F;}

        if (!this.onGround || this.getHealth() <= 0.0F) {f1 = 0.0F;}

        if (this.onGround || this.getHealth() <= 0.0F) {f = 0.0F;}
        this.cameraYaw += (f1 - this.cameraYaw) * 0.4F;
        this.cameraPitch += (f - this.cameraPitch) * 0.8F;
        this.world.profiler.startSection("push");
        this.collideWithNearbyEntities();
        this.world.profiler.endSection();
    }

    @Inject(method = "onUpdate", at = @At ("HEAD"), cancellable = true)
    public void prikol(CallbackInfo ci) {
        if(((NoInterp)Main.getMain().getModuleManager().get(NoInterp.class)).animation) {
            renderOffsetY = 0;
            super.onUpdate();
            limbSwing = 0;
            limbSwingAmount = 0;
            prevLimbSwingAmount = 0;
            ci.cancel();
        }
    }

}