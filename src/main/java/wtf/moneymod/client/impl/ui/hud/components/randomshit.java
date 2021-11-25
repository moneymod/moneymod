package wtf.moneymod.client.impl.ui.hud.components;

import wtf.moneymod.client.impl.ui.hud.HudComponent;

import java.util.Random;

public class randomshit extends HudComponent
{
    public randomshit( )
    {
        super( "RandomShit" );
    }

    @Override
    public void update( )
    {
        setText( "random " + new Random( ).nextInt( 200000 ) );
        super.update( );
    }
}

