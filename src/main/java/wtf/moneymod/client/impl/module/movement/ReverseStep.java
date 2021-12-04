package wtf.moneymod.client.impl.module.movement;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.world.EntityUtil;

@Module.Register( label = "ReverseStep", cat = Module.Category.MOVEMENT )
public class ReverseStep extends Module {

    @Override public void onTick() {
        if (!mc.player.onGround || mc.player.isInWater() || mc.player.isInWater() || !EntityUtil.INSTANCE.isMoving(mc.player) || mc.player.collidedHorizontally) return;

        System.out.println(mc.player.moveStrafing + " : " + mc.player.moveForward);

        BlockPos playerPos = new BlockPos(mc.player.getPositionVector());

        if (mc.world.getBlockState(playerPos.down()).getBlock().equals(Blocks.AIR)) {
            for (int j = 1; j < 6; j++) {
                BlockPos pos = playerPos.down(j);
                if (!mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR)) {
                    mc.player.setPosition(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
                    break;
                }
            }
        }

    }

}
