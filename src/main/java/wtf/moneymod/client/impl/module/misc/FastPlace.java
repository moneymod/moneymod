package wtf.moneymod.client.impl.module.misc;

import net.minecraft.item.ItemBlock;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.mixin.mixins.ducks.IMinecraft;

@Module.Register( label = "FastPlace", cat = Module.Category.MISC)
public class FastPlace extends Module {

    @Override
    public void onTick() {
        if (mc.player.getHeldItemMainhand().getItem() instanceof ItemBlock) {
            ((IMinecraft) mc).setRightClickDelayTimer(0);
        }
    }
}
