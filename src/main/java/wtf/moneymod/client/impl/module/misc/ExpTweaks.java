package wtf.moneymod.client.impl.module.misc;

import net.minecraft.init.Items;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.util.EnumHand;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.player.ItemUtil;
import wtf.moneymod.client.mixin.mixins.ducks.IMinecraft;

@Module.Register( label = "ExpTweaks", cat = Module.Category.MISC)
public class ExpTweaks extends Module {

    @Value(value = "Silent") public boolean silent = false;

    @Override
    public void onTick() {
        if (mc.player.getHeldItemMainhand().getItem() == Items.EXPERIENCE_BOTTLE && !silent) {
            ((IMinecraft) mc).setRightClickDelayTimer(0);
        }
        if (silent) {
            int old = mc.player.inventory.currentItem; int xp = ItemUtil.findHotbarBlock(ItemExpBottle.class);
            if (xp == -1) return;
            ItemUtil.switchToHotbarSlot(xp, false);
            mc.playerController.processRightClick(mc.player, mc.world, EnumHand.MAIN_HAND);
            ItemUtil.switchToHotbarSlot(old, false);
        }
    }
}
