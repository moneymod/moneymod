package wtf.moneymod.client.impl.module.misc;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.util.EnumHand;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.player.ItemUtil;
import wtf.moneymod.client.mixin.mixins.ducks.IMinecraft;

@Module.Register( label = "FastUse", cat = Module.Category.MISC)
public class FastUse extends Module {

    @Value(value = "Xp") public boolean xp = true;
    @Value(value = "Blocks") public boolean blocks = false;
    @Value(value = "Crystals") public boolean crystals = false;
    @Value(value = "All") public boolean all = false;

    @Override
    public void onTick() {
        if (mc.player.getHeldItemMainhand().getItem() == Items.EXPERIENCE_BOTTLE && xp)
            ((IMinecraft) mc).setRightClickDelayTimer(0);
        if (mc.player.getHeldItemMainhand().getItem() instanceof ItemBlock && blocks)
            ((IMinecraft) mc).setRightClickDelayTimer(0);
        if (mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL && crystals)
            ((IMinecraft) mc).setRightClickDelayTimer(0);
        if (all)
            ((IMinecraft) mc).setRightClickDelayTimer(0);
    }
}
