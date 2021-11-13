package wtf.moneymod.client.impl.ui.irc;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.impl.utility.impl.render.Renderer2D;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class IrcScreen extends GuiScreen {

    private final ArrayList<TextComponentString> messages = new ArrayList<>( );
    private GuiTextField message;
    private GuiButton back;

    public ArrayList<TextComponentString> getMessages( ) {
        return messages;
    }

    @Override public void initGui( ) {
        message = new GuiTextField( 1488, this.fontRenderer, 5, this.height - 26, width - 10, 20 );
        buttonList.add( back = new GuiButton( 13371, 2, 2, 98, 20, "Back" ) );
        message.setMaxStringLength( 256 );
    }

    @Override protected void actionPerformed( GuiButton button ) {
        if ( button.id == 13371 ) {
            mc.displayGuiScreen( null );
        }
    }

    @Override public void drawScreen( int mouseX, int mouseY, float partialTicks ) {
        if ( mc.world == null ) drawDefaultBackground( );
        else Renderer2D.drawRect( 0, 0, width, height, new Color( 0, 0, 0, 185 ).getRGB( ) );
        super.drawScreen( mouseX, mouseY, partialTicks );
        message.drawTextBox( );
        drawString( fontRenderer, String.format( "Online %s (%s)", Main.getMain().getCapeThread( ).getUserCount( ), Main.getMain().getCapeThread( ).getOnline( ) ), 102, 8, -1 );
        int y = this.height - 50;
        GL11.glPushMatrix( );
        glScissor( 0, 22, width, height );
        GL11.glEnable( GL11.GL_SCISSOR_TEST );
        try {
            for ( TextComponentString component : messages ) {
                drawString( fontRenderer, component.getText( ), 10, y, -1 );
                y -= fontRenderer.FONT_HEIGHT;
            }
        } catch ( Exception ignored ) {}
        GL11.glDisable( GL11.GL_SCISSOR_TEST );
        GL11.glPopMatrix( );
    }

    @Override protected void keyTyped( char typedChar, int keyCode ) {
        try {
            super.keyTyped( typedChar, keyCode );
        } catch ( Exception e ) {
            e.printStackTrace( );
        }
        if ( keyCode == Keyboard.KEY_RETURN && !message.getText( ).isEmpty( ) ) {
            Main.getMain().getCapeThread( ).sendChatMessage( message.getText( ) );
            message.setText( "" );
        }

        message.textboxKeyTyped( typedChar, keyCode );
        if ( keyCode == Keyboard.KEY_ESCAPE ) {
            mc.displayGuiScreen( null );

            if ( mc.currentScreen == null ) {
                mc.setIngameFocus( );
            }
        }
    }

    @Override protected void mouseClicked( int mouseX, int mouseY, int mouseButton ) throws IOException {
        super.mouseClicked( mouseX, mouseY, mouseButton );
        message.mouseClicked( mouseX, mouseY, mouseButton );
    }

    public void glScissor( int x, int y, int width, int height ) {
        Minecraft mc = Minecraft.getMinecraft( );
        ScaledResolution resolution = new ScaledResolution( mc );
        int scale = resolution.getScaleFactor( );

        int scissorWidth = width * scale;
        int scissorHeight = height * scale;
        int scissorX = x * scale;
        int scissorY = mc.displayHeight - scissorHeight - ( y * scale );

        GL11.glScissor( scissorX, scissorY, scissorWidth, scissorHeight );
    }

    @Override public void onGuiClosed( ) {
        if ( mc.entityRenderer.isShaderActive( ) ) mc.entityRenderer.stopUseShader( );
        if ( this.mc.entityRenderer.getShaderGroup( ) != null ) {
            this.mc.entityRenderer.getShaderGroup( ).deleteShaderGroup( );
        }
    }

}