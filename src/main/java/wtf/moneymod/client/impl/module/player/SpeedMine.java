package wtf.moneymod.client.impl.module.player;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import wtf.moneymod.client.api.events.DamageBlockEvent;
import wtf.moneymod.client.api.events.MoveEvent;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.math.MathUtil;
import wtf.moneymod.client.impl.utility.impl.misc.Timer;
import wtf.moneymod.client.impl.utility.impl.player.ItemUtil;
import wtf.moneymod.client.impl.utility.impl.player.ToolUtil;
import wtf.moneymod.client.impl.utility.impl.render.Renderer3D;
import wtf.moneymod.client.impl.utility.impl.world.BlockUtil;
import wtf.moneymod.client.mixin.accessors.IPlayerControllerMP;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

import java.awt.*;

@Module.Register( label = "SpeedMine", cat = Module.Category.PLAYER )
public class SpeedMine extends Module {

    @Value( value = "Renderer" ) public RenderMode mode = RenderMode.FADE;
    @Value( value = "Render" ) public boolean render = true;
    @Value( "AutoSwitch" ) public boolean autoSwitch = true;
    @Value( value = "Silent" ) public boolean silent = true;
    @Value( value = "Instant Rebreak" ) public boolean instant = true;
    @Value( "Strict Rebreak" ) public boolean strict = true;
    @Value( "Rebreak Attempts" ) @Bounds( max = 20 ) public int attempts = 8;
    @Value( value = "Range" ) @Bounds( min = 4, max = 30 ) public int range = 16;
    @Value( value = "Speed" ) @Bounds( min = 0.1f, max = 1 ) public float speed = 0.8f;
    @Value( value = "Packet Spam" ) @Bounds( min = 1, max = 10 ) public int spam = 1;
    public Color color = new Color(255, 0, 0, 75);
    public Color readyColor = new Color(0, 255, 0, 75);

    private BlockPos currentPos;
    private final Timer timer = new Timer();
    private long start;
    private int old, delay, rebreakCount;
    public boolean swap = false, checked, strictCheck;

    @Override protected void onToggle() {
        rebreakCount = 0;
        old = -1;
        currentPos = null;
        delay = 0;
    }

    @Override public void onTick() {
        //this swap code is sooo trash
        if (swap) {
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, currentPos, EnumFacing.DOWN));
            if (delay >= 2) {
                if (old != -1) {
                    ItemUtil.swapToHotbarSlot(old, false);
                }
                swap = false;
                if (!instant && currentPos != null) currentPos = null;
                delay = 0;
            }
            delay++;
        }
    }

    @Handler public Listener<MoveEvent> moveEventListener = new Listener<>(MoveEvent.class, e -> {
        if (currentPos != null) {
            if (instant) {
                if (mc.world.getBlockState(currentPos).getBlock() == Blocks.AIR) {
                    if (!checked) {
                        rebreakCount = 0;
                        checked = true;
                        start = System.currentTimeMillis();
                        timer.reset();
                        strictCheck = false;
                    }
                } else {
                    if (strict && !strictCheck) {
                        Block block = mc.world.getBlockState(currentPos).getBlock();
                        if (!(block.equals(Blocks.ENDER_CHEST) || block.equals(Blocks.ANVIL) || block.equals(Blocks.AIR))) {
                            rebreakCount = 0;
                            currentPos = null;
                            timer.reset();
                            strictCheck = true;
                            return;
                        }
                    }
                    checked = false;
                }

            }

            if (getBlockProgress(currentPos, mc.player.inventory.getStackInSlot(ToolUtil.bestSlot(currentPos)), start) <= 1 - speed && mc.world.getBlockState(currentPos).getBlock() != Blocks.AIR) {

                if (autoSwitch) {
                    if(!swapTo()) return;
                }

            }

            if (!swap) old = mc.player.inventory.currentItem;

            if (currentPos != null && mc.player.getDistanceSq(currentPos) >= MathUtil.INSTANCE.square(range)) {
                currentPos = null;
            }
        }
        try {
            (( IPlayerControllerMP ) mc.playerController).setBlockHitDelay(0);
        } catch (Exception ignored) {}

    });

    @Handler public Listener<DamageBlockEvent> damageBlockEvent = new Listener<>(DamageBlockEvent.class, e -> {
        if (swap) {
            e.setCancelled(true);
            return;
        }

        if (nullCheck() || !BlockUtil.INSTANCE.canBlockBeBroken(e.getBlockPos())) return;

        if (currentPos != null) {

            if (e.getBlockPos().toLong() == currentPos.toLong()) {
                if (!swap && getBlockProgress(currentPos, mc.player.inventory.getStackInSlot(ToolUtil.bestSlot(currentPos)), start) <= 1 - speed && mc.world.getBlockState(currentPos).getBlock() != Blocks.AIR) {
                    if (silent) swapTo();
                    mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, currentPos, EnumFacing.DOWN));
                    e.cancel();
                }
                return;
            }

            if (e.getBlockPos().toLong() != currentPos.toLong()) {
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, currentPos, e.getFaceDirection()));
                (( IPlayerControllerMP ) mc.playerController).setIsHittingBlock(false);
            }

        }

        mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
        for (int j = 0; j < spam; j++) {
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, e.getBlockPos(), e.getFaceDirection()));
        }
        mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, e.getBlockPos(), EnumFacing.DOWN));
        currentPos = e.getBlockPos();
        start = System.currentTimeMillis();
        strictCheck = true;
        timer.reset();
        e.setCancelled(true);
    });

    @Override public void onRender3D(float partialTicks) {
        if (nullCheck() || currentPos == null || !render || mc.world.getBlockState(currentPos).getBlock() == Blocks.AIR || mc.world.getBlockState(currentPos).getBlock() instanceof BlockLiquid)
            return;
        AxisAlignedBB bb = mc.world.getBlockState(currentPos).getSelectedBoundingBox(mc.world, currentPos);
        float progress = getBlockProgress(currentPos, mc.player.inventory.getStackInSlot(ToolUtil.bestSlot(currentPos)), start);
        if (progress <= 1 - speed) {
            Renderer3D.drawBoxESP(bb, readyColor, 1f, true, true, readyColor.getAlpha(), 255);
        } else {
            if (mode == RenderMode.FADE) {
                Renderer3D.drawBoxESP(bb, new Color(( int ) (color.getRed() * progress), ( int ) (readyColor.getGreen() * (1 - progress)), color.getBlue()), 1f, true, true, color.getAlpha(), 255);
            } else {
                Renderer3D.INSTANCE.drawProgressBox(bb, progress, color);
            }
        }
    }

    float getBlockProgress(BlockPos blockPos, ItemStack stack, long start) {
        return ( float ) MathUtil.INSTANCE.clamp(1 - ((System.currentTimeMillis() - start) / ( double ) ToolUtil.INSTANCE.time(blockPos, stack)), 0, 1);
    }

    boolean swapTo() {
        if (rebreakCount > attempts - 1 && attempts != 0) {
            currentPos = null;
            rebreakCount = 0;
            return false;
        }
        ItemUtil.swapToHotbarSlot(ToolUtil.bestSlot(currentPos), false);
        if (silent) {
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, currentPos, EnumFacing.DOWN));
            rebreakCount++;
            if (!instant) currentPos = null;
            ItemUtil.swapToHotbarSlot(old, false);
        } else {
            swap = true;
        }
        return true;
    }

    public BlockPos getCurrentPos() {
        return currentPos;
    }

    public enum RenderMode {
        FADE,
        EXPAND
    }

}
