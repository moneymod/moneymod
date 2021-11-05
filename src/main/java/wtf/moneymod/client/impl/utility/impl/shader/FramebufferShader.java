package wtf.moneymod.client.impl.utility.impl.shader;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import wtf.moneymod.client.impl.utility.Globals;
import wtf.moneymod.client.mixin.accessors.IEntityRenderer;

import java.awt.*;

public abstract class FramebufferShader extends Shader implements Globals {
    private Framebuffer framebuffer;
    protected float red;
    protected float green;
    protected float blue;
    protected float alpha;
    protected float radius;
    protected float quality;
    protected float saturation;
    protected float speed;
    protected float x, y;
    private boolean entityShadows;

    public FramebufferShader( final String fragmentShader ) {
        super( fragmentShader );
        alpha = 1.0f;
        radius = 2.0f;
        quality = 1.0f;
    }

    public void startDraw( final float partialTicks ) {
        GlStateManager.enableAlpha( );
        GlStateManager.pushMatrix( );
        GlStateManager.pushAttrib( );
        ( framebuffer = setupFrameBuffer( framebuffer ) ).framebufferClear( );
        framebuffer.bindFramebuffer( true );
        entityShadows = mc.gameSettings.entityShadows;
        mc.gameSettings.entityShadows = false;
        ( ( IEntityRenderer ) mc.entityRenderer ).setupCamera( partialTicks, 0 );
    }

    public void stopDraw(final Color color, final float radius, final float quality, float saturation, float speed, float x, float y ) {
        mc.gameSettings.entityShadows = entityShadows;
        framebuffer.unbindFramebuffer( );
        GL11.glEnable( 3042 );
        GL11.glBlendFunc( 770, 771 );
        mc.getFramebuffer( ).bindFramebuffer( true );
        this.saturation = saturation;
        this.speed = speed;
        this.x = x;
        this.y = y;
        red = color.getRed( ) / 255.0f;
        green = color.getGreen( ) / 255.0f;
        blue = color.getBlue( ) / 255.0f;
        alpha = color.getAlpha( ) / 255.0f;
        this.radius = radius;
        this.quality = quality;
        mc.entityRenderer.disableLightmap( );
        RenderHelper.disableStandardItemLighting( );
        startShader( );
        mc.entityRenderer.setupOverlayRendering( );
        drawFramebuffer( framebuffer );
        stopShader( );
        mc.entityRenderer.disableLightmap( );
        GlStateManager.popMatrix( );
        GlStateManager.popAttrib( );
    }

    public Framebuffer setupFrameBuffer( Framebuffer frameBuffer ) {
        if ( frameBuffer == null ) {
            return new Framebuffer( mc.displayWidth, mc.displayHeight, true );
        }
        if ( frameBuffer.framebufferWidth != mc.displayWidth || frameBuffer.framebufferHeight != mc.displayHeight ) {
            frameBuffer.deleteFramebuffer( );
            frameBuffer = new Framebuffer( mc.displayWidth, mc.displayHeight, true );
        }
        return frameBuffer;
    }

    public void drawFramebuffer( final Framebuffer framebuffer ) {
        final ScaledResolution scaledResolution = new ScaledResolution( mc );
        GL11.glBindTexture( 3553, framebuffer.framebufferTexture );
        GL11.glBegin( 7 );
        GL11.glTexCoord2d( 0.0, 1.0 );
        GL11.glVertex2d( 0.0, 0.0 );
        GL11.glTexCoord2d( 0.0, 0.0 );
        GL11.glVertex2d( 0.0, scaledResolution.getScaledHeight( ) );
        GL11.glTexCoord2d( 1.0, 0.0 );
        GL11.glVertex2d( scaledResolution.getScaledWidth( ), scaledResolution.getScaledHeight( ) );
        GL11.glTexCoord2d( 1.0, 1.0 );
        GL11.glVertex2d( scaledResolution.getScaledWidth( ), 0.0 );
        GL11.glEnd( );
        GL20.glUseProgram( 0 );
    }

    public Framebuffer getFramebuffer( ) {return framebuffer;}
}