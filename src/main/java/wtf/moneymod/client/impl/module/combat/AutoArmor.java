package wtf.moneymod.client.impl.module.combat;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.*;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.misc.Timer;
import wtf.moneymod.client.impl.utility.impl.player.ItemUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Module.Register( label = "AutoArmor", cat = Module.Category.COMBAT)
public class AutoArmor extends Module {

    public static boolean pause = false;
    public static boolean checkCrafting = false;
    private final Timer timer = new Timer();
    private final ArrayList<Item> leggings = new ArrayList<>(Arrays.asList(Items.LEATHER_LEGGINGS, Items.IRON_LEGGINGS, Items.GOLDEN_LEGGINGS, Items.CHAINMAIL_LEGGINGS, Items.DIAMOND_LEGGINGS));
    private final ArrayList<Item> helmets = new ArrayList<>(Arrays.asList(Items.LEATHER_HELMET, Items.IRON_HELMET, Items.GOLDEN_HELMET, Items.CHAINMAIL_HELMET, Items.DIAMOND_HELMET));
    private final ArrayList<Item> chestplates = new ArrayList<>(Arrays.asList(Items.LEATHER_CHESTPLATE, Items.IRON_CHESTPLATE, Items.GOLDEN_CHESTPLATE, Items.CHAINMAIL_CHESTPLATE, Items.DIAMOND_CHESTPLATE, Items.ELYTRA));
    private final ArrayList<Item> boots = new ArrayList<>(Arrays.asList(Items.LEATHER_BOOTS, Items.IRON_BOOTS, Items.GOLDEN_BOOTS, Items.CHAINMAIL_BOOTS, Items.DIAMOND_BOOTS));

    @Value(value = "Elytra") public boolean elytras = false;

    @Override
    public void onTick() {
        if (nullCheck()) return;
        if (pause) return;
        if (mc.currentScreen != null) return;

        final int[] refined = refineList(mapArmor());
        int switched = 0;
        boolean hadtowait = false;

        for (int a = 0; a < 4; a++) {
            if (refined[a] != -1) {
                final ItemStack armor = mc.player.inventoryContainer.getInventory().get(a + 5);

                if (armor == null || armor.getItem() == Items.AIR) {
                    if (!timer.passed(100)) {
                        hadtowait = true;
                        timer.reset();
                        continue;
                    }

                    mc.playerController.windowClick(mc.player.inventoryContainer.windowId, refined[a], 0, ClickType.PICKUP, mc.player);
                    mc.playerController.windowClick(mc.player.inventoryContainer.windowId, a + 5, 0, ClickType.PICKUP, mc.player);
                    mc.playerController.windowClick(mc.player.inventoryContainer.windowId, refined[a], 0, ClickType.PICKUP, mc.player);
                    switched++;
                }
            }
        }

        if (!hadtowait) {
            if (switched == 0) checkCrafting = false;
        }
    }

    private ArrayList<Integer> mapArmor() {
        ArrayList<Integer> map = new ArrayList<>();
        for (int a = 9; a < 36; a++) {
            final Item item = mc.player.inventoryContainer.getInventory().get(a).getItem();
            if (item instanceof ItemArmor || item instanceof ItemElytra) {
                map.add(a);
            }
        }
        return map;
    }

    private int[] refineList(ArrayList<Integer> map) {
        int legSlot = -1;
        int chestSlot = -1;
        int bootsSlot = -1;
        int helmetSlot = -1;
        for (int slot : map) {
            final ItemStack item = mc.player.inventoryContainer.getInventory().get(slot);

            if (EnchantmentHelper.getEnchantments(item).containsKey(Enchantment.getEnchantmentByID(7))) {
                continue;
            }

            if (leggings.contains(item.getItem())) {
                if (legSlot == -1) {
                    legSlot = slot;
                } else {
                    final Item leg = mc.player.inventoryContainer.getInventory().get(legSlot).getItem();
                    if (((ItemArmor) item.getItem()).damageReduceAmount < ((ItemArmor) leg).damageReduceAmount) {
                        legSlot = slot;
                    }
                }
            } else if (boots.contains(item.getItem())) {
                if (bootsSlot == -1) {
                    bootsSlot = slot;
                } else {
                    final Item boot = mc.player.inventoryContainer.getInventory().get(bootsSlot).getItem();
                    if (((ItemArmor) item.getItem()).damageReduceAmount < ((ItemArmor) boot).damageReduceAmount) {
                        bootsSlot = slot;
                    }
                }
            } else if (chestplates.contains(item.getItem())) {
                if (chestSlot == -1) {
                    chestSlot = slot;
                } else {
                    final Item chest = mc.player.inventoryContainer.getInventory().get(chestSlot).getItem();
                    if (item.getItem() instanceof ItemElytra) {
                        chestSlot = slot;
                    }
                    if (chest instanceof ItemElytra && !(item.getItem() instanceof ItemElytra) && elytras) {
                        continue;
                    }
                    if (item.getItem() instanceof ItemArmor && chest instanceof ItemArmor) {
                        if (((ItemArmor) item.getItem()).damageReduceAmount < ((ItemArmor) chest).damageReduceAmount) {
                            chestSlot = slot;
                        }
                    }
                }
            } else if (helmets.contains(item.getItem())) {
                if (helmetSlot == -1) {
                    helmetSlot = slot;
                } else {
                    final Item helmet = mc.player.inventoryContainer.getInventory().get(helmetSlot).getItem();
                    if (((ItemArmor) item.getItem()).damageReduceAmount < ((ItemArmor) helmet).damageReduceAmount) {
                        helmetSlot = slot;
                    }
                }
            }
        }

        return new int[]{helmetSlot, chestSlot, legSlot, bootsSlot};
    }
}