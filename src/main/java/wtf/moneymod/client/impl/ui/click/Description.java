package wtf.moneymod.client.impl.ui.click;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.impl.module.global.ClickGui;
import wtf.moneymod.client.impl.utility.Globals;

import java.awt.*;

public class Description implements Globals
{
    private boolean draw;
    private int mouseX;
    private int mouseY;
    private String text;

    public void reset( )
    {
        draw = false;
        mouseX = 0;
        mouseY = 0;
        text = "";
    }

    public void update( String text, int mouseX, int mouseY )
    {
        this.draw = true;
        this.text = text;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    public void draw( )
    {
        if( draw )
        {
            ClickGui clickgui = ( ClickGui ) Main.getMain().getModuleManager( ).get( ClickGui.class );

            if( clickgui.desc && text.length( ) > 0 )
            {
                ScaledResolution sr = new ScaledResolution( mc );
                int width = mc.fontRenderer.getStringWidth( text );
                int height = mc.fontRenderer.FONT_HEIGHT;

                boolean left = false;
                if( ( mouseX + width ) >= sr.getScaledWidth_double( ) )
                    left = true;

                int startx =
                        left
                                ? mouseX - width - 2
                                : mouseX + 2;

                Gui.drawRect( startx, mouseY - height - 1, startx + width + 3, mouseY + 1, new Color( 0, 0, 0, 170 ).getRGB( ) );
                mc.fontRenderer.drawStringWithShadow( text, startx + 2, mouseY - height + 1, new Color( 255, 255, 255, 255 ).getRGB( ) );
            }
        }
    }
}
