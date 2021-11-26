package wtf.moneymod.client.impl.module.combat;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;

@Module.Register( label = "FastBow", cat = Module.Category.COMBAT)
public class FastBow extends Module {

    @Value(value = "Ticks") @Bounds(max = 20) public int ticks = 5;

    @Override
    public void onTick(){
        if (mc.player.getHeldItemOffhand().getItem() == Items.BOW || mc.player.getHeldItemMainhand().getItem() == Items.BOW){
            if (mc.player.isHandActive() && mc.player.getItemInUseMaxCount() >= ticks){
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
                mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                mc.player.stopActiveHand();
            }
        }
    }
}