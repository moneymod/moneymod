package wtf.moneymod.client.impl.utility.impl.player;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import wtf.moneymod.client.impl.utility.Globals;
import wtf.moneymod.client.impl.utility.impl.world.BlockUtil;

public enum ToolUtil implements Globals {
    INSTANCE;

    public boolean canHarvestBlock(Block block, BlockPos pos, ItemStack stack) {
        IBlockState state = mc.world.getBlockState(pos);
        state = state.getBlock().getActualState(state, mc.world, pos);

        if (state.getMaterial().isToolNotRequired()) {
            return true;
        }

        String tool = block.getHarvestTool(state);

        if (stack.isEmpty() || tool == null) {
            return mc.player.canHarvestBlock(state);
        }

        final int toolLevel = stack.getItem().getHarvestLevel(stack, tool, mc.player, state);

        if (toolLevel < 0) {
            return mc.player.canHarvestBlock(state);
        }

        return toolLevel >= block.getHarvestLevel(state);
    }

    public int bestSlot(BlockPos pos) {
        int best = 0;
        double max = 0;

        for (int i = 0; i < 9; ++i) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (!stack.isEmpty()) {
                float speed = stack.getDestroySpeed(mc.world.getBlockState(pos));
                if (speed > 1.0f) {
                    int eff;
                    speed += ( float ) (((eff = EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack)) > 0) ? (Math.pow(eff, 2.0) + 1.0) : 0.0);
                    if (speed > max) {
                        max = speed;
                        best = i;
                    }
                }
            }
        }

        return best;

    }

    public long time(BlockPos pos) {
        return time(pos, EnumHand.MAIN_HAND);
    }

    public long time(BlockPos pos, EnumHand hand) {
        return time(pos, mc.player.getHeldItem(hand));
    }

    public long time(BlockPos pos, ItemStack stack) {
        final IBlockState state = mc.world.getBlockState(pos);
        final Block block = state.getBlock();

        float toolMultiplier = stack.getDestroySpeed(state);

        if (!canHarvestBlock(block, pos, stack) && block != Blocks.ENDER_CHEST)
            toolMultiplier = 1.0f;
        else if (EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack) != 0) {
            toolMultiplier += Math.pow(EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack), 2) + 1;
        }

        if (mc.player.isPotionActive(MobEffects.HASTE)) {
            toolMultiplier *= 1.0F + ( float ) (mc.player.getActivePotionEffect(MobEffects.HASTE).getAmplifier() + 1) * 0.2F;
        }

        if (mc.player.isPotionActive(MobEffects.MINING_FATIGUE)) {
            float f1;
            switch (mc.player.getActivePotionEffect(MobEffects.MINING_FATIGUE).getAmplifier()) {
                case 0:
                    f1 = 0.3F;
                    break;
                case 1:
                    f1 = 0.09F;
                    break;
                case 2:
                    f1 = 0.0027F;
                    break;
                case 3:
                default:
                    f1 = 8.1E-4F;
                    break;
            }
            toolMultiplier *= f1;
        }

        if (mc.player.isInsideOfMaterial(Material.WATER) && !EnchantmentHelper.getAquaAffinityModifier(mc.player)) {
            toolMultiplier /= 5.0F;
        }

        float dmg = toolMultiplier / state.getBlockHardness(mc.world, pos);

        if (canHarvestBlock(block, pos, stack) || block == Blocks.ENDER_CHEST)
            dmg /= 30;
        else
            dmg /= 100;

        if (dmg > 1)
            dmg = 0;

        float ticks = ( float ) (Math.floor(1.0f / dmg) + 1.0f);

        return ( long ) ((ticks / 20.0f) * 1000);
    }

}
