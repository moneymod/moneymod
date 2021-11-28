package wtf.moneymod.client.impl.module.player;

import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.util.EnumHand;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;

@Module.Register( label = "NoEntityTrace", cat = Module.Category.PLAYER )
public class NoEntityTrace extends Module
{
    @Value( "Smart" ) public boolean onlypickaxe = true;



    public boolean check( )
    {
        if( !onlypickaxe ) return true;

        return mc.player.getHeldItem( EnumHand.MAIN_HAND ).getItem( ) instanceof ItemPickaxe || mc.player.getHeldItem( EnumHand.MAIN_HAND ).getItem( ) instanceof ItemAppleGold;
    }
}
