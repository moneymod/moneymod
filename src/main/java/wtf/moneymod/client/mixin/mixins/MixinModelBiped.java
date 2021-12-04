package wtf.moneymod.client.mixin.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.moneymod.client.api.management.impl.RotationHandler;

@Mixin( ModelBiped.class )
public class MixinModelBiped {

    @Shadow public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {}

//    @Inject( method = "setRotationAngles", at = @At( value = "FIELD", target = "Lnet/minecraft/client/model/ModelBiped;swingProgress:F", ordinal = 0) )
//    private void setAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn, CallbackInfo ci) {
//        if (RotationHandler.getInstance().isSet() && entityIn == Minecraft.getMinecraft().player) {
//            float yaw = ( float ) (RotationHandler.getInstance().getRotation().getYaw() / (180 / Math.PI)),
//                    pitch = ( float ) (RotationHandler.getInstance().getRotation().getPitch() / (180 / Math.PI));
//            setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, yaw, pitch, scaleFactor, entityIn);
//            RotationHandler.getInstance().reset();
//        } else {
//            setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
//        }
//    }

    @Redirect( method = "render", at = @At( value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBiped;setRotationAngles(FFFFFFLnet/minecraft/entity/Entity;)V", ordinal = 0) )
    private void render(ModelBiped modelBiped, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        if (RotationHandler.getInstance().isSet() && entityIn == Minecraft.getMinecraft().player) {
            float yaw = ( float ) (RotationHandler.getInstance().getRotation().getYaw() / (180 / Math.PI)),
                    pitch = ( float ) (RotationHandler.getInstance().getRotation().getPitch() / (180 / Math.PI));
            modelBiped.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, yaw, pitch, scaleFactor, entityIn);
            RotationHandler.getInstance().reset();
        } else {
            modelBiped.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        }
    }

}
