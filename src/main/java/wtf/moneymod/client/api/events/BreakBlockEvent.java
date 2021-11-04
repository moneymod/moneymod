package wtf.moneymod.client.api.events;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wtf.moneymod.eventhandler.event.Event;

public class BreakBlockEvent extends Event {

    private World world;
    private EntityPlayer player;
    private BlockPos blockPos;
    private IBlockState blockState;

    public BreakBlockEvent(World world, EntityPlayer player, BlockPos blockPos, IBlockState blockState) {
        this.world = world;
        this.player = player;
        this.blockPos = blockPos;
        this.blockState = blockState;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    public IBlockState getBlockState() {
        return blockState;
    }

    public World getWorld() {
        return world;
    }

}
