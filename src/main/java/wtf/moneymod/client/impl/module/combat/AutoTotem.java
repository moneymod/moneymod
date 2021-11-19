package wtf.moneymod.client.impl.module.combat;

import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;
import org.lwjgl.input.Mouse;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.player.ItemUtil;

@Module.Register( label = "AutoTotem", cat = Module.Category.COMBAT )
public class AutoTotem extends Module {

    //pig pig pig

    @Value(value = "Health") @Bounds(max = 36) public int health = 16;
    @Value(value = "Item") public Mode mode = Mode.CRYSTAL;
    @Value(value = "Right Click Gapple") public boolean rightClickGapple = true;

    boolean doStrict;

    @Override
    public void onTick() {
        if (nullCheck() || mc.currentScreen instanceof GuiInventory) return;
        float hp = mc.player.getHealth() + mc.player.getAbsorptionAmount();
        if (rightClickGapple && mc.player.getHeldItemMainhand().getItem() instanceof ItemSword && mc.gameSettings.keyBindUseItem.isKeyDown() && hp >= health)
            swap(Items.GOLDEN_APPLE);
        if (hp >= health && mode != Mode.TOTEM) {
            switch (mode) {
                case GAPPLE:
                    swap(Items.GOLDEN_APPLE);
                    break;
                case CRYSTAL:
                    swap(Items.END_CRYSTAL);
                    break;
            }
        } else {
            swap(Items.TOTEM_OF_UNDYING);
            return;
        }
    }


    public void swap(Item items){
        ItemUtil.swapToOffhandSlot(ItemUtil.getItemSlot(items));
    }

    public enum Mode { TOTEM, GAPPLE, CRYSTAL }
}
