package wtf.moneymod.client.impl.module.combat;

import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.events.StepEvent;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.misc.Timer;
import wtf.moneymod.client.impl.utility.impl.player.ItemUtil;
import wtf.moneymod.client.impl.utility.impl.world.BlockUtil;
import wtf.moneymod.eventhandler.event.enums.Era;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

import java.util.HashMap;
import java.util.List;

@Module.Register( label = "FeetPlace", cat = Module.Category.COMBAT )
public class FeetPlace extends Module {

    @Value( value = "Delay" ) @Bounds( max = 250 ) public int delay = 50;
    @Value( value = "BPS" ) @Bounds( max = 20, min = 1 ) public int bps = 8;
    @Value( value = "Retry" ) public boolean retry = true;
    @Value( value = "Retries" ) @Bounds( max = 25, min = 1 ) public int retries = 5;
    @Value( value = "Cleaner" ) public boolean cleaner = true;
    @Value( value = "Helping Blocks" ) public boolean help = true;
    @Value( value = "Jump Disable" ) public boolean jumpDisable = true;
    @Value( value = "Disable" ) public boolean disable = false;
    @Value( value = "Rotate" ) public boolean rotate = false;
    @Value( value = "AutoCenter" ) public boolean center = false;
    @Value( "Smart" ) public boolean smart = true;

    private final Timer timer = new Timer();
    private int placed;
    public boolean didPlace;
    private double y;
    private HashMap<BlockPos, Integer> retriesCount = new HashMap<>();

    @Override protected void onEnable() {
        placed = 0;
        y = mc.player.posY;
        rotate = false;
        didPlace = false;
        timer.reset();
        retriesCount.clear();
        BlockPos s = new BlockPos(mc.player.getPositionVector());
        if (center) {
            mc.player.setPosition(s.getX() + 0.5f, mc.player.posY, s.getZ() + 0.5f);
        }
    }

    @Handler public Listener<StepEvent> onStep = new Listener<>(StepEvent.class, e -> {
        if (e.getEra() == Era.PRE && jumpDisable && mc.player.stepHeight > 1) {
            setToggled(false);
        }
    });

    @Override public void onTick() {
        if (nullCheck()) return;
        doFeetPlace();
    }

    private void doFeetPlace() {
        if ((mc.player.posY != y || mc.player.motionY > 0) && jumpDisable) {
            setToggled(false);
            return;
        }
        if (!this.timer.passed(delay) && didPlace) return;
        if (Main.getMain().getHoleManagement().isInHole(new BlockPos(mc.player.getPositionVector())) || BlockUtil.INSTANCE.getUnsafePositions(mc.player.getEntityBoundingBox(), 0, smart).size() == 0) {
            if (disable) setToggled(false);
            return;
        }
        if (help) {
            placeBlocks(BlockUtil.INSTANCE.getUnsafePositions(mc.player.getEntityBoundingBox(), -1, smart));
        }
        placeBlocks(BlockUtil.INSTANCE.getUnsafePositions(mc.player.getEntityBoundingBox(), 0, smart));
        placed = 0;
        timer.reset();
    }

    private void placeBlocks(List<BlockPos> blocks) {
        for (BlockPos bp : blocks) {
            if (placed >= bps) return;
            int old = mc.player.inventory.currentItem;
            if (ItemUtil.swapToHotbarSlot(ItemUtil.findItem(BlockObsidian.class), false) == -1) {
                if (ItemUtil.swapToHotbarSlot(ItemUtil.findItem(BlockEnderChest.class), false) == -1) return;
            }
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
                        if (retriesCount.get(bp) > retries) break;
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
                ItemUtil.swapToHotbarSlot(old, false);
            }
        }
    }

}
