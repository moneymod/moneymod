package wtf.moneymod.client.impl.utility.impl.render;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;
import wtf.moneymod.client.impl.utility.Globals;
import wtf.moneymod.client.mixin.mixins.ducks.AccessorRenderManager;

import java.awt.*;
import java.util.Objects;

public enum  Renderer3D implements Globals {
    INSTANCE;

    private static ICamera camera = new Frustum();
    public static final AccessorRenderManager rendermgr = ( AccessorRenderManager )mc.getRenderManager( );

    public static void prepare() {
        GlStateManager.pushMatrix();
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        GlStateManager.disableAlpha();
        GlStateManager.disableCull();
        GlStateManager.enableBlend();
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glBlendFunc(770, 771);
    }

    public static AxisAlignedBB fixBB(final AxisAlignedBB bb) {
        return new AxisAlignedBB(bb.minX - mc.getRenderManager().viewerPosX, bb.minY - mc.getRenderManager().viewerPosY, bb.minZ - mc.getRenderManager().viewerPosZ, bb.maxX - mc.getRenderManager().viewerPosX, bb.maxY - mc.getRenderManager().viewerPosY, bb.maxZ - mc.getRenderManager().viewerPosZ);
    }

    public static void release() {
        GlStateManager.depthMask((boolean)true);
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.popMatrix();
        GL11.glEnable(3553);
        GL11.glPolygonMode(1032, 6914);
    }

    public static void drawBlockOutline(final AxisAlignedBB bb, final Color color, final float linewidth) {
        final float red = color.getRed() / Float.intBitsToFloat(Float.floatToIntBits(0.010800879f) ^ 0x7F4FF62C);
        final float green = color.getGreen() / Float.intBitsToFloat(Float.floatToIntBits(0.013595752f) ^ 0x7F21C0B8);
        final float blue = color.getBlue() / Float.intBitsToFloat(Float.floatToIntBits(0.014829914f) ^ 0x7F0DF92B);
        final float alpha = Float.intBitsToFloat(Float.floatToIntBits(5.635761f) ^ 0x7F345827);
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glLineWidth(linewidth);
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GL11.glDisable(2848);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }


    public static void drawBlockOutline(final BlockPos pos, final Color color, final float linewidth, final boolean air, final double height) {
        final IBlockState iblockstate = mc.world.getBlockState(pos);
        if (!air) {
            if (iblockstate.getMaterial() == Material.AIR) {
                return;
            }
        }
        if (mc.world.getWorldBorder().contains(pos)) {
            final AxisAlignedBB blockAxis = new AxisAlignedBB(pos.getX() - mc.getRenderManager().viewerPosX, pos.getY() - mc.getRenderManager().viewerPosY, pos.getZ() - mc.getRenderManager().viewerPosZ, pos.getX() + 1 - mc.getRenderManager().viewerPosX, pos.getY() + 1 - mc.getRenderManager().viewerPosY + height, pos.getZ() + 1 - mc.getRenderManager().viewerPosZ);
            drawBlockOutline(blockAxis.grow(Double.longBitsToDouble(Double.doubleToLongBits(3177.4888695024906) ^ 0x7FC8B0B7AD1A7A6BL)), color, linewidth);
        }
    }

