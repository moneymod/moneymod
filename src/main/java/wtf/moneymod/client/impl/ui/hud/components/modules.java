package wtf.moneymod.client.impl.ui.hud.components;

import wtf.moneymod.client.Main;
import wtf.moneymod.client.impl.ui.hud.HudComponent;
import wtf.moneymod.client.impl.utility.impl.render.fonts.FontRender;

import java.util.Comparator;

public class modules extends HudComponent
{
    public modules( )
    {
        super( "Modules" );
    }

    @Override
    public void update( )
    {
        StringBuilder text = new StringBuilder( "" );
        Main.getMain( ).getModuleManager( ).stream( ).sorted( Comparator.comparing( module -> -FontRender.getStringWidth( module.getLabel( ) ) ) ).filter( m -> m.isToggled( ) && m.drawn ).forEach( m -> {
            text.append( m.getLabel( ) );
            text.append( "\n" );
        } );
        setText( text.toString( ).substring( 0, text.length( ) - 1 ) );
        super.update( );
    }
}