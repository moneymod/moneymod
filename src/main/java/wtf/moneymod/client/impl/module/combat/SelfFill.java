package wtf.moneymod.client.impl.module.combat;

import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.player.ItemUtil;
import wtf.moneymod.client.impl.utility.impl.world.BlockUtil;

import java.util.Arrays;
import java.util.List;

@Module.Register( label = "SelfFill", cat = Module.Category.COMBAT )
public class SelfFill extends Module {

    //pig pig pig

    @Value(value = "Height") @Bounds(min = -8, max = 8) public int height = 4;
    @Value(value = "Rotate") public boolean rotate = false;
    @Value(value = "Packet") public boolean packet = false;

    private final List<Double> offsets = Arrays.asList( 0.4199999, 0.7531999, 1.0013359, 1.1661092 );
    private BlockPos startPos;
    int tick = 0;
    boolean fill = false;

    private boolean check( ) {
        return mc.player.onGround && mc.world.getBlockState( startPos ).getBlock( ) == Blocks.AIR && mc.world.getBlockState( startPos.add( 0, 3, 0 ) ).getBlock( ) == Blocks.AIR;
    }

    @Override
    public void onEnable(){
        fill = true;
    }

    @Override
    public void onTick() {
        int startSlot = mc.player.inventory.currentItem;
        startPos = new BlockPos(mc.player.getPositionVector());
        if (ItemUtil.findItem(Blocks.ENDER_CHEST, Blocks.OBSIDIAN, Blocks.CHEST) == -1) {
            setToggled(false);
            tick = 0;
            return;
        }
        if (!check()) return;
        tick++;
        if (fill) {
            ItemUtil.swapToHotbarSlot(ItemUtil.findItem(Blocks.ENDER_CHEST, Blocks.OBSIDIAN, Blocks.CHEST), false);
            offsets.forEach(offset -> mc.getConnection().sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + offset, mc.player.posZ, true)));
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
            Main.getMain().getRotationManagement().look(startPos,packet);
            BlockUtil.INSTANCE.placeBlock(startPos);
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + height, mc.player.posZ, false));
            ItemUtil.swapToHotbarSlot(startSlot, false);
            fill = false;
        }
        if (tick >= 8) {
            tick = 0;
            setToggled(false);
        }
    }
}
