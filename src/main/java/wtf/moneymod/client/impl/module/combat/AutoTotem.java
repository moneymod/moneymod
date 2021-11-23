package wtf.moneymod.client.impl.module.combat;

import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSword;
import org.lwjgl.input.Mouse;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.player.ItemUtil;

@Module.Register( label = "AutoTotem", cat = Module.Category.COMBAT )
public class AutoTotem extends Module {

    //pig pig pig

    @Value( value = "Health" ) @Bounds( max = 36 ) public int health = 16;
    @Value( value = "Item" ) public Mode mode = Mode.CRYSTAL;
    @Value( "Crapple" ) public boolean crapple = false;
    @Value( value = "Right Click Gapple" ) public boolean rightClickGapple = true;

    @Override
    public void onTick() {
        if (nullCheck() || mc.currentScreen instanceof GuiInventory) return;
        float hp = mc.player.getHealth() + mc.player.getAbsorptionAmount();
        if (hp > health) {
            Item heldItem = mc.player.getHeldItemMainhand().getItem();
            if (rightClickGapple && mc.gameSettings.keyBindUseItem.isKeyDown() && (heldItem instanceof ItemSword || heldItem instanceof ItemAxe) && mc.currentScreen == null) {
                if (mode != Mode.GAPPLE)
                    ItemUtil.swapToOffhandSlot(getSlot(Mode.GAPPLE));
            } else {
                ItemUtil.swapToOffhandSlot(getSlot(mode));
            }
        } else {
            ItemUtil.swapToOffhandSlot(ItemUtil.getItemSlot(Items.TOTEM_OF_UNDYING));
        }
    }

    private int getSlot(Mode mode) {
        switch (mode) {
            case CRYSTAL:
                return ItemUtil.getItemSlot(Items.END_CRYSTAL);
            case GAPPLE:
                return ItemUtil.getGappleSlot(crapple);
            default:
                return ItemUtil.getItemSlot(Items.TOTEM_OF_UNDYING);
        }
    }

    public enum Mode {
        TOTEM,
        GAPPLE,
        CRYSTAL
    }

}