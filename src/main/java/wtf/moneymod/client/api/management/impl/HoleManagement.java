package wtf.moneymod.client.api.management.impl;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import wtf.moneymod.client.api.management.IManager;
import wtf.moneymod.client.impl.utility.Globals;
import wtf.moneymod.client.impl.utility.impl.world.BlockUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HoleManagement extends ArrayList<HoleManagement.Hole> implements IManager<HoleManagement>, Globals {

    public float range = 8;

    @Override public HoleManagement register() {
        if (nullCheck()) return this;
        clear();
        List<BlockPos> blockPoses = BlockUtil.INSTANCE.getSphere(range, false);

        for (BlockPos bp : blockPoses) {
            if (!isHole(bp)) continue;
            add(new Hole(bp, getHoleType(bp)));
        }

        return this;
    }

    private boolean isHole(BlockPos bp) {
        if (mc.world.getBlockState(bp).getBlock() != Blocks.AIR || mc.world.getBlockState(bp.down()).getBlock() == Blocks.AIR || mc.world.getBlockState(bp.up()).getBlock() != Blocks.AIR || mc.world.getBlockState(bp.up(2)).getBlock() != Blocks.AIR)
            return false;
        for (BlockPos pos : convert(bp)) {
            Block block = mc.world.getBlockState(pos).getBlock();
            if (!block.equals(Blocks.BEDROCK) && !block.equals(Blocks.OBSIDIAN)) return false;
        }
        return true;
    }

    private HoleType getHoleType(BlockPos bp) {
        for (BlockPos blockPos : convert(bp)) {
            Block block = mc.world.getBlockState(blockPos).getBlock();
            if (block == Blocks.OBSIDIAN) return HoleType.OBSIDIAN;
        }
        return HoleType.BEDROCK;
    }

    private List<BlockPos> convert(BlockPos bp) {
        return BlockUtil.INSTANCE.getLevels(0).stream().map(vec -> new BlockPos(bp.getX() + vec.x, bp.getY(), bp.getZ() + vec.z)).collect(Collectors.toList());
    }

    public boolean isInHole(BlockPos bp) {
        return stream().anyMatch(block -> block.getBlockPos().toLong() == bp.toLong());
    }

    public class Hole {

        BlockPos blockPos;
        HoleType type;

        public Hole(BlockPos blockPos, HoleType type) {
            this.blockPos = blockPos;
            this.type = type;
        }

        public BlockPos getBlockPos() {
            return blockPos;
        }

        public HoleType getType() {
            return type;
        }

    }

    public enum HoleType {
        BEDROCK,
        OBSIDIAN
    }

}