    public static void drawBoxESP(final BlockPos pos, final Color color, final float lineWidth, final boolean outline, final boolean box, final int boxAlpha, final int outlineAlpha, final float height) {
        final AxisAlignedBB bb = new AxisAlignedBB(pos.getX() - mc.getRenderManager().viewerPosX, pos.getY() - mc.getRenderManager().viewerPosY, pos.getZ() - mc.getRenderManager().viewerPosZ, pos.getX() + 1 - mc.getRenderManager().viewerPosX, pos.getY() + height - mc.getRenderManager().viewerPosY, pos.getZ() + 1 - mc.getRenderManager().viewerPosZ);
        camera.setPosition(Objects.requireNonNull(mc.getRenderViewEntity()).posX, mc.getRenderViewEntity().posY, mc.getRenderViewEntity().posZ);
        if (camera.isBoundingBoxInFrustum(new AxisAlignedBB(pos))) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);
            GL11.glEnable(2848);
            GL11.glHint(3154, 4354);
            GL11.glLineWidth(lineWidth);
            if (box) {
                RenderGlobal.renderFilledBox(bb, color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, boxAlpha / 255.0f);
            }
            if (outline) {
                RenderGlobal.drawBoundingBox(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ, color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, outlineAlpha / 255.0f);
            }
            GL11.glDisable(2848);
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    public static void drawBoxESP(final AxisAlignedBB pos, final Color color, final float lineWidth, final boolean outline, final boolean box, final int boxAlpha, final int outlineAlpha) {
        final AxisAlignedBB bb = new AxisAlignedBB(pos.minX - mc.getRenderManager().viewerPosX, pos.minY - mc.getRenderManager().viewerPosY, pos.minZ - mc.getRenderManager().viewerPosZ, pos.maxX - mc.getRenderManager().viewerPosX, pos.maxY - mc.getRenderManager().viewerPosY, pos.maxZ - mc.getRenderManager().viewerPosZ);
        camera.setPosition(Objects.requireNonNull(mc.getRenderViewEntity()).posX, mc.getRenderViewEntity().posY, mc.getRenderViewEntity().posZ);
        if (camera.isBoundingBoxInFrustum(pos)) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);
            GL11.glEnable(2848);
            GL11.glHint(3154, 4354);
            GL11.glLineWidth(lineWidth);
            if (box) {
                RenderGlobal.renderFilledBox(bb, color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, boxAlpha / 255.0f);
            }
            if (outline) {
                RenderGlobal.drawBoundingBox(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ, color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, outlineAlpha / 255.0f);
            }
            GL11.glDisable(2848);
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    public static void drawFilledBox(final AxisAlignedBB bb, final int color) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        final float alpha = (color >> 24 & 0xFF) / Float.intBitsToFloat(Float.floatToIntBits(0.0121679185f) ^ 0x7F385BF3);
        final float red = (color >> 16 & 0xFF) / Float.intBitsToFloat(Float.floatToIntBits(0.009070697f) ^ 0x7F6B9D43);
        final float green = (color >> 8 & 0xFF) / Float.intBitsToFloat(Float.floatToIntBits(0.013924689f) ^ 0x7F1B2461);
        final float blue = (color & 0xFF) / Float.intBitsToFloat(Float.floatToIntBits(0.067761265f) ^ 0x7EF5C66B);
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public void drawProgressBox(AxisAlignedBB pos, float progress, Color color) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);

        float nxOff = ( float ) (pos.minX + (pos.getCenter().x - pos.minX) * progress);
        float nyOff = ( float ) (pos.minY + (pos.getCenter().y - pos.minY) * progress);
        float nzOff = ( float ) (pos.minZ + (pos.getCenter().z - pos.minZ) * progress);
        float mxOff = ( float ) (pos.maxX + (pos.getCenter().x - pos.maxX) * progress);
        float myOff = ( float ) (pos.maxY + (pos.getCenter().y - pos.maxY) * progress);
        float mzOff = ( float ) (pos.maxZ + (pos.getCenter().z - pos.maxZ) * progress);

        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(nxOff, nyOff, nzOff, mxOff, myOff, mzOff);

        drawBoxESP(axisAlignedBB, color, 1f, true, true, color.getAlpha(), 255);

        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public void drawProgressBox(AxisAlignedBB pos, float progress, Color color, boolean outline,float linewidht, boolean box, int alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);

        float nxOff = ( float ) (pos.minX + (pos.getCenter().x - pos.minX) * progress);
        float nyOff = ( float ) (pos.minY + (pos.getCenter().y - pos.minY) * progress);
        float nzOff = ( float ) (pos.minZ + (pos.getCenter().z - pos.minZ) * progress);
        float mxOff = ( float ) (pos.maxX + (pos.getCenter().x - pos.maxX) * progress);
        float myOff = ( float ) (pos.maxY + (pos.getCenter().y - pos.maxY) * progress);
        float mzOff = ( float ) (pos.maxZ + (pos.getCenter().z - pos.maxZ) * progress);

        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(nxOff, nyOff, nzOff, mxOff, myOff, mzOff);

        drawBoxESP(axisAlignedBB, color, linewidht, outline, box, color.getAlpha(), alpha);

        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    // насрал блядь

    // THIS IS MY CODE
    public static void doRandomShitWithGL( boolean state )
    {
        if( state )
        {
            GlStateManager.pushMatrix( );
            GlStateManager.disableLighting( );
            GlStateManager.enableBlend( );
            GL11.glEnable( 2848 );
            GlStateManager.disableDepth( );
            GlStateManager.disableTexture2D( );
            GlStateManager.enableAlpha( );
            GlStateManager.tryBlendFuncSeparate( 770, 771, 1, 0 );
            GL11.glHint( 3154, 4354 );
        }
        else
        {
            GlStateManager.enableLighting( );
            GlStateManager.disableBlend( );
            GlStateManager.enableTexture2D( );
            GL11.glDisable( 2848 );
            GlStateManager.enableDepth( );
            GlStateManager.popMatrix( );
            GlStateManager.color( 1.0f, 1.0f, 1.0f, 1.0f );
        }

        // good code 10/10
        GlStateManager.depthMask( ( !state ? 1 : 0 ) != 0 );
    }

    // THIS IS MY CODE
    public static float[ ] getColor( Object[ ] var0 )
    {
        int var1 = ( Integer )var0[ 0 ];
        boolean var10003 = true;
        int var10000 = var1 >> 16;
        var10003 = true;
        float var2 = ( var10000 & 255 ) / 255.0F;
        var10003 = true;
        var10000 = var1 >> 8;
        var10003 = true;
        float var3 = ( var10000 & 255 ) / 255.0F;
        var10003 = true;
        float var4 = ( var1 & 255 ) / 255.0F;
        var10003 = true;
        var10000 = var1 >> 24;
        var10003 = true;
        float var5 = ( var10000 & 255 ) / 255.0F;
        boolean var10002 = true;
        float[] var6 = new float[ 4 ];
        var10002 = true;
        boolean var10004 = true;
        var6[0] = var2;
        var10004 = true;
        var6[1] = var3;
        var10004 = true;
        var6[2] = var4;
        var10004 = true;
        var6[3] = var5;
        return var6;
    }

    // THIS IS MY CODE
    public static void thisIsMyCode1(Object[] objectArray)
    {
        AxisAlignedBB axisAlignedBB = (AxisAlignedBB)objectArray[0];
        int n = (Integer)objectArray[1];
        float[] fArray = getColor(new Object[]{n});
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        BufferBuilder bufferBuilder2 = bufferBuilder;
        AxisAlignedBB axisAlignedBB2 = axisAlignedBB;
        BufferBuilder bufferBuilder3 = bufferBuilder;
        AxisAlignedBB axisAlignedBB3 = axisAlignedBB;
        BufferBuilder bufferBuilder4 = bufferBuilder;
        AxisAlignedBB axisAlignedBB4 = axisAlignedBB;
        BufferBuilder bufferBuilder5 = bufferBuilder;
        AxisAlignedBB axisAlignedBB5 = axisAlignedBB;
        BufferBuilder bufferBuilder6 = bufferBuilder;
        AxisAlignedBB axisAlignedBB6 = axisAlignedBB;
        BufferBuilder bufferBuilder7 = bufferBuilder;
        AxisAlignedBB axisAlignedBB7 = axisAlignedBB;
        BufferBuilder bufferBuilder8 = bufferBuilder;
        AxisAlignedBB axisAlignedBB8 = axisAlignedBB;
        BufferBuilder bufferBuilder9 = bufferBuilder;
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferBuilder9.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(fArray[0], fArray[1], fArray[2], fArray[3]).endVertex();
        bufferBuilder9.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(fArray[0], fArray[1], fArray[2], fArray[3]).endVertex();
        bufferBuilder.pos(axisAlignedBB8.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(fArray[0], fArray[1], fArray[2], fArray[3]).endVertex();
        bufferBuilder8.pos(axisAlignedBB8.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(fArray[0], fArray[1], fArray[2], fArray[3]).endVertex();
        bufferBuilder8.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(fArray[0], fArray[1], fArray[2], fArray[3]).endVertex();
        bufferBuilder.pos(axisAlignedBB7.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(fArray[0], fArray[1], fArray[2], fArray[3]).endVertex();
        bufferBuilder7.pos(axisAlignedBB7.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(fArray[0], fArray[1], fArray[2], fArray[3]).endVertex();
        bufferBuilder7.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(fArray[0], fArray[1], fArray[2], fArray[3]).endVertex();
        bufferBuilder.pos(axisAlignedBB6.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(fArray[0], fArray[1], fArray[2], fArray[3]).endVertex();
        bufferBuilder6.pos(axisAlignedBB6.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(fArray[0], fArray[1], fArray[2], fArray[3]).endVertex();
        bufferBuilder6.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(fArray[0], fArray[1], fArray[2], fArray[3]).endVertex();
        bufferBuilder.pos(axisAlignedBB5.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(fArray[0], fArray[1], fArray[2], fArray[3]).endVertex();
        bufferBuilder5.pos(axisAlignedBB5.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(fArray[0], fArray[1], fArray[2], fArray[3]).endVertex();
        bufferBuilder5.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(fArray[0], fArray[1], fArray[2], fArray[3]).endVertex();
        bufferBuilder.pos(axisAlignedBB4.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(fArray[0], fArray[1], fArray[2], fArray[3]).endVertex();
        bufferBuilder4.pos(axisAlignedBB4.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(fArray[0], fArray[1], fArray[2], fArray[3]).endVertex();
        bufferBuilder4.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(fArray[0], fArray[1], fArray[2], fArray[3]).endVertex();
        bufferBuilder.pos(axisAlignedBB3.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(fArray[0], fArray[1], fArray[2], fArray[3]).endVertex();
        bufferBuilder3.pos(axisAlignedBB3.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(fArray[0], fArray[1], fArray[2], fArray[3]).endVertex();
        bufferBuilder3.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(fArray[0], fArray[1], fArray[2], fArray[3]).endVertex();
        bufferBuilder.pos(axisAlignedBB2.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(fArray[0], fArray[1], fArray[2], fArray[3]).endVertex();
        bufferBuilder2.pos(axisAlignedBB2.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(fArray[0], fArray[1], fArray[2], fArray[3]).endVertex();
        bufferBuilder2.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(fArray[0], fArray[1], fArray[2], fArray[3]).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(fArray[0], fArray[1], fArray[2], fArray[3]).endVertex();
        tessellator.draw();
    }

    // THIS IS MY CODE
    public static void thisIsMyCode2(Object[] objectArray) {
        AxisAlignedBB axisAlignedBB = (AxisAlignedBB)objectArray[0];
        float f = ((Float)objectArray[1]).floatValue();
        int n = (Integer)objectArray[2];
        float[] fArray = getColor(new Object[]{n});
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        if (f < 1.0f) {
            f = 1.0f;
        }
        GL11.glLineWidth((float)f);
        BufferBuilder bufferBuilder2 = bufferBuilder;
        AxisAlignedBB axisAlignedBB2 = axisAlignedBB;
        BufferBuilder bufferBuilder3 = bufferBuilder;
        AxisAlignedBB axisAlignedBB3 = axisAlignedBB;
        BufferBuilder bufferBuilder4 = bufferBuilder;
        AxisAlignedBB axisAlignedBB4 = axisAlignedBB;
        BufferBuilder bufferBuilder5 = bufferBuilder;
        AxisAlignedBB axisAlignedBB5 = axisAlignedBB;
        BufferBuilder bufferBuilder6 = bufferBuilder;
        AxisAlignedBB axisAlignedBB6 = axisAlignedBB;
        BufferBuilder bufferBuilder7 = bufferBuilder;
        AxisAlignedBB axisAlignedBB7 = axisAlignedBB;
        bufferBuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferBuilder.pos(axisAlignedBB7.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(fArray[0], fArray[1], fArray[2], 0.0f).endVertex();
        bufferBuilder7.pos(axisAlignedBB7.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(fArray[0], fArray[1], fArray[2], 1.0f).endVertex();
        bufferBuilder7.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(fArray[0], fArray[1], fArray[2], 1.0f).endVertex();
        bufferBuilder.pos(axisAlignedBB6.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(fArray[0], fArray[1], fArray[2], 1.0f).endVertex();
        bufferBuilder6.pos(axisAlignedBB6.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(fArray[0], fArray[1], fArray[2], 1.0f).endVertex();
        bufferBuilder6.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(fArray[0], fArray[1], fArray[2], 1.0f).endVertex();
        bufferBuilder.pos(axisAlignedBB5.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(fArray[0], fArray[1], fArray[2], 1.0f).endVertex();
        bufferBuilder5.pos(axisAlignedBB5.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(fArray[0], fArray[1], fArray[2], 1.0f).endVertex();
        bufferBuilder5.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(fArray[0], fArray[1], fArray[2], 1.0f).endVertex();
        bufferBuilder.pos(axisAlignedBB4.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(fArray[0], fArray[1], fArray[2], 1.0f).endVertex();
        bufferBuilder4.pos(axisAlignedBB4.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(fArray[0], fArray[1], fArray[2], 1.0f).endVertex();
        bufferBuilder4.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(fArray[0], fArray[1], fArray[2], 0.0f).endVertex();
        bufferBuilder.pos(axisAlignedBB3.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(fArray[0], fArray[1], fArray[2], 1.0f).endVertex();
        bufferBuilder3.pos(axisAlignedBB3.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(fArray[0], fArray[1], fArray[2], 0.0f).endVertex();
        bufferBuilder3.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(fArray[0], fArray[1], fArray[2], 1.0f).endVertex();
        bufferBuilder.pos(axisAlignedBB2.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(fArray[0], fArray[1], fArray[2], 0.0f).endVertex();
        bufferBuilder2.pos(axisAlignedBB2.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(fArray[0], fArray[1], fArray[2], 1.0f).endVertex();
        bufferBuilder2.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(fArray[0], fArray[1], fArray[2], 0.0f).endVertex();
        tessellator.draw();
    }

    public static void rhm(Object[] objectArray) {
        double d = (Double)objectArray[0];
        double d2 = (Double)objectArray[1];
        double d3 = (Double)objectArray[2];
        double d4 = (Double)objectArray[3];
        double d5 = (Double)objectArray[4];
        double d6 = (Double)objectArray[5];
        boolean bl = (Boolean)objectArray[6];
        boolean bl2 = (Boolean)objectArray[7];
        float f = ((Float)objectArray[8]).floatValue();
        Color color = (Color)objectArray[9];
        d -= rendermgr.getRenderPosX();
        d2 -= rendermgr.getRenderPosY();
        d3 -= rendermgr.getRenderPosZ();
        d4 -= rendermgr.getRenderPosX();
        d5 -= rendermgr.getRenderPosY();
        d6 -= rendermgr.getRenderPosZ();
        doRandomShitWithGL( true );
        if (bl) {
            Object[] objectArray2 = new Object[2];
            objectArray2[1] = color.getRGB();
            objectArray2[0] = new AxisAlignedBB(d, d2, d3, d4, d5, d6);
            thisIsMyCode1(objectArray2);
        }
        if (bl2) {
            Object[] objectArray3 = new Object[3];
            objectArray3[2] = color.getRGB();
            objectArray3[1] = Float.valueOf(f);
            objectArray3[0] = new AxisAlignedBB(d, d2, d3, d4, d5, d6);
            thisIsMyCode2(objectArray3);
        }
        doRandomShitWithGL( false );
    }

    public static void rhK(BlockPos blockPos, boolean bl, boolean bl2, Color color) {
        BlockPos blockPos2 = blockPos;
        double d = blockPos2.getX();
        double d2 = blockPos2.getY();
        double d3 = blockPos2.getZ();
        Object[] objectArray2 = new Object[10];
        objectArray2[9] = color;
        objectArray2[8] = Float.valueOf(2.0f);
        objectArray2[7] = bl2;
        objectArray2[6] = bl;
        objectArray2[5] = d3 + 1.0;
        objectArray2[4] = d2 + 1.0;
        objectArray2[3] = d + 1.0;
        objectArray2[2] = d3;
        objectArray2[1] = d2;
        objectArray2[0] = d;
        rhm(objectArray2);
    }
}