package wtf.moneymod.client.impl.module.misc;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.input.Keyboard;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.module.player.AutoMine;
import wtf.moneymod.client.impl.module.player.SpeedMine;
import wtf.moneymod.client.impl.utility.impl.math.MathUtil;
import wtf.moneymod.client.impl.utility.impl.misc.Timer;
import wtf.moneymod.client.impl.utility.impl.player.ItemUtil;
import wtf.moneymod.client.impl.utility.impl.player.ToolUtil;
import wtf.moneymod.client.impl.utility.impl.world.BlockUtil;
import wtf.moneymod.client.impl.utility.impl.world.ChatUtil;
import wtf.moneymod.client.impl.utility.impl.world.EntityUtil;

@Module.Register( label = "AnvilBreaker", cat = Module.Category.MISC)
public class AnvilBreaker extends Module {

    @Value(value = "Range") @Bounds(max = 8) public int range = 6;
    @Value(value = "Only Burrow") public boolean onlyBurrow = true;

    BlockPos breakPos;
    @Override
    public void onToggle(){
        breakPos = null;
    }
    boolean hit = true;
    @Override public void onTick() {
        if (nullCheck()) return;
        //TODO: nado silent switch
        if (onlyBurrow) {
            mc.world.getLoadedEntityList().forEach(e -> {
                if (e.getDistance(mc.player) < range) {
                    if (mc.world.getBlockState(new BlockPos(e.posX, e.posY, e.posZ)).getBlock() == Blocks.ANVIL || mc.world.getBlockState(new BlockPos(e.posX, e.posY, e.posZ)).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(new BlockPos(e.posX, e.posY, e.posZ)).getBlock() == Blocks.ENDER_CHEST) {
                        breakPos = new BlockPos(e.posX, e.posY, e.posZ);
                        SpeedMine speedMine = (SpeedMine) Main.getMain().getModuleManager().get(SpeedMine.class);
                        if(Main.getMain().getModuleManager().get(SpeedMine.class).isToggled() && (speedMine.getCurrentPos() == null || speedMine.getCurrentPos().toLong() != breakPos.toLong())) {
                            mc.playerController.clickBlock(breakPos, EnumFacing.DOWN);
                        }
                        mc.player.swingArm(EnumHand.MAIN_HAND);
                    }
                }
            });
        }
    }
}
