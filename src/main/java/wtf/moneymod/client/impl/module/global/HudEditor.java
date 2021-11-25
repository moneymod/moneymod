package wtf.moneymod.client.impl.module.global;

import wtf.moneymod.client.Main;
import wtf.moneymod.client.impl.module.Module;

@Module.Register( label = "HudEditorTest", desc = "testing", cat = Module.Category.GLOBAL )
public class HudEditor extends Module
{
    @Override
    public void onEnable( )
    {
        disable( );
        mc.displayGuiScreen( Main.getMain( ).getHudEditorScreen( ) );
    }
}
