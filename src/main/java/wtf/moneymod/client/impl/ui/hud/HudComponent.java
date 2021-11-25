package wtf.moneymod.client.impl.ui.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import wtf.moneymod.client.impl.utility.impl.render.fonts.FontRender;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HudComponent
{
    private static int globalorder;

    private int order;
    private boolean enabled;
    private int x;
    private int y;
    private int[ ] size;
    private String name;
    private String text;
    private int color;
    private List< String > drawtext;

    public Minecraft mc = Minecraft.getMinecraft( );

    // mouse
    private boolean dragging;
    private int dragX;
    private int dragY;

    public HudComponent( String name )
    {
        globalorder++;
        this.order = globalorder;
        this.name = name;
        this.size = new int[ 2 ];
        this.x = -1337;
        this.color = Color.WHITE.getRGB( );
        this.enabled = true;
        this.drawtext = new ArrayList( );
    }

    public boolean isEnabled( )
    {
        return enabled;
    }

    public int getX( )
    {
        return x;
    }

    public int getY( )
    {
        return y;
    }

    public int[ ] getSize( )
    {
        return size;
    }

    public String getName( )
    {
        return name;
    }

    public String getText( )
    {
        return text;
    }

    public int getColor( )
    {
        return color;
    }

    public void setEnabled( boolean enabled )
    {
        this.enabled = enabled;
    }

    public void setX( int x )
    {
        this.x = x;
    }

    public void setY( int y )
    {
        this.y = y;
    }

    public void setSize( int[ ] size )
    {
        this.size = size;
    }

    public void setText( String text )
    {
        this.text = text;
    }

    public void setColor( int color )
    {
        this.color = color;
    }

    public boolean isMouseHovered( int mouseX, int mouseY )
    {
        return ( mouseX >= x - 1 && mouseY >= y - 1 ) &&
                ( mouseX <= ( x + size[ 0 ] ) && mouseY <= ( y + size[ 1 ] ) );
    }

    public void render( int mouseX, int mouseY )
    {
        ScaledResolution res = new ScaledResolution( mc );

        if( mc.currentScreen instanceof HudEditorScreen )
        {
            if( dragging )
            {
                setX( mouseX - dragX );
                setY( mouseY - dragY );
            }

            int alpha = 102;

            if( isMouseHovered( mouseX, mouseY ) )
                alpha = 120;

            if( dragging )
                alpha = 140;

            Gui.drawRect( x - 1, y - 1, x + size[ 0 ] + 1, y + size[ 1 ] + 1,
                    new Color( 0, 0, 0, alpha ).getRGB( ) );
        }

        int position = 0;
        int w = res.getScaledWidth( );
        int h = res.getScaledHeight( );

        if( y >= ( h / 2 ) )
            Collections.reverse( drawtext );

        if( x <= ( w / 3 ) )
            position = -1;
        else if( x >= ( w - ( w / 3 ) ) )
            position = 1;

        int addy = 0;
        for( String str : drawtext )
        {
            if( position == 0 )
            {
                int strw = FontRender.getStringWidth( str );
                FontRender.drawStringWithShadow( str, x + ( size[ 0 ] / 2 ) - ( strw / 2 ), y + addy, getColor( ) );
            }
            else if( position == 1 )
            {
                int strw = FontRender.getStringWidth( str );
                FontRender.drawStringWithShadow( str, x + size[ 0 ] - strw, y + addy, getColor( ) );
            }
            else if( position == -1 )
                FontRender.drawStringWithShadow( str, x, y + addy, getColor( ) );

            addy += FontRender.getFontHeight( );
        }

        //RenderUtil.drawStringWithShadow( getText( ), x, y, getColor( ) );
    }

    public void mouseClicked( int mouseX, int mouseY, int button )
    {
        if( mc.currentScreen instanceof HudEditorScreen )
        {
            if( button == 0 && isMouseHovered( mouseX, mouseY ) )
            {
                dragging = true;
                dragX = mouseX - x;
                dragY = mouseY - y;
            }
        }
    }

    public void mouseReleased( int mouseX, int mouseY, int mouseButton )
    {
        if( mouseButton == 0 )
        {
            dragging = false;

            ScaledResolution res = new ScaledResolution( mc );

            // offscreen clamping
            if( ( x + size[ 0 ] ) >= res.getScaledWidth( ) )
                setX( res.getScaledWidth( ) - size[ 0 ] );
            if( ( y + size[ 1 ] ) >= res.getScaledHeight( ) )
                setY( res.getScaledHeight( ) - size[ 1 ] );

            if( x < 0 )
                setX( 0 );
            if( y < 0 )
                setY( 0 );
        }
    }

    public void update( )
    {
        drawtext = Arrays.asList( getText( ).split( "\n" ) );

        for( String str : drawtext )
            size[ 0 ] = Math.max( FontRender.getStringWidth( str ), size[ 0 ] );
        size[ 1 ] = ( drawtext.size( ) * FontRender.getFontHeight( ) ) - 1;

        // first time
        if( x == -1337 )
        {
            x = 50;
            y = ( order * size[ 1 ] ) + ( order * 5 );
        }
    }
}
