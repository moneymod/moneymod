package wtf.moneymod.client.impl.module.combat;

import club.cafedevelopment.reflectionsettings.annotation.Clamp;
import club.cafedevelopment.reflectionsettings.annotation.Setting;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;
import org.lwjgl.input.Mouse;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.player.ItemUtil;

@Module.Register( label = "AutoTotem", cat = Module.Category.COMBAT )
public class AutoTotem extends Module {

    //pig pig pig

    @Setting(id = "Health", clamp = @Clamp(min = 1, max = 36)) public int health = 16;
    @Setting(id = "Mode") public Mode mode = Mode.CRYSTAL;
    @Setting(id = "RightClickGapple") public boolean rightClickGapple = true;

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
