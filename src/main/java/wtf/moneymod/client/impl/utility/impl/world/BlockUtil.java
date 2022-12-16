package wtf.moneymod.client.impl.utility.impl.world;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import wtf.moneymod.client.impl.utility.Globals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum BlockUtil implements Globals {
    INSTANCE;

    private boolean unshift = false;

    public boolean placeBlock(BlockPos pos) {
        Block block = mc.world.getBlockState(pos).getBlock();
        EnumFacing direction = calcSide(pos);
        if (direction == null) {
            return false;
        }
        boolean activated = block.onBlockActivated(mc.world, pos, mc.world.getBlockState(pos), mc.player, EnumHand.MAIN_HAND, direction, 0.0f, 0.0f, 0.0f);
        if (activated) {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
        }
        mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos.offset(direction), direction.getOpposite(), EnumHand.MAIN_HAND, 0.5f, 0.5f, 0.5f));
        mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
        if (activated || unshift) {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            unshift = false;
        }
        mc.playerController.updateController();
        return true;
    }

    public static boolean canPlaceCrystal(BlockPos blockPos, boolean check) {
        return canPlaceCrystal(blockPos, check, true, false);
    }

    public static void placeCrystalOnBlock(BlockPos pos, EnumHand hand, boolean swing) {
        if (pos == null || hand == null) return;
        EnumFacing facing = EnumFacing.UP;
        mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, facing, hand, 0f, 0f, 0f));
        if (swing) {
            mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
        }
        mc.playerController.updateController();
    }

    public static boolean canPlaceCrystal(BlockPos blockPos, boolean check, boolean entity, boolean multiplace) {
        if (BlockUtil.mc.world.getBlockState(blockPos).getBlock() != Blocks.BEDROCK && BlockUtil.mc.world.getBlockState(blockPos).getBlock() != Blocks.OBSIDIAN) {
            return false;
        }
        BlockPos boost = blockPos.add(0, 1, 0);
        if (BlockUtil.mc.world.getBlockState(boost).getBlock() != Blocks.AIR || BlockUtil.mc.world.getBlockState(blockPos.add(0, 2, 0)).getBlock() != Blocks.AIR) {
            return false;
        }
        return !entity || BlockUtil.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost.getX(), boost.getY(), boost.getZ(), boost.getX() + 1, boost.getY() + (check ? 2 : 1), boost.getZ() + 1), e -> !(e instanceof EntityEnderCrystal) || multiplace).size() == 0;
    }

    public static boolean canPlaceCrystal(BlockPos blockPos) {
        BlockPos boost = blockPos.add(0, 1, 0);
        if (BlockUtil.mc.world.getBlockState(boost).getBlock() != Blocks.AIR || BlockUtil.mc.world.getBlockState(blockPos.add(0, 2, 0)).getBlock() != Blocks.AIR) {
            return false;
        }
        return BlockUtil.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost.getX(), boost.getY(), boost.getZ(), boost.getX() + 1, boost.getY() + (2), boost.getZ() + 1), e -> !(e instanceof EntityEnderCrystal)).size() == 0;
    }

    public EnumFacing calcSide(BlockPos pos) {
        for (EnumFacing side : EnumFacing.values()) {
            IBlockState offsetState = mc.world.getBlockState(pos.offset(side));
            boolean activated = offsetState.getBlock().onBlockActivated(mc.world, pos, offsetState, mc.player, EnumHand.MAIN_HAND, side, 0.0f, 0.0f, 0.0f);
            if (activated) {
                mc.getConnection().sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
                unshift = true;
            }
            if (!offsetState.getBlock().canCollideCheck(offsetState, false) || offsetState.getMaterial().isReplaceable())
                continue;
            return side;
        }
        return null;
    }

    public boolean canBlockBeBroken(final BlockPos pos) {
        final IBlockState blockState = mc.world.getBlockState(pos);
        final Block block = blockState.getBlock();
        return block.getBlockHardness(blockState, mc.world, pos) != -1;
    }

    public List<BlockPos> getUnsafePositions(AxisAlignedBB vector, int level, boolean smart) {
        List<BlockPos> blocks = new ArrayList<>();
        if(!smart) {
            getLevels(level).stream().map(vec -> new BlockPos(vector.getCenter().add(vec.x, vec.y, vec.z))).filter(block -> mc.world.getBlockState(block).getMaterial().isReplaceable()).forEach(blocks::add);
        } else {
            int maxX = ( int ) Math.floor(vector.maxX),
                    minX = ( int ) Math.floor(vector.minX),
                    maxZ = ( int ) Math.floor(vector.maxZ),
                    minZ = ( int ) Math.floor(vector.minZ);
            Arrays.asList(new BlockPos(maxX, vector.getCenter().y, maxZ), new BlockPos(maxX, vector.getCenter().y, minZ), new BlockPos(minX, vector.getCenter().y, maxZ), new BlockPos(minX, vector.getCenter().y, minZ)).forEach(bp -> {
                if (!mc.world.getBlockState(bp).getMaterial().isReplaceable()) return;
                getLevels(level).stream().map(vec -> bp.add(vec.x, vec.y, vec.z)).filter(block -> mc.world.getBlockState(block).getMaterial().isReplaceable()).forEach(blocks::add);
            });
        }
        return blocks;
    }

    public List<Vec3d> getLevels(int y) {
        return Arrays.asList(new Vec3d(-1, y, 0), new Vec3d(1, y, 0), new Vec3d(0, y, 1), new Vec3d(0, y, -1));
    }

    //1 - not placeable, 0 - placeable, -1 - not placeable cuz fuckin entity
    public int isPlaceable(BlockPos bp) {
        if (mc.world == null || bp == null) return 1;
        if (!mc.world.getBlockState(bp).getMaterial().isReplaceable()) return 1;
        for (Entity e : mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(bp))) {
            if (e instanceof EntityXPOrb || e instanceof EntityItem) continue;
            if (e instanceof EntityPlayer || e == mc.player) return 1;
            return -1;
        }
        return 0;
    }

    public List<BlockPos> getSphere(float radius, boolean ignoreAir) {
        ArrayList<BlockPos> sphere = new ArrayList<BlockPos>();
        BlockPos pos = new BlockPos(BlockUtil.mc.player.getPositionVector());
        int posX = pos.getX();
        int posY = pos.getY();
        int posZ = pos.getZ();
        int radiuss = ( int ) radius;
        int x = posX - radiuss;
        while (( float ) x <= ( float ) posX + radius) {
            int z = posZ - radiuss;
            while (( float ) z <= ( float ) posZ + radius) {
                int y = posY - radiuss;
                while (( float ) y < ( float ) posY + radius) {
                    if (( float ) ((posX - x) * (posX - x) + (posZ - z) * (posZ - z) + (posY - y) * (posY - y)) < radius * radius) {
                        BlockPos position = new BlockPos(x, y, z);
                        if (!ignoreAir || BlockUtil.mc.world.getBlockState(position).getBlock() != Blocks.AIR) {
                            sphere.add(position);
                        }
                    }
                    ++y;
                }
                ++z;
            }
            ++x;
        }
        return sphere;
    }

}
