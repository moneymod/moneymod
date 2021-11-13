package wtf.moneymod.client.impl.module.combat;

import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.management.impl.FriendManagement;
import wtf.moneymod.client.api.management.impl.HoleManagement;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.math.MathUtil;
import wtf.moneymod.client.impl.utility.impl.player.ItemUtil;
import wtf.moneymod.client.impl.utility.impl.world.BlockUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Module.Register( label = "HoleFiller", cat = Module.Category.COMBAT )
public class HoleFiller extends Module {

    @Value( value = "Target Range" ) @Bounds( min = 1, max = 9 ) public double targetRange = 6;
    @Value( value = "Range" ) @Bounds( min = 1, max = 6 ) public double placeRange = 4.5;
    @Value( value = "Smart Range" ) @Bounds( min = 1, max = 6 ) public double smartRange = 1;
    @Value( value = "Boost Speed" ) @Bounds( min = 1, max = 10 ) public float boostSpeed = 4;
    @Value( value = "BPS" ) @Bounds( min = 1, max = 20 ) public int bps = 8;
    @Value( value = "AutoSwitch" ) public boolean autoSwitch = true;
    @Value( value = "Boost" ) public boolean boost = true;
    @Value( value = "Smart" ) public boolean smart = true;
    @Value( value = "Rotate" ) public boolean rotate = false;

    int placed;

    @Override protected void onToggle() {
        placed = 0;
        if (boost) Main.TICK_TIMER = 1;
    }

    @Override public void onTick() {

        placed = 0;

        if (smart) {
            doSmart(getTargets(targetRange));
        } else {
            List<HoleManagement.Hole> holes = new ArrayList<>();
            for (HoleManagement.Hole hole : Main.getMain().getHoleManagement()) {
                if (Math.sqrt(mc.player.getDistanceSq(hole.getBlockPos())) < placeRange) holes.add(hole);
            }

            if (!holes.isEmpty()) {
                if (boost) Main.TICK_TIMER = boostSpeed;
                int old = mc.player.inventory.currentItem;
                if (autoSwitch) {
                    if (ItemUtil.swapToHotbarSlot(ItemUtil.findItem(BlockObsidian.class), false) == -1) return;
                }

                placeBlocks(holes);

                if (mc.player.inventory.currentItem != old && autoSwitch) ItemUtil.swapToHotbarSlot(old, false);

            } else {
                if (boost) Main.TICK_TIMER = 1;
            }

        }

    }

    void doSmart(List<Entity> targets) {
        if (targets.isEmpty() || Main.getMain().getHoleManagement().isEmpty()) return;
        List<HoleManagement.Hole> holes = new ArrayList<>();
        for (Entity entityPlayer : targets) {
            for (HoleManagement.Hole hole : Main.getMain().getHoleManagement()) {
                if (entityPlayer.getDistanceSq(hole.getBlockPos()) < MathUtil.INSTANCE.square(smartRange) && mc.player.getDistanceSq(hole.getBlockPos()) < MathUtil.INSTANCE.square(placeRange))
                    holes.add(hole);
            }
        }
        if (!holes.isEmpty()) {
            if (boost) Main.TICK_TIMER = boostSpeed;
            int old = mc.player.inventory.currentItem;
            if (ItemUtil.swapToHotbarSlot(ItemUtil.findItem(BlockObsidian.class), false) == -1) return;

            placeBlocks(holes);

            if (mc.player.inventory.currentItem != old) ItemUtil.swapToHotbarSlot(old, false);

        } else {
            if (boost) Main.TICK_TIMER = 1;
        }
    }

    void placeBlocks(List<HoleManagement.Hole> holes) {
        for (HoleManagement.Hole hole : holes) {
            if (placed >= bps) continue;
            switch (BlockUtil.INSTANCE.isPlaceable(hole.getBlockPos())) {
                case 1:
                case -1:
                    break;
                case 0:
                    BlockUtil.INSTANCE.placeBlock(hole.getBlockPos());
                    placed++;
                    break;
            }
        }
    }

    List<Entity> getTargets(double range) {
        return mc.world.loadedEntityList.stream().filter(e -> e instanceof EntityPlayer && e != mc.player && !FriendManagement.getInstance().is(e.getName()) && e.getDistance(mc.player) < range && !e.isDead && (( EntityPlayer ) e).getHealth() > 0).collect(Collectors.toList());
    }

}
