package wtf.moneymod.client.impl.ui.hud.components;

import wtf.moneymod.client.Main;
import wtf.moneymod.client.impl.ui.hud.HudComponent;

public class fps extends HudComponent
{
    public fps()
    {
        super("FPS");
    }

    @Override
    public void update( )
    {
        setText( "FPS: " + Main.getMain( ).getFpsManagement( ).getFPS( ) );
        super.update( );
    }
}
