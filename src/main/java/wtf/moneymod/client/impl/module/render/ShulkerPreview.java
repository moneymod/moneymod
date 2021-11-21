package wtf.moneymod.client.impl.module.render;

import net.minecraft.item.Item;
import wtf.moneymod.client.impl.module.Module;

@Module.Register( label = "ShulkerPreview", cat = Module.Category.RENDER)
public class ShulkerPreview extends Module {

    private boolean check = false;
    private Item item = null;

    public void setState( boolean state )
    {
        this.check = state;
    }

    public boolean check( )
    {
        return check;
    }

    public void setItem( Item item )
    {
        this.item = item;
    }

    public Item getItem( )
    {
        return item;
    }

}
