package wtf.moneymod.client.impl.module.combat;

import club.cafedevelopment.reflectionsettings.annotation.Clamp;
import club.cafedevelopment.reflectionsettings.annotation.Setting;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.misc.Timer;
import wtf.moneymod.client.impl.utility.impl.player.ItemUtil;
import wtf.moneymod.client.impl.utility.impl.world.BlockUtil;

import java.util.HashMap;
import java.util.List;

@Module.Register( label = "FeetPlace", cat = Module.Category.COMBAT )
public class FeetPlace extends Module {

    @Setting( id = "Delay", clamp = @Clamp( max = 250, min = 0 ) ) public int delay = 50;
    @Setting( id = "BPS", clamp = @Clamp( max = 20, min = 1 ) ) public int bps = 8;
    @Setting( id = "Retry" ) public boolean retry = true;
    @Setting( id = "Retries", clamp = @Clamp( max = 25, min = 1 ) ) public int retries = 5;
    @Setting( id = "Cleaner" ) public boolean cleaner = true;
    @Setting( id = "Help" ) public boolean help = true;
    @Setting( id = "Jump Disable" ) public boolean jumpDisable = true;
    @Setting( id = "Disable" ) public boolean disable = false;
    @Setting( id = "AutoCenter" ) public boolean center = false;

    private final Timer timer = new Timer();
    private int placed;
    public boolean didPlace;
    private double y;
    private HashMap<BlockPos, Integer> retriesCount = new HashMap<>();

    @Override protected void onEnable() {
        placed = 0;
        y = mc.player.posY;
        didPlace = false;
        timer.reset();
        retriesCount.clear();
    }

    @Override public void onTick() {
        if (nullCheck()) return;
        doFeetPlace();
    }

    private void doFeetPlace() {
        if (!this.timer.passed(delay) && didPlace) return;
        if (mc.player.posY != y && jumpDisable) setToggled(false);
        int offset = (mc.world.getBlockState(new BlockPos(mc.player.getPositionVector())).getBlock() == Blocks.ENDER_CHEST && mc.player.posY - Math.floor(mc.player.posY) > 0.5 ? 1 : 0);
        if (BlockUtil.INSTANCE.getUnsafePositions(mc.player.getPositionVector(), offset).size() == 0) {
            if (disable) setToggled(false);
            return;
        }
        if (help) {
            placeBlocks(BlockUtil.INSTANCE.getUnsafePositions(mc.player.getPositionVector(), offset - 1));
        }
        placeBlocks(BlockUtil.INSTANCE.getUnsafePositions(mc.player.getPositionVector(), offset));
        placed = 0;
        timer.reset();
    }

    private void placeBlocks(List<BlockPos> blocks) {
        for (BlockPos bp : blocks) {
            if (placed >= bps) return;
            int old = mc.player.inventory.currentItem;
            if (ItemUtil.switchToHotbarSlot(ItemUtil.findHotbarBlock(BlockObsidian.class), false) == -1)
                return;
            switch (BlockUtil.INSTANCE.isPlaceable(bp)) {
                case 0: {
                    BlockUtil.INSTANCE.placeBlock(bp);
                    didPlace = true;
                    placed++;
                    break;
                }
                case -1: {
                    if (retry) {
                        retriesCount.putIfAbsent(bp, 0);
                        if (retriesCount.get(bp) > retries) return;
                        System.out.println(retriesCount.get(bp));
                        if (cleaner) {
                            for (Entity e : mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(bp))) {
                                if (e instanceof EntityEnderCrystal)
                                    mc.player.connection.sendPacket(new CPacketUseEntity(e));
                            }
                        }
                        BlockUtil.INSTANCE.placeBlock(bp);
                        retriesCount.replace(bp, retriesCount.get(bp) + 1);
                        placed++;
                    }
                    break;
                }
                case 1: {
                    break;
                }
            }
            if (mc.player.inventory.currentItem != old) {
                ItemUtil.switchToHotbarSlot(old, false);
            }
        }
    }


}
