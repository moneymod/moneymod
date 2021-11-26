package wtf.moneymod.client.impl.utility.impl.player;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import wtf.moneymod.client.api.management.impl.PacketManagement;
import wtf.moneymod.client.impl.utility.Globals;

import java.util.*;
import java.util.stream.Collectors;

public class ItemUtil implements Globals {

    public static Map<ItemStack, Integer> getHotbarItems() {
        Map<ItemStack, Integer> items = new HashMap<>();
        for (int j = 0; j < 9; j++) {
            items.put(mc.player.inventory.getStackInSlot(j), j);
        }
        return items;
    }

    public static int getGappleSlot(boolean crapple) {
        if (Items.GOLDEN_APPLE == mc.player.getHeldItemOffhand().getItem() && (crapple == (mc.player.getHeldItemOffhand().getRarity().equals(EnumRarity.RARE))))
            return -1;
        for (int i = 36; i >= 0; i--) {
            final ItemStack item = mc.player.inventory.getStackInSlot(i);
            if ((crapple == item.getRarity().equals(EnumRarity.RARE)) && item.getItem() == Items.GOLDEN_APPLE) {
                if (i < 9) {
                    i += 36;
                }
                return i;
            }
        }
        return -1;
    }


    public static int getItemSlot(Item input) {
        if (input == mc.player.getHeldItemOffhand().getItem()) return -1;
        for (int i = 36; i >= 0; i--) {
            final Item item = mc.player.inventory.getStackInSlot(i).getItem();
            if (item == input) {
                if (i < 9) {
                    i += 36;
                }
                return i;
            }
        }
        return -1;
    }

    public static boolean isArmorLow(final EntityPlayer player, final int durability) {
        for (int i = 0; i < 4; ++i) {
            if (getDamageInPercent(player.inventory.armorInventory.get(i)) < durability) {
                return true;
            }
        }
        return false;
    }

    public static float getDamageInPercent(final ItemStack stack) {
        final float green = (stack.getMaxDamage() - ( float ) stack.getItemDamage()) / stack.getMaxDamage();
        final float red = 1.0f - green;
        return ( float ) (100 - ( int ) (red * 100.0f));
    }


    public static void swapToOffhandSlot(int slot) {
        if (slot == -1) return;
        mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player);
        mc.playerController.updateController();
    }
    
    public static int swapToHotbarSlot(int slot, boolean silent){
        if (mc.player.inventory.currentItem == slot || slot < 0 || slot > 8) return slot;
        PacketManagement.getInstance().addLast(new CPacketHeldItemChange(slot));
        if (!silent) mc.player.inventory.currentItem = slot;
        mc.playerController.updateController();
        return slot;
    }

    public static int findItem(Block... blockIn) {
        List<Block> list = Arrays.stream(blockIn).collect(Collectors.toList());
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack == ItemStack.EMPTY || !(stack.getItem() instanceof ItemBlock) || (!list.contains((( ItemBlock ) stack.getItem()).getBlock())))
                continue;
            return i;
        }
        return -1;
    }

    public static int findItem(Class clazz) {
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack == ItemStack.EMPTY) continue;
            if (clazz.isInstance(stack.getItem())) {
                return i;
            }
            if (!(stack.getItem() instanceof ItemBlock) || !clazz.isInstance((( ItemBlock ) stack.getItem()).getBlock()))
                continue;
            return i;
        }
        return -1;
    }


}