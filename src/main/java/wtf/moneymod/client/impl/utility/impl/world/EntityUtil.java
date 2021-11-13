package wtf.moneymod.client.impl.utility.impl.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockWeb;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.*;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.management.impl.FriendManagement;
import wtf.moneymod.client.impl.utility.Globals;

import java.util.Arrays;
import java.util.List;

/**
 * @author cattyn
 * @since 11/02/21
 */

public enum EntityUtil implements Globals {
    INSTANCE;

    private static List<Block> valid = Arrays.asList(Blocks.OBSIDIAN, Blocks.BEDROCK, Blocks.ENDER_CHEST, Blocks.ANVIL);

    public static EntityPlayer getTarget(final float range) {
        EntityPlayer currentTarget = null;
        for (int size = mc.world.playerEntities.size(), i = 0; i < size; ++i) {
            final EntityPlayer player = mc.world.playerEntities.get(i);
            if (!EntityUtil.isntValid(player, range)) {
                if (currentTarget == null) {
                    currentTarget = player;
                } else if (mc.player.getDistanceSq(player) < mc.player.getDistanceSq(currentTarget)) {
                    currentTarget = player;
                }
            }
        }
        return currentTarget;
    }

    public static boolean isntValid(final EntityPlayer entity, final double range) {
        return EntityUtil.mc.player.getDistance(entity) > range || entity == EntityUtil.mc.player || entity.getHealth() <= 0.0f || entity.isDead || FriendManagement.getInstance().is(entity.getName());
    }


    public boolean isMoving(EntityLivingBase entity) {
        return entity.moveStrafing != 0 || entity.moveForward != 0;
    }

    public static double[] forward(double d) {
        float f = mc.player.movementInput.moveForward;
        float f2 = mc.player.movementInput.moveStrafe;
        float f3 = mc.player.prevRotationYaw + (mc.player.rotationYaw - mc.player.prevRotationYaw) * mc.getRenderPartialTicks();
        if (f != 0.0f) {
            if (f2 > 0.0f) {
                f3 += ( float ) (f > 0.0f ? -45 : 45);
            } else if (f2 < 0.0f) {
                f3 += ( float ) (f > 0.0f ? 45 : -45);
            }
            f2 = 0.0f;
            if (f > 0.0f) {
                f = 1.0f;
            } else if (f < 0.0f) {
                f = -1.0f;
            }
        }
        double d2 = Math.sin(Math.toRadians(f3 + 90.0f));
        double d3 = Math.cos(Math.toRadians(f3 + 90.0f));
        double d4 = ( double ) f * d * d3 + ( double ) f2 * d * d2;
        double d5 = ( double ) f * d * d2 - ( double ) f2 * d * d3;
        return new double[] { d4, d5 };
    }

    public static float getHealth(EntityLivingBase player) {
        return player.getHealth() + player.getAbsorptionAmount();
    }

    public float calculate(double posX, final double posY, double posZ, EntityLivingBase entity) {
        double v = (1.0 - entity.getDistance(posX, posY, posZ) / 12.0) * getBlockDensity(new Vec3d(posX, posY, posZ), entity.getEntityBoundingBox());
        return getBlastReduction(entity, getDamageMultiplied(( float ) ((v * v + v) / 2.0 * 85.0 + 1.0)));
    }

    public float getBlastReduction(EntityLivingBase entity, float damageI) {
        float damage = damageI;
        damage = CombatRules.getDamageAfterAbsorb(damage, ( float ) entity.getTotalArmorValue(), ( float ) entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
        damage *= 1.0f - MathHelper.clamp(( float ) EnchantmentHelper.getEnchantmentModifierDamage(entity.getArmorInventoryList(), EXPLOSION_SOURCE), 0.0f, 20.0f) / 25.0f;
        if (entity.isPotionActive(MobEffects.RESISTANCE)) {
            return damage - damage / 4.0f;
        }
        return damage;
    }

    public float getDamageMultiplied(float damage) {
        final int diff = EntityUtil.mc.world.getDifficulty().getId();
        return damage * ((diff == 0) ? 0.0f : ((diff == 2) ? 1.0f : ((diff == 1) ? 0.5f : 1.5f)));
    }

    public float getBlockDensity(final Vec3d vec, final AxisAlignedBB bb) {
        final double d0 = 1.0 / ((bb.maxX - bb.minX) * 2.0 + 1.0);
        final double d2 = 1.0 / ((bb.maxY - bb.minY) * 2.0 + 1.0);
        final double d3 = 1.0 / ((bb.maxZ - bb.minZ) * 2.0 + 1.0);
        final double d4 = (1.0 - Math.floor(1.0 / d0) * d0) / 2.0;
        final double d5 = (1.0 - Math.floor(1.0 / d3) * d3) / 2.0;
        float j2 = 0.0f;
        float k2 = 0.0f;
        for (float f = 0.0f; f <= 1.0f; f += ( float ) d0) {
            for (float f2 = 0.0f; f2 <= 1.0f; f2 += ( float ) d2) {
                for (float f3 = 0.0f; f3 <= 1.0f; f3 += ( float ) d3) {
                    final double d6 = bb.minX + (bb.maxX - bb.minX) * f;
                    final double d7 = bb.minY + (bb.maxY - bb.minY) * f2;
                    final double d8 = bb.minZ + (bb.maxZ - bb.minZ) * f3;
                    if (rayTraceBlocks(new Vec3d(d6 + d4, d7, d8 + d5), vec, false, true, false) == null) {
                        ++j2;
                    }
                    ++k2;
                }
            }
        }
        return j2 / k2;
    }

    public static RayTraceResult rayTraceBlocks(Vec3d vec31, Vec3d vec32, boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock) {
        if (!Double.isNaN(vec31.x) && !Double.isNaN(vec31.y) && !Double.isNaN(vec31.z)) {
            if (!Double.isNaN(vec32.x) && !Double.isNaN(vec32.y) && !Double.isNaN(vec32.z)) {
                int i = MathHelper.floor(vec32.x);
                int j = MathHelper.floor(vec32.y);
                int k = MathHelper.floor(vec32.z);
                int l = MathHelper.floor(vec31.x);
                int i1 = MathHelper.floor(vec31.y);
                int j1 = MathHelper.floor(vec31.z);
                BlockPos blockpos = new BlockPos(l, i1, j1);
                IBlockState iblockstate = mc.world.getBlockState(blockpos);
                Block block = iblockstate.getBlock();

                if (!valid.contains(block)) {
                    block = Blocks.AIR;
                    iblockstate = Blocks.AIR.getBlockState().getBaseState();
                }

                if ((!ignoreBlockWithoutBoundingBox || iblockstate.getCollisionBoundingBox(mc.world, blockpos) != Block.NULL_AABB) && block.canCollideCheck(iblockstate, stopOnLiquid)) {
                    RayTraceResult raytraceresult = iblockstate.collisionRayTrace(mc.world, blockpos, vec31, vec32);

                    if (raytraceresult != null) {
                        return raytraceresult;
                    }
                }

                RayTraceResult raytraceresult2 = null;
                int k1 = 200;

                while (k1-- >= 0) {
                    if (Double.isNaN(vec31.x) || Double.isNaN(vec31.y) || Double.isNaN(vec31.z)) {
                        return null;
                    }

                    if (l == i && i1 == j && j1 == k) {
                        return returnLastUncollidableBlock ? raytraceresult2 : null;
                    }

                    boolean flag2 = true;
                    boolean flag = true;
                    boolean flag1 = true;
                    double d0 = 999.0D;
                    double d1 = 999.0D;
                    double d2 = 999.0D;

                    if (i > l) {
                        d0 = ( double ) l + 1.0D;
                    } else if (i < l) {
                        d0 = ( double ) l + 0.0D;
                    } else {
                        flag2 = false;
                    }

                    if (j > i1) {
                        d1 = ( double ) i1 + 1.0D;
                    } else if (j < i1) {
                        d1 = ( double ) i1 + 0.0D;
                    } else {
                        flag = false;
                    }

                    if (k > j1) {
                        d2 = ( double ) j1 + 1.0D;
                    } else if (k < j1) {
                        d2 = ( double ) j1 + 0.0D;
                    } else {
                        flag1 = false;
                    }

                    double d3 = 999.0D;
                    double d4 = 999.0D;
                    double d5 = 999.0D;
                    double d6 = vec32.x - vec31.x;
                    double d7 = vec32.y - vec31.y;
                    double d8 = vec32.z - vec31.z;

                    if (flag2) {
                        d3 = (d0 - vec31.x) / d6;
                    }

                    if (flag) {
                        d4 = (d1 - vec31.y) / d7;
                    }

                    if (flag1) {
                        d5 = (d2 - vec31.z) / d8;
                    }

                    if (d3 == -0.0D) {
                        d3 = -1.0E-4D;
                    }

                    if (d4 == -0.0D) {
                        d4 = -1.0E-4D;
                    }

                    if (d5 == -0.0D) {
                        d5 = -1.0E-4D;
                    }

                    EnumFacing enumfacing;

                    if (d3 < d4 && d3 < d5) {
                        enumfacing = i > l ? EnumFacing.WEST : EnumFacing.EAST;
                        vec31 = new Vec3d(d0, vec31.y + d7 * d3, vec31.z + d8 * d3);
                    } else if (d4 < d5) {
                        enumfacing = j > i1 ? EnumFacing.DOWN : EnumFacing.UP;
                        vec31 = new Vec3d(vec31.x + d6 * d4, d1, vec31.z + d8 * d4);
                    } else {
                        enumfacing = k > j1 ? EnumFacing.NORTH : EnumFacing.SOUTH;
                        vec31 = new Vec3d(vec31.x + d6 * d5, vec31.y + d7 * d5, d2);
                    }

                    l = MathHelper.floor(vec31.x) - (enumfacing == EnumFacing.EAST ? 1 : 0);
                    i1 = MathHelper.floor(vec31.y) - (enumfacing == EnumFacing.UP ? 1 : 0);
                    j1 = MathHelper.floor(vec31.z) - (enumfacing == EnumFacing.SOUTH ? 1 : 0);
                    blockpos = new BlockPos(l, i1, j1);
                    IBlockState iblockstate1 = mc.world.getBlockState(blockpos);
                    Block block1 = iblockstate1.getBlock();

                    if (!valid.contains(block1)) {
                        block1 = Blocks.AIR;
                        iblockstate1 = Blocks.AIR.getBlockState().getBaseState();
                    }

                    if (!ignoreBlockWithoutBoundingBox || iblockstate1.getMaterial() == Material.PORTAL || iblockstate1.getCollisionBoundingBox(mc.world, blockpos) != Block.NULL_AABB) {
                        if (block1.canCollideCheck(iblockstate1, stopOnLiquid)) {

                            return iblockstate1.collisionRayTrace(mc.world, blockpos, vec31, vec32);
                        } else {
                            raytraceresult2 = new RayTraceResult(RayTraceResult.Type.MISS, vec31, enumfacing, blockpos);
                        }
                    }
                }

                return returnLastUncollidableBlock ? raytraceresult2 : null;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public int getMinArmor(EntityPlayer player) {
        int min = 100;
        for (int j = 0; j < 4; j++) {

            ItemStack stack = player.inventory.armorInventory.get(j);
            int dura = 100 - ( int ) ((1 - (( float ) stack.getMaxDamage() - ( float ) stack.getItemDamage()) / ( float ) stack.getMaxDamage()) * 100);

            if (dura < min) min = dura;
        }
        return min;
    }

    private final DamageSource EXPLOSION_SOURCE = new DamageSource("explosion").setDifficultyScaled().setExplosion();
}
