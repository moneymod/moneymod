package wtf.moneymod.client.impl.ui.hud;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.impl.module.global.ClickGui;
import wtf.moneymod.client.impl.ui.click.Screen;
import wtf.moneymod.client.impl.ui.hud.components.*;
import wtf.moneymod.client.impl.utility.impl.render.ColorUtil;
import wtf.moneymod.client.impl.utility.impl.render.Renderer2D;
import wtf.moneymod.client.impl.utility.impl.render.fonts.FontRender;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HudEditorScreen extends GuiScreen
{
    private int x;
    private int y;
    private int width;
    private int height;
    private int dragX;
    private int dragY;
    private int barHeight;
    private boolean isDragging;

    private static List< HudComponent > components = new ArrayList< >( );

    public HudEditorScreen( )
    {
        if( components.isEmpty( ) )
        {
            components = Arrays.asList(
                    new fps( ),
                    new modules( ),
                    new randomshit( )
            );
        }

        this.width = 110;
        this.barHeight = 18;
        this.isDragging = false;
    }

    public void drawScreen( int mouseX, int mouseY, float partialTicks )
    {
        // this shit panel brooo
        int basex = 50;
        int basey = 300;

        for( HudComponent component : components )
        {
            int color = component.isEnabled( ) ? Color.GREEN.getRGB( ) : Color.white.getRGB( );

            FontRender.drawStringWithShadow( component.getName( ), basex, basey, color );

            basey += FontRender.getFontHeight( ) + 5;
        }

        //updatePosition( mouseX, mouseY );
        //renderPanel( mouseX, mouseY );

        for( HudComponent component : components )
        {
            if( !component.isEnabled( ) ) continue;

            component.update( );
            component.render( mouseX, mouseY );
        }
    }

    @Override
    public void mouseClicked( int mouseX, int mouseY, int mouseButton )
    {
        int basex = 50;
        int basey = 300;
        for( HudComponent component : components )
        {
            if( mouseButton == 0 )
            {
                if( mouseX >= basex && mouseX <= basex + component.getSize( )[ 0 ] && mouseY >= basey && mouseY <= basey + component.getSize( )[ 1 ] )
                {
                    component.setEnabled( !component.isEnabled( ) );
                    System.out.println( "rrrrr" );
                    break;
                }
            }

            basey += FontRender.getFontHeight( ) + 5;
        }

        if ( isHover( mouseX, mouseY ) && mouseButton == 0 ) {
            isDragging = true;
            dragX = mouseX - x;
            dragY = mouseY - y;
        }

        for( HudComponent component : components )
        {
            if( !component.isEnabled( ) ) continue;

            component.mouseClicked( mouseX, mouseY, mouseButton );
        }
    }

    @Override
    public void mouseReleased( int mouseX, int mouseY, int mouseButton )
    {
        for( HudComponent component : components )
        {
            if( !component.isEnabled( ) ) continue;

            component.mouseReleased( mouseX, mouseY, mouseButton );
        }
    }

    /*public void renderPanel( int mouseX, int mouseY ) {
        ScaledResolution sr = new ScaledResolution( mc );

        GlStateManager.scale( ClickGui.size, ClickGui.size, 0f );
        ClickGui clickgui = ( ClickGui ) Main.getMain( ).getModuleManager( ).get( ClickGui.class );
        Gui.drawRect( x - 2, y - 2, x + width + 2, y, new Color( 255, 255, 255, 90 ).getRGB( ) );
        Gui.drawRect( x, y, x + width, y + barHeight - 2, new Color( 0, 0, 0, 210 ).getRGB( ) );
        FontRender.drawStringWithShadow( "HUDEditor" + ( clickgui.modulecounter.getValue( ) ? ChatFormatting.WHITE + " (" + components.size( ) + ")" : "" ), x + 3, y + 4, Screen.color.getRGB( ) );
        if ( ( ( ( ClickGui ) Main.getMain( ).getModuleManager( ).get( ClickGui.class ) ).glow.getValue( ) ) ) {
            Renderer2D.drawGlow( x + 4,
                    y + 2,
                    x + FontRender.getStringWidth( "HUDEditor" ) + 7,
                    y + 4 + FontRender.getFontHeight( ),
                    ColorUtil.injectAlpha( Screen.color, 90 ).getRGB( ) );
        }
        FontRender.drawStringWithShadow( "+", x + width - 10, y + 4, Color.GRAY.getRGB( ) );
        if ( !components.isEmpty( ) ) {
            components.forEach( component -> {
                component.render( mouseX, mouseY );
            } );
        }
        int localHeight = height;
        Gui.drawRect( x - 2, y, x, y + localHeight, new Color( 255, 255, 255, 90 ).getRGB( ) );
        Gui.drawRect( x + width, y, x + width + 2, y + localHeight, new Color( 255, 255, 255, 90 ).getRGB( ) );
        Gui.drawRect( x - 2, y + localHeight, x + width + 2, y + localHeight + 2, new Color( 255, 255, 255, 90 ).getRGB( ) );
    }*/

    public boolean isHover( final double x, final double y ) {
        return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.barHeight;
    }

    public void updatePosition( final int mouseX, final int mouseY ) {
        if ( this.isDragging ) {
            x = ( mouseX - this.dragX );
            y = ( mouseY - this.dragY );
        }
    }
}
