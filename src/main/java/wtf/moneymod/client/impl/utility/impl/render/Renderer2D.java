package wtf.moneymod.client.impl.utility.impl.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class Renderer2D extends GlStateManager{

    public static void drawRect( float left, float top, float right, float bottom, int color ) {
        float red = ( float ) ( color >> 16 & 0xFF ) / 255.0f;
        float green = ( float ) ( color >> 8 & 0xFF ) / 255.0f;
        float blue = ( float ) ( color & 0xFF ) / 255.0f;
        float alpha = ( float ) ( color >> 24 & 0xFF ) / 255.0f;
        disableTexture2D( );
        enableBlend( );
        disableAlpha( );
        tryBlendFuncSeparate( SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO );
        shadeModel( 7425 );
        GL11.glEnable( 2848 );
        GL11.glHint( 3154, 4354 );
        Tessellator tessellator = Tessellator.getInstance( );
        BufferBuilder bufferbuilder = tessellator.getBuffer( );
        bufferbuilder.begin( 7, DefaultVertexFormats.POSITION_COLOR );
        bufferbuilder.pos( left, top, 0 ).color( red, green, blue, alpha ).endVertex( );
        bufferbuilder.pos( left, bottom, 0 ).color( red, green, blue, alpha ).endVertex( );
        bufferbuilder.pos( right, bottom, 0 ).color( red, green, blue, alpha ).endVertex( );
        bufferbuilder.pos( right, top, 0 ).color( red, green, blue, alpha ).endVertex( );
        tessellator.draw( );
        GL11.glDisable( 2848 );
        shadeModel( 7424 );
        disableBlend( );
        enableAlpha( );
        enableTexture2D( );
    }

    public static void drawRoundedRect( float left, float top, float right, float bottom, int smooth, Color color ) {
        drawRect( left + smooth, top, right - smooth, bottom, color.getRGB( ) );
        drawRect( left, top + smooth, right, bottom - smooth, color.getRGB( ) );
        drawPolygon( ( int ) left + smooth, ( int ) top + smooth, smooth, 360, color.getRGB( ) );
        drawPolygon( ( int ) right - smooth, ( int ) top + smooth, smooth, 360, color.getRGB( ) );
        drawPolygon( ( int ) right - smooth, ( int ) bottom - smooth, smooth, 360, color.getRGB( ) );
        drawPolygon( ( int ) left + smooth, ( int ) bottom - smooth, smooth, 360, color.getRGB( ) );
    }

    public static void drawVGradientRect( float left, float top, float right, float bottom, int startColor, int endColor ) {
        float f = ( float ) ( startColor >> 24 & 255 ) / 255.0F;
        float f1 = ( float ) ( startColor >> 16 & 255 ) / 255.0F;
        float f2 = ( float ) ( startColor >> 8 & 255 ) / 255.0F;
        float f3 = ( float ) ( startColor & 255 ) / 255.0F;
        float f4 = ( float ) ( endColor >> 24 & 255 ) / 255.0F;
        float f5 = ( float ) ( endColor >> 16 & 255 ) / 255.0F;
        float f6 = ( float ) ( endColor >> 8 & 255 ) / 255.0F;
        float f7 = ( float ) ( endColor & 255 ) / 255.0F;
        disableTexture2D( );
        enableBlend( );
        disableAlpha( );
        tryBlendFuncSeparate( SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO );
        shadeModel( 7425 );
        Tessellator tessellator = Tessellator.getInstance( );
        BufferBuilder bufferbuilder = tessellator.getBuffer( );
        bufferbuilder.begin( 7, DefaultVertexFormats.POSITION_COLOR );
        bufferbuilder.pos( right, top, 0 ).color( f1, f2, f3, f ).endVertex( );
        bufferbuilder.pos( left, top, 0 ).color( f1, f2, f3, f ).endVertex( );
        bufferbuilder.pos( left, bottom, 0 ).color( f5, f6, f7, f4 ).endVertex( );
        bufferbuilder.pos( right, bottom, 0 ).color( f5, f6, f7, f4 ).endVertex( );
        tessellator.draw( );
        shadeModel( 7424 );
        disableBlend( );
        enableAlpha( );
        enableTexture2D( );
    }

    public static void drawHGradientRect( float left, float top, float right, float bottom, int startColor, int endColor ) {
        float f = ( float ) ( startColor >> 24 & 255 ) / 255.0F;
        float f1 = ( float ) ( startColor >> 16 & 255 ) / 255.0F;
        float f2 = ( float ) ( startColor >> 8 & 255 ) / 255.0F;
        float f3 = ( float ) ( startColor & 255 ) / 255.0F;
        float f4 = ( float ) ( endColor >> 24 & 255 ) / 255.0F;
        float f5 = ( float ) ( endColor >> 16 & 255 ) / 255.0F;
        float f6 = ( float ) ( endColor >> 8 & 255 ) / 255.0F;
        float f7 = ( float ) ( endColor & 255 ) / 255.0F;
        disableTexture2D( );
        enableBlend( );
        disableAlpha( );
        tryBlendFuncSeparate( SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO );
        shadeModel( 7425 );
        Tessellator tessellator = Tessellator.getInstance( );
        BufferBuilder bufferbuilder = tessellator.getBuffer( );
        bufferbuilder.begin( 7, DefaultVertexFormats.POSITION_COLOR );
        bufferbuilder.pos( left, top, 0 ).color( f1, f2, f3, f4 ).endVertex( );
        bufferbuilder.pos( left, bottom, 0 ).color( f1, f2, f3, f4 ).endVertex( );
        bufferbuilder.pos( right, bottom, 0 ).color( f5, f6, f7, f4 ).endVertex( );
        bufferbuilder.pos( right, top, 0 ).color( f5, f6, f7, f4 ).endVertex( );
        tessellator.draw( );
        shadeModel( 7424 );
        disableBlend( );
        enableAlpha( );
        enableTexture2D( );
    }

    public static void drawOutline( double left, double top, double right, double bottom, float t, int color ) {
        float red = ( float ) ( color >> 16 & 0xFF ) / 255.0f;
        float green = ( float ) ( color >> 8 & 0xFF ) / 255.0f;
        float blue = ( float ) ( color & 0xFF ) / 255.0f;
        float alpha = ( float ) ( color >> 24 & 0xFF ) / 255.0f;
        pushMatrix( );
        disableTexture2D( );
        enableBlend( );
        disableAlpha( );
        tryBlendFuncSeparate( 770, 771, 1, 0 );
        shadeModel( 7425 );
        GL11.glLineWidth( t );
        GL11.glEnable( 2848 );
        GL11.glHint( 3154, 4354 );
        Tessellator tessellator = Tessellator.getInstance( );
        BufferBuilder bufferbuilder = tessellator.getBuffer( );
        bufferbuilder.begin( 2, DefaultVertexFormats.POSITION_COLOR );
        bufferbuilder.pos( right, top, 0 ).color( red, green, blue, alpha ).endVertex( );
        bufferbuilder.pos( left, top, 0 ).color( red, green, blue, alpha ).endVertex( );
        bufferbuilder.pos( left, bottom, 0 ).color( red, green, blue, alpha ).endVertex( );
        bufferbuilder.pos( right, bottom, 0 ).color( red, green, blue, alpha ).endVertex( );
        tessellator.draw( );
        shadeModel( 7424 );
        GL11.glDisable( 2848 );
        disableBlend( );
        enableAlpha( );
        enableTexture2D( );
        popMatrix( );
    }

    public static void drawLine( float x, float y, float x1, float y1, float t, int color ) {
        float red = ( color >> 16 & 0xFF ) / 255.0F;
        float green = ( color >> 8 & 0xFF ) / 255.0F;
        float blue = ( color & 0xFF ) / 255.0F;
        float alpha = ( color >> 24 & 0xFF ) / 255.0F;

        pushMatrix( );
        disableTexture2D( );
        enableBlend( );
        disableAlpha( );
        tryBlendFuncSeparate( 770, 771, 1, 0 );
        shadeModel( GL11.GL_SMOOTH );
        GL11.glLineWidth( t );
        GL11.glEnable( GL11.GL_LINE_SMOOTH );
        GL11.glHint( GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST );
        Tessellator tessellator = Tessellator.getInstance( );
        BufferBuilder bufferbuilder = tessellator.getBuffer( );
        bufferbuilder.begin( GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR );
        bufferbuilder.pos( x, y, 0 ).color( red, green, blue, alpha ).endVertex( );
        bufferbuilder.pos( x1, y1, 0 ).color( red, green, blue, alpha ).endVertex( );
        tessellator.draw( );
        shadeModel( GL11.GL_FLAT );
        GL11.glDisable( GL11.GL_LINE_SMOOTH );
        disableBlend( );
        enableAlpha( );
        enableTexture2D( );
        popMatrix( );
    }

    public static void drawRoundedOutline( float left, float top, float right, float bottom, int smooth, float t, Color color ) {
        drawLine( left + smooth, top, right - smooth, top, t, color.getRGB( ) );
        drawLine( right, top + smooth, right, bottom - smooth, t, color.getRGB( ) );
        drawLine( left + smooth, bottom, right - smooth, bottom, t, color.getRGB( ) );
        drawLine( left, top + smooth, left, bottom - smooth, t, color.getRGB( ) );
        drawPolygonPartOutline( left + smooth, top + smooth, smooth, 0, t, color.getRGB( ) );
        drawPolygonPartOutline( right - smooth, top + smooth, smooth, 3, t, color.getRGB( ) );
        drawPolygonPartOutline( right - smooth,  bottom - smooth, smooth, 2, t, color.getRGB( ) );
        drawPolygonPartOutline(  left + smooth, bottom - smooth, smooth, 1, t, color.getRGB( ) );
    }


    public static void drawPolygonOutline( double x, double y, double lineWidth, int radius, int sides, int color ) {
        float alpha = ( float ) ( color >> 24 & 255 ) / 255.0F;
        float red = ( float ) ( color >> 16 & 255 ) / 255.0F;
        float green = ( float ) ( color >> 8 & 255 ) / 255.0F;
        float blue = ( float ) ( color & 255 ) / 255.0F;
        pushMatrix( );
        disableTexture2D( );
        enableBlend( );
        disableAlpha( );
        tryBlendFuncSeparate( 770, 771, 1, 0 );
        shadeModel( 7425 );
        glLineWidth( ( float ) lineWidth );
        final Tessellator tessellator = Tessellator.getInstance( );
        final BufferBuilder bufferbuilder = tessellator.getBuffer( );
        bufferbuilder.begin( 2, DefaultVertexFormats.POSITION_COLOR );
        final double TWICE_PI = Math.PI * 2;
        for ( int i = 0; i <= sides; i++ ) {
            double angle = ( TWICE_PI * i / sides ) + Math.toRadians( 180 );
            bufferbuilder.pos( x + Math.sin( angle ) * radius, y + Math.cos( angle ) * radius, 0 ).color( red, green, blue, alpha ).endVertex( );
        }
        tessellator.draw( );
        shadeModel( 7424 );
        GL11.glDisable( 2848 );
        disableBlend( );
        enableAlpha( );
        enableTexture2D( );
        popMatrix( );
    }

    public static void drawPolygon( double x, double y, int radius, int sides, int color ) {
        float alpha = ( float ) ( color >> 24 & 255 ) / 255.0F;
        float red = ( float ) ( color >> 16 & 255 ) / 255.0F;
        float green = ( float ) ( color >> 8 & 255 ) / 255.0F;
        float blue = ( float ) ( color & 255 ) / 255.0F;
        GL11.glEnable( GL11.GL_BLEND );
        GL11.glDisable( GL11.GL_TEXTURE_2D );
        GL11.glBlendFunc( GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA );
        final Tessellator tessellator = Tessellator.getInstance( );
        final BufferBuilder bufferbuilder = tessellator.getBuffer( );
        bufferbuilder.begin( GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR );
        bufferbuilder.pos( x, y, 0 ).color( red, green, blue, alpha ).endVertex( );
        final double TWICE_PI = Math.PI * 2;
        for ( int i = 0; i <= sides; i++ ) {
            double angle = ( TWICE_PI * i / sides ) + Math.toRadians( 180 );
            bufferbuilder.pos( x + Math.sin( angle ) * radius, y + Math.cos( angle ) * radius, 0 ).color( red, green, blue, alpha ).endVertex( );
        }
        tessellator.draw( );
        GL11.glEnable( GL11.GL_TEXTURE_2D );
        GL11.glDisable( GL11.GL_BLEND );
    }

    public static void drawPolygonPart( double x, double y, int radius, int part, int color, int endcolor ) {
        float alpha = ( float ) ( color >> 24 & 255 ) / 255.0F;
        float red = ( float ) ( color >> 16 & 255 ) / 255.0F;
        float green = ( float ) ( color >> 8 & 255 ) / 255.0F;
        float blue = ( float ) ( color & 255 ) / 255.0F;
        float alpha1 = ( float ) ( endcolor >> 24 & 255 ) / 255.0F;
        float red1 = ( float ) ( endcolor >> 16 & 255 ) / 255.0F;
        float green1 = ( float ) ( endcolor >> 8 & 255 ) / 255.0F;
        float blue1 = ( float ) ( endcolor & 255 ) / 255.0F;
        disableTexture2D( );
        enableBlend( );
        disableAlpha( );
        tryBlendFuncSeparate( SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO );
        shadeModel( 7425 );
        final Tessellator tessellator = Tessellator.getInstance( );
        final BufferBuilder bufferbuilder = tessellator.getBuffer( );
        bufferbuilder.begin( GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR );
        bufferbuilder.pos( x, y, 0 ).color( red, green, blue, alpha ).endVertex( );
        final double TWICE_PI = Math.PI * 2;
        for ( int i = part * 90; i <= part * 90 + 90; i++ ) {
            double angle = ( TWICE_PI * i / 360 ) + Math.toRadians( 180 );
            bufferbuilder.pos( x + Math.sin( angle ) * radius, y + Math.cos( angle ) * radius, 0 ).color( red1, green1, blue1, alpha1 ).endVertex( );
        }
        tessellator.draw( );
        shadeModel( 7424 );
        disableBlend( );
        enableAlpha( );
        enableTexture2D( );
    }


    public static void drawPolygonPartOutline( double x, double y, int radius, int part, float t, int color ) {
        float alpha = ( float ) ( color >> 24 & 255 ) / 255.0F;
        float red = ( float ) ( color >> 16 & 255 ) / 255.0F;
        float green = ( float ) ( color >> 8 & 255 ) / 255.0F;
        float blue = ( float ) ( color & 255 ) / 255.0F;
        pushMatrix( );
        disableTexture2D( );
        enableBlend( );
        disableAlpha( );
        tryBlendFuncSeparate( 770, 771, 1, 0 );
        shadeModel( 7425 );
        glLineWidth( t );
        final Tessellator tessellator = Tessellator.getInstance( );
        final BufferBuilder bufferbuilder = tessellator.getBuffer( );
        bufferbuilder.begin( GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR );
        final double TWICE_PI = Math.PI * 2;
        for ( int i = part * 90; i <= part * 90 + 90; i++ ) {
            double angle = ( TWICE_PI * i / 360 ) + Math.toRadians( 180 );
            bufferbuilder.pos( x + Math.sin( angle ) * radius, y + Math.cos( angle ) * radius, 0 ).color( red, green, blue, alpha ).endVertex( );
        }
        tessellator.draw( );
        shadeModel( 7424 );
        GL11.glDisable( 2848 );
        disableBlend( );
        enableAlpha( );
        enableTexture2D( );
        popMatrix( );
    }

    public static void drawGlow( double x, double y, double x1, double y1, int color ) {
        disableTexture2D( );
        enableBlend( );
        disableAlpha( );
        tryBlendFuncSeparate( SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO );
        shadeModel( 7425 );
        drawVGradientRect( ( int ) x, ( int ) y, ( int ) x1, ( int ) ( y + ( y1 - y ) / 2f ), ColorUtil.injectAlpha( new Color( color ), 0 ).getRGB( ), color );
        drawVGradientRect( ( int ) x, ( int ) ( y + ( y1 - y ) / 2f ), ( int ) x1, ( int ) y1, color, ColorUtil.injectAlpha( new Color( color ), 0 ).getRGB( ) );
        int radius = ( int ) ( ( y1 - y ) / 2f );
        drawPolygonPart( x, ( y + ( y1 - y ) / 2f ), radius, 0, color, ColorUtil.injectAlpha( new Color( color ), 0 ).getRGB( ) );
        drawPolygonPart( x, ( y + ( y1 - y ) / 2f ), radius, 1, color, ColorUtil.injectAlpha( new Color( color ), 0 ).getRGB( ) );
        drawPolygonPart( x1, ( y + ( y1 - y ) / 2f ), radius, 2, color, ColorUtil.injectAlpha( new Color( color ), 0 ).getRGB( ) );
        drawPolygonPart( x1, ( y + ( y1 - y ) / 2f ), radius, 3, color, ColorUtil.injectAlpha( new Color( color ), 0 ).getRGB( ) );
        shadeModel( 7424 );
        disableBlend( );
        enableAlpha( );
        enableTexture2D( );
    }

    public static void drawTexture(float posX, float posY, int width, int height) {
        GL11.glPushMatrix();
        GL11.glTranslatef(posX, posY, 0.0f);
        GL11.glBegin(7);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex3f(0.0f, 0.0f, 0.0f);
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex3f(0.0f, (float) height, 0.0f);
        GL11.glTexCoord2f(1.0f, 1.0f);
        GL11.glVertex3f((float) width, (float) height, 0.0f);
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex3f((float) width, 0.0f, 0.0f);
        GL11.glEnd();
        GL11.glPopMatrix();
    }

    public static void drawBlockWireframe(BlockPos bp, double height, float width, Color color) {
        drawBlockWireframe(bp, height, width, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public static void drawBlockWireframe(BlockPos bp, double height, float width, int r, int g, int b, int alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GL11.glEnable(2848);
        GL11.glHint(3154,  4354);
        GL11.glLineWidth(width);
        Minecraft mc = Minecraft.getMinecraft();
        double x = (double) bp.getX() - mc.getRenderManager().viewerPosX;
        double y = (double) bp.getY() - mc.getRenderManager().viewerPosY;
        double z = (double) bp.getZ() - mc.getRenderManager().viewerPosZ;
        AxisAlignedBB bb = new AxisAlignedBB(x, y, z, x + 1.0, y + height, z + 1.0);
        net.minecraft.client.renderer.Tessellator tessellator = net.minecraft.client.renderer.Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(1, DefaultVertexFormats.POSITION_COLOR);

        //bottom
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(r, g, b, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex();

        //top
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(r, g, b, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(r, g, b, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(r, g, b, alpha).endVertex();

        //sides
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(r, g, b, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(r, g, b, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, alpha).endVertex();

        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(r, g, b, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(r, g, b, alpha).endVertex();

        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(r, g, b, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, alpha).endVertex();

        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(r, g, b, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(r, g, b, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(r, g, b, alpha).endVertex();

        tessellator.draw();
        GL11.glDisable(2848);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

}
