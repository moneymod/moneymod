package wtf.moneymod.client.impl.module.combat;

import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.events.PacketEvent;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.misc.Timer;
import wtf.moneymod.client.impl.utility.impl.player.ItemUtil;
import wtf.moneymod.client.impl.utility.impl.world.BlockUtil;
import wtf.moneymod.client.mixin.mixins.ducks.AccessorCPacketPlayer;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Module.Register(label = "SelfPlace", cat = Module.Category.COMBAT)
public class SelfPlace extends Module {

    @Value(value = "Bps") @Bounds(min = 1, max = 20) public int bps = 8;
    @Value(value = "Delay") @Bounds(min = 1, max = 250) public int delay = 0;
    @Value(value = "Disable") public boolean disable = true;
    @Value(value = "Rotate") public boolean rotate = false;

    final Timer timer = new Timer();
    boolean didPlace, rotating;
    int placed;
    float yaw = 0f, pitch = 0f;

    @Override
    protected void onEnable() {
        placed = 0;
        didPlace = false;
        timer.reset();
        rotating = false;
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;
        if (!timer.passed(delay) && didPlace) return;
        if (getBlocks(mc.player).size() == 0) {
            if (disable) setToggled(false);
            rotating = false;
            return;
        }
        placeBlocks(getBlocks(mc.player));
        placed = 0;
        timer.reset();
    }

    List<BlockPos> getBlocks(Entity target) {
        AtomicBoolean add = new AtomicBoolean(false);
        List<BlockPos> blocks = Stream.of(new BlockPos(target.posX, target.posY + 2.0, target.posZ)).filter(blockPos -> {
            switch (BlockUtil.INSTANCE.isPlaceable(blockPos)) {
                case 0: {
                    if (BlockUtil.INSTANCE.calcSide(blockPos) == null) {
                        add.set(true);
                    }
                    return true;
                }
                case -1:
                case 1: {
                    return false;
                }
            }
            return false;
        }).collect(Collectors.toList());
        if (add.get()) {
            for (int j = 0; j < 3; j++) blocks.add(new BlockPos(target.posX + 1f, target.posY + j, target.posZ));
        }
        return blocks;
    }

    void placeBlocks(List<BlockPos> blockPos) {
        rotating = true;
        for (BlockPos bp : blockPos) {
            if (placed >= bps) return;
            int old = mc.player.inventory.currentItem;
            if (ItemUtil.swapToHotbarSlot(ItemUtil.findItem(BlockObsidian.class), false) == -1)
                return;
            switch (BlockUtil.INSTANCE.isPlaceable(bp)) {
                case 0: {
                    yaw = Main.getMain().getRotationManagement().look(bp, false, false)[0];
                    pitch = Main.getMain().getRotationManagement().look(bp, false, false)[1];
                    BlockUtil.INSTANCE.placeBlock(bp);
                    didPlace = true;
                    placed++;
                    break;
                }
                case -1:
                case 1: {
                    break;
                }
            }
            ItemUtil.swapToHotbarSlot(old, false);
        }
    }
    @Handler
    public Listener<PacketEvent.Receive> packetEventReceive = new Listener<>(PacketEvent.Receive.class, e -> {
        if (e.getPacket() instanceof CPacketPlayer && rotating && rotate) {
            ((AccessorCPacketPlayer) e.getPacket()).setYaw(yaw);
            ((AccessorCPacketPlayer) e.getPacket()).setPitch(pitch);
        }
    });
}
