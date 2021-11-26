package wtf.moneymod.client.impl.module.combat;

import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.events.DisconnectEvent;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.module.movement.Speed;
import wtf.moneymod.client.impl.module.player.SpeedMine;
import wtf.moneymod.client.impl.utility.impl.misc.Timer;
import wtf.moneymod.client.impl.utility.impl.player.ItemUtil;
import wtf.moneymod.client.impl.utility.impl.render.JColor;
import wtf.moneymod.client.impl.utility.impl.render.Renderer3D;
import wtf.moneymod.client.impl.utility.impl.world.BlockUtil;
import wtf.moneymod.client.impl.utility.impl.world.EntityUtil;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

import java.util.Comparator;
import java.util.HashMap;

@Module.Register(label = "AntiSurround", cat = Module.Category.COMBAT)
public class AntiSurround extends Module {

    @Value(value = "Range") @Bounds(max = 7) public int range = 5;
    @Value(value = "Delay") @Bounds(max = 50) public int delay = 0;
    @Value(value = "Silent") public boolean silentSwitch = true;
    @Value(value = "Protect") public boolean protect = true;
    @Value(value = "Color") public JColor color = new JColor(255, 0, 0, 100, false);

    private HashMap<BlockPos, Vec3d> lole = new HashMap<>();
    private Timer timer = new Timer();
    private Entity crystal;
    private EntityPlayer target;
    private BlockPos targetBlock;
    boolean digging;
    int step, action = 0;

    @Handler
    public Listener<DisconnectEvent> disconnectEventListener = new Listener<>(DisconnectEvent.class, e -> {
        setToggled(false);
    });


    public void onRender3D(float partialTicks) {
        if (targetBlock != null)
            Renderer3D.drawBoxESP(targetBlock, color.getColor(), 1f, true, true, color.getColor().getAlpha(), color.getColor().getAlpha(), 1);
    }

    @Override
    public void onDisable() {
        if (protect) {
            if (action == 2) {
                Main.getMain().getModuleManager().get(AutoCrystal.class).setToggled(true);
                action = 0;
            }
        }

    }

    @Override
    protected void onEnable() {
        step = 0;
        crystal = null;
        target = null;
        targetBlock = null;
        digging = false;
        lole.clear();
    }

    @Override
    public void onTick() {
        if (nullCheck()) {
            if (protect) {
                setToggled(false);
                Main.getMain().getModuleManager().get(AutoCrystal.class).setToggled(false);
            } else return;
        }
        if (protect) {
            if (Main.getMain().getModuleManager().get(AutoTrap.class).isToggled()) {
                return;
            }
        }


        if (Main.getMain().getModuleManager().get(AutoCrystal.class).isToggled()) {
            Main.getMain().getModuleManager().get(AutoCrystal.class).setToggled(false);
            action = 2;

        }

        target = EntityUtil.getTarget(range);
        if (target != null) {
            if (targetBlock == null) {
                targetBlock = BlockUtil.INSTANCE.getLevels(0).stream().map(vec -> {
                    BlockPos lambdaPos = new BlockPos(target.getPositionVector()).add(vec.x, vec.y, vec.z);
                    lole.put(lambdaPos, vec);
                    return lambdaPos;
                }).filter(bp -> {
                    BlockPos crystalPos = new BlockPos(bp).add(lole.get(bp).x, -1, lole.get(bp).z);
                    return mc.world.getBlockState(bp).getBlock() != Blocks.BEDROCK && BlockUtil.canPlaceCrystal(crystalPos, true, false);
                }).min(Comparator.comparing(bp -> mc.player.getDistanceSq(bp))).orElse(null);
                return;
            }
            IBlockState state = mc.world.getBlockState(targetBlock);

            int old = mc.player.inventory.currentItem;
            if (step == 0) {
                if (BlockUtil.canPlaceCrystal(new BlockPos(targetBlock).add(lole.get(targetBlock).x, -1, lole.get(targetBlock).z), true) && ItemUtil.swapToHotbarSlot(ItemUtil.findItem(ItemEndCrystal.class), false) != -1) {
                    BlockUtil.placeCrystalOnBlock(new BlockPos(targetBlock).add(lole.get(targetBlock).x, -1, lole.get(targetBlock).z), EnumHand.MAIN_HAND, true);
                    crystal = mc.world.loadedEntityList.stream().filter(EntityEnderCrystal.class::isInstance).min(Comparator.comparing(c -> mc.player.getDistance(c))).orElse(null);
                    ItemUtil.swapToHotbarSlot(old, false);
                }
                if (!(state.getBlock() instanceof BlockAir)) {
                    if (mc.player.getHeldItemMainhand().getItem().equals(Items.DIAMOND_PICKAXE) || ItemUtil.swapToHotbarSlot(ItemUtil.findItem(ItemPickaxe.class), false) != -1) {
                        if (!digging) {
                            digging = true;
                            SpeedMine speedMine = (SpeedMine) Main.getMain().getModuleManager().get(SpeedMine.class);
                            if(Main.getMain().getModuleManager().get(SpeedMine.class).isToggled() && (speedMine.getCurrentPos() == null || speedMine.getCurrentPos().toLong() != targetBlock.toLong())) {
                                mc.playerController.clickBlock(targetBlock, EnumFacing.DOWN);
                            } else {
                                mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
                                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, targetBlock, EnumFacing.DOWN));
                                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, targetBlock, EnumFacing.DOWN));
                            }
                        }
                    }
                } else {
                    digging = false;
                    step = 1;
                }
            } else if (step == 1) {
                if (timer.passed(delay)) {
                    if (crystal != null) mc.player.connection.sendPacket(new CPacketUseEntity(crystal));
                    if (ItemUtil.swapToHotbarSlot(ItemUtil.findItem(Blocks.ANVIL), false) != -1) {
                        BlockUtil.INSTANCE.placeBlock(targetBlock);
                        ItemUtil.swapToHotbarSlot(ItemUtil.getItemSlot(Items.DIAMOND_PICKAXE), false);
                        step = 0;
                    }
                    timer.reset();
                }
            }
            if (silentSwitch) ItemUtil.swapToHotbarSlot(old, false);
        }
    }


}
