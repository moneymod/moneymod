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

    @Value(value = "Health") @Bounds(min = 1, max = 36) public int health = 16;
    @Value(value = "Item") public Mode mode = Mode.CRYSTAL;
    @Value(value = "Right Click Gapple") public boolean rightClickGapple = true;

    @Override
    public void onTick() {
        if (nullCheck() || mc.currentScreen instanceof GuiInventory) return;
        float hp = mc.player.getHealth() + mc.player.getAbsorptionAmount();
        if (hp > health){
            if (rightClickGapple && mc.player.getHeldItemMainhand().getItem() instanceof ItemSword && mc.gameSettings.keyBindUseItem.isKeyDown()){
                ItemUtil.swapItemsOffhand(ItemUtil.getItemSlot(Items.GOLDEN_APPLE));
            } else {
                switch (mode) {
                    case TOTEM:
                        ItemUtil.swapItemsOffhand(ItemUtil.getItemSlot(Items.TOTEM_OF_UNDYING));
                        break;
                    case GAPPLE:
                        ItemUtil.swapItemsOffhand(ItemUtil.getItemSlot(Items.GOLDEN_APPLE));
                        break;
                    case CRYSTAL:
                        ItemUtil.swapItemsOffhand(ItemUtil.getItemSlot(Items.END_CRYSTAL));
                        break;
                }
            }
        } else {
            ItemUtil.swapItemsOffhand(ItemUtil.getItemSlot(Items.TOTEM_OF_UNDYING));
            return;
        }
    }
    public enum Mode { TOTEM, GAPPLE, CRYSTAL }
}
