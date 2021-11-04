package wtf.moneymod.client.impl.module.player;

import club.cafedevelopment.reflectionsettings.annotation.Clamp;
import club.cafedevelopment.reflectionsettings.annotation.Setting;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wtf.moneymod.client.api.events.BreakBlockEvent;
import wtf.moneymod.client.api.events.DamageBlockEvent;
import wtf.moneymod.client.api.management.impl.PacketManagement;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.math.MathUtil;
import wtf.moneymod.client.impl.utility.impl.misc.Timer;
import wtf.moneymod.client.impl.utility.impl.player.ItemUtil;
import wtf.moneymod.client.impl.utility.impl.player.ToolUtil;
import wtf.moneymod.client.impl.utility.impl.render.JColor;
import wtf.moneymod.client.impl.utility.impl.render.Renderer3D;
import wtf.moneymod.client.impl.utility.impl.world.BlockUtil;
import wtf.moneymod.client.mixin.accessors.IPlayerControllerMP;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

@Module.Register( label = "SpeedMine", cat = Module.Category.PLAYER )
public class SpeedMine extends Module {

    @Setting( id = "Render" ) public boolean render = true;
    @Setting( id = "Silent" ) public boolean silent = true;
    @Setting( id = "Instant Rebreak" ) public boolean instant = true;
    @Setting( id = "Range", clamp = @Clamp( min = 4, max = 30 ) ) public int range = 16;
    @Setting( id = "Packet Spam", clamp = @Clamp( min = 1, max = 10 ) ) public int spam = 1;
    @Setting( id = "Color" ) public JColor color = new JColor(255, 0, 0, 160, false);
    @Setting( id = "Ready Color" ) public JColor readyColor = new JColor(0, 255, 0, 160, false);

    private BlockPos currentPos;
    private final Timer timer = new Timer();
    private long start;
    private int old, delay;
    private boolean swap;

    @Override protected void onToggle() {
        old = -1;
        currentPos = null;
        delay = 0;
    }

    @Override public void onTick() {
        //this swap code is sooo trash
        if (swap) {
            if (delay >= 5) {
                if (old != -1) {
                    ItemUtil.switchToHotbarSlot(old, false);
                }
                swap = false;
                delay = 0;
            }
            delay++;
        }

        if (currentPos != null) {
            if (mc.player.inventory.currentItem == ToolUtil.INSTANCE.bestSlot(currentPos) && getBlockProgress(currentPos, mc.player.inventory.getStackInSlot(ToolUtil.INSTANCE.bestSlot(currentPos)), start) <= 0.1 && mc.world.getBlockState(currentPos).getBlock() != Blocks.AIR) {
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, currentPos, EnumFacing.DOWN));
            }

            if (!swap) old = mc.player.inventory.currentItem;

            if (mc.player.getDistanceSq(currentPos) >= MathUtil.INSTANCE.square(range)) {
                currentPos = null;
            }
        }
        try {
            (( IPlayerControllerMP ) mc.playerController).setBlockHitDelay(0);
        } catch (Exception ignored) {}

    }

    @Handler public Listener<DamageBlockEvent> damageBlockEvent = new Listener<>(DamageBlockEvent.class, e -> {
        if (nullCheck() || !BlockUtil.INSTANCE.canBlockBeBroken(e.getBlockPos())) return;

        if (currentPos != null) {
            if (e.getBlockPos().toLong() == currentPos.toLong() && !swap && getBlockProgress(currentPos, mc.player.inventory.getStackInSlot(ToolUtil.INSTANCE.bestSlot(currentPos)), start) <= 0.1 && mc.world.getBlockState(currentPos).getBlock() != Blocks.AIR) {
                ItemUtil.switchToHotbarSlot(ToolUtil.INSTANCE.bestSlot(currentPos), silent);
                swap = true;
                e.cancel();
                return;
            }
        }

        if (swap) {
            e.cancel();
            return;
        }
        mc.player.swingArm(EnumHand.MAIN_HAND);
        for (int j = 0; j < spam; j++) {
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, e.getBlockPos(), e.getFaceDirection()));
        }
        mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, e.getBlockPos(), EnumFacing.DOWN));
        currentPos = e.getBlockPos();
        start = System.currentTimeMillis();
        timer.reset();
        e.setCancelled(true);
    });

    @Handler public Listener<BreakBlockEvent> breakBlockEvent = new Listener<>(BreakBlockEvent.class, e -> {
        if (nullCheck() || currentPos == null || e.getBlockPos().toLong() != currentPos.toLong() || !instant) return;
        start = System.currentTimeMillis();
        timer.reset();
    });

    @SubscribeEvent public void onRender(RenderWorldLastEvent event) {
        if (currentPos == null || !render || mc.world.getBlockState(currentPos).getBlock() == Blocks.AIR) return;
        float progress = getBlockProgress(currentPos, mc.player.inventory.getStackInSlot(ToolUtil.INSTANCE.bestSlot(currentPos)), start);
        if (progress <= 0.1) {
            Renderer3D.drawBoxESP(new AxisAlignedBB(currentPos), readyColor.getColor(), 1f, true, true, readyColor.getColor().getAlpha(), 255);
        } else {
            Renderer3D.INSTANCE.drawProgressBox(new AxisAlignedBB(currentPos), progress, color.getColor());
        }
    }

    float getBlockProgress(BlockPos blockPos, ItemStack stack, long start) {
        return ( float ) MathUtil.INSTANCE.clamp(1 - ((System.currentTimeMillis() - start) / ( double ) ToolUtil.INSTANCE.time(blockPos, stack)), 0, 1);
    }

}
