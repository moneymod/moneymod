package wtf.moneymod.client.api.events;

import net.minecraft.client.renderer.EnumFaceDirection;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import wtf.moneymod.eventhandler.event.Event;

public class DamageBlockEvent extends Event {

    private BlockPos blockPos;
    private EnumFacing faceDirection;


    public DamageBlockEvent(BlockPos blockPos, EnumFacing faceDirection) {
        this.blockPos = blockPos;
        this.faceDirection = faceDirection;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public EnumFacing getFaceDirection() {
        return faceDirection;
    }

}
