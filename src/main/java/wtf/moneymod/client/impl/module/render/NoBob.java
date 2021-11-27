package wtf.moneymod.client.impl.module.render;

import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;

@Module.Register( label = "NoBob", cat = Module.Category.RENDER )
public class NoBob extends Module
{
    @Value( "NoSway" ) public boolean nosway = false;

    public boolean oldvalue = false;

    @Override
    public void onEnable( )
    {
        super.onEnable( );
        oldvalue = mc.gameSettings.viewBobbing;
    }

    @Override
    public void onDisable( )
    {
        super.onDisable( );
        mc.gameSettings.viewBobbing = oldvalue;
    }

    @Override
    public void onTick( )
    {
        if( mc.gameSettings != null )
            mc.gameSettings.viewBobbing = false;
    }
}
