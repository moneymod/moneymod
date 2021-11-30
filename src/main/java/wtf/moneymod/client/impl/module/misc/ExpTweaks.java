package wtf.moneymod.client.impl.module.misc;

import net.minecraft.item.ItemExpBottle;
import net.minecraft.util.EnumHand;
import org.lwjgl.input.Mouse;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.player.ItemUtil;

@Module.Register( label = "ExpTweaks", cat = Module.Category.MISC)
public class ExpTweaks extends Module {

    @Value(value = "Mode") public Mode mode = Mode.MIDDLE;
    public enum Mode{
        TOGGLE, MIDDLE
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;

        int old = mc.player.inventory.currentItem;
        int xp = ItemUtil.findItem(ItemExpBottle.class);
        if (xp == -1) return;

        if (mode == Mode.MIDDLE){
            if (!Mouse.isButtonDown(2)) return;
        }

        ItemUtil.swapToHotbarSlot(xp, false);
        mc.playerController.processRightClick(mc.player, mc.world, EnumHand.MAIN_HAND);
        ItemUtil.swapToHotbarSlot(old, false);
    }
}
