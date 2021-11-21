package wtf.moneymod.client.impl.module.player;

import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.util.text.TextComponentString;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;

import java.util.HashMap;
import java.util.Map;

@Module.Register( label = "Refill", cat = Module.Category.PLAYER)
public class Refill extends Module {

    @Value(value = "Count") @Bounds(max = 64) public int count = 48;

    int delay = 1;
    private int delayStep = 0;

    private Map<Integer, ItemStack> getInventory() {
        return this.getInventorySlots(9, 35);
    }

    private Map<Integer, ItemStack> getHotbar() {
        return this.getInventorySlots(36, 44);
    }

    private Map<Integer, ItemStack> getInventorySlots(int current, int last) {
        HashMap<Integer, ItemStack> fullInventorySlots = new HashMap<Integer, ItemStack>();
        while (current <= last) {
            fullInventorySlots.put(current, (ItemStack)this.mc.player.inventoryContainer.getInventory().get(current));
            ++current;
        }
        return fullInventorySlots;
    }

    @Override
    public void onTick(){
        if (this.nullCheck()) {
            return;
        }
        if (this.mc.currentScreen instanceof GuiContainer) {
            return;
        }
        if (this.delayStep < delay) {
            ++this.delayStep;
            return;
        }
        this.delayStep = 0;
        Pair<Integer, Integer> slots = this.findReplenishableHotbarSlot();
        if (slots == null) {
            return;
        }
        int inventorySlot = slots.getKey();
        int hotbarSlot = slots.getValue();
        this.mc.playerController.windowClick(0, inventorySlot, 0, ClickType.PICKUP, (EntityPlayer)this.mc.player);
        this.mc.playerController.windowClick(0, hotbarSlot, 0, ClickType.PICKUP, (EntityPlayer)this.mc.player);
        this.mc.playerController.windowClick(0, inventorySlot, 0, ClickType.PICKUP, (EntityPlayer)this.mc.player);
    }

    private Pair<Integer, Integer> findReplenishableHotbarSlot() {
        Pair<Integer, Integer> returnPair = null;
        for (Map.Entry<Integer, ItemStack> hotbarSlot : this.getHotbar().entrySet()) {
            int inventorySlot;
            ItemStack stack = hotbarSlot.getValue();
            if (stack.isEmpty() || stack.getItem() == Items.AIR || !stack.isStackable() || stack.getCount() >= stack.getMaxStackSize() || stack.getCount() >= count || (inventorySlot = this.findCompatibleInventorySlot(stack)) == -1) continue;
            returnPair = new Pair<Integer, Integer>(inventorySlot, hotbarSlot.getKey());
        }
        return returnPair;
    }

    private int findCompatibleInventorySlot(ItemStack hotbarStack) {
        int inventorySlot = -1;
        int smallestStackSize = 999;
        for (Map.Entry<Integer, ItemStack> entry : this.getInventory().entrySet()) {
            int currentStackSize;
            ItemStack inventoryStack = entry.getValue();
            if (inventoryStack.isEmpty() || inventoryStack.getItem() == Items.AIR || !this.isCompatibleStacks(hotbarStack, inventoryStack) || smallestStackSize <= (currentStackSize = ((ItemStack)this.mc.player.inventoryContainer.getInventory().get(entry.getKey().intValue())).getCount())) continue;
            smallestStackSize = currentStackSize;
            inventorySlot = entry.getKey();
        }
        return inventorySlot;
    }

    private boolean isCompatibleStacks(ItemStack stack1, ItemStack stack2) {
        if (!stack1.getItem().equals(stack2.getItem())) {
            return false;
        }
        if (stack1.getItem() instanceof ItemBlock && stack2.getItem() instanceof ItemBlock) {
            Block block1 = ((ItemBlock)stack1.getItem()).getBlock();
            Block block2 = ((ItemBlock)stack2.getItem()).getBlock();
            if (!block1.getMaterial(block1.getBlockState().getBaseState()).equals(block2.getMaterial(block2.getBlockState().getBaseState()))) {
                return false;
            }
        }
        if (!this.stackEqualExact(stack1, stack2)) {
            return false;
        }
        return stack1.getItemDamage() == stack2.getItemDamage();
    }

    private boolean stackEqualExact(ItemStack stack1, ItemStack stack2) {
        return stack1.getItem() == stack2.getItem() && (!stack1.getHasSubtypes() || stack1.getMetadata() == stack2.getMetadata()) && ItemStack.areItemStackTagsEqual((ItemStack)stack1, (ItemStack)stack2);
    }

    private static class Pair<K, V> {
        final K key;
        final V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public V getValue() {
            return this.value;
        }

        public K getKey() {
            return this.key;
        }
    }
}
