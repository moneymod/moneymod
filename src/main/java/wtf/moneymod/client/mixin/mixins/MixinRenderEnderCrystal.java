package wtf.moneymod.client.mixin.mixins;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderEnderCrystal;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.impl.module.render.Chams;
import wtf.moneymod.client.impl.utility.Globals;

@Mixin( RenderEnderCrystal.class )
public class MixinRenderEnderCrystal implements Globals {
    private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");

    @Redirect(method = "doRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
    public void doRender(ModelBase modelBase, Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        Chams cc = (Chams) Main.getMain().getModuleManager().get(Chams.class);
        if (cc.isToggled() && cc.crystalChams) {

            float red = cc.crystalColor.getColor().getRed() / 255f;
            float green = cc.crystalColor.getColor().getGreen() / 255f;
            float blue = cc.crystalColor.getColor().getBlue() / 255f;
            float alpha = cc.crystalColor.getColor().getAlpha() / 255f;
            float glint_red = cc.crystalColorGlint.getColor().getRed() / 255f;
            float glint_green = cc.crystalColorGlint.getColor().getGreen() / 255f;
            float glint_blue = cc.crystalColorGlint.getColor().getBlue() / 255f;
            float glint_alpha = cc.crystalColorGlint.getColor().getAlpha() / 255f;
            float limb = (cc.cancelX ? 0f : limbSwingAmount);
            float age = (cc.cancelY ? 0f : ageInTicks);
            float scales = (float) cc.crystalScale;

            GlStateManager.scale(scales, scales, scales);

            //lines
            if (cc.crystalLine) {
                GL11.glPushMatrix();
                if (cc.crystalModel)    modelBase.render(entityIn, limbSwing, limb, age, netHeadYaw, headPitch, scale);
                GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
                GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glEnable(GL11.GL_LINE_SMOOTH);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glColor3f(red, green, blue);
                GL11.glLineWidth((float) cc.crystalLineWidht);
                modelBase.render(entityIn, limbSwing, limb, age, netHeadYaw, headPitch, scale);
                GL11.glPopAttrib();
                GL11.glPopMatrix();
            } else {
                if (cc.crystalModel) modelBase.render(entityIn, limbSwing, limb, age, netHeadYaw, headPitch, scale);
            }

            if (cc.crystalChams) {
                GL11.glPushMatrix();
                GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                if (cc.crystalLine) GL11.glLineWidth((float) cc.crystalLineWidht);
                //GL11.glEnable(GL11.GL_STENCIL_TEST);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glDepthMask(false);
                //GL11.glEnable(GL11.GL_POLYGON_OFFSET_LINE);

                GL11.glEnable(GL11.GL_LINE_SMOOTH);
                GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);

                GL11.glColor4f(red, green, blue, alpha);
                modelBase.render(entityIn, limbSwing, limb, age, netHeadYaw, headPitch, scale);

                GL11.glDisable(GL11.GL_LINE_SMOOTH);

                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glDepthMask(true);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glPopAttrib();
                GL11.glPopMatrix();
            }
            if(cc.crystalGlint) {
                GL11.glPushMatrix();
                GL11.glPushAttrib(1048575);
                GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDepthRange( 0, 0.1 );
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glColor4f(glint_red, glint_green, glint_blue, glint_alpha);

                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);

                mc.getTextureManager().bindTexture(RES_ITEM_GLINT);
                modelBase.render(entityIn, limbSwing, limb, age, netHeadYaw, headPitch, scale);

                GL11.glDisable(GL11.GL_BLEND);
                GL11.glDepthRange( 0, 1 );
                GL11.glEnable(GL11.GL_LIGHTING);

                GL11.glPopAttrib();
                GL11.glPopMatrix();
            }

        } else {
            GlStateManager.scale(1, 1, 1);
            modelBase.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }
}