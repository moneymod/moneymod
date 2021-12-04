package wtf.moneymod.client.impl.module.combat;

import net.minecraft.init.Items;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.player.ItemUtil;
import wtf.moneymod.client.mixin.mixins.ducks.AccessorKeyBinding;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

@Module.Register( label = "SelfHit", cat = Module.Category.COMBAT )
public class Quiver extends Module {

    @Value(value = "Auto") public boolean auto = false;

    boolean usePressed;

    @Override
    public void onEnable(){
        if (auto) usePressed = true;
        int bow = ItemUtil.findItem(ItemBow.class);
        if (bow != -1) ItemUtil.swapToHotbarSlot(bow, false);
        else setToggled(false);
    }

    @Override
    public void onDisable(){
        usePressed = false;
    }

    @Override public void onTick(){
        if (nullCheck() || mc.player.getHeldItemMainhand().getItem() != Items.BOW){
            setToggled(false);
            return;
        }

        if (usePressed && auto) ((AccessorKeyBinding) mc.gameSettings.keyBindUseItem).setPressed(true);


        if (mc.player.getHeldItemMainhand().getItem() instanceof ItemBow && mc.player.getItemInUseMaxCount() >= 4 && mc.player.isHandActive()){
            Main.getMain().getRotationManagement().set(mc.player.rotationYaw, -90, true);
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            mc.player.stopActiveHand();
            if (auto) {
                usePressed = false;
                ((AccessorKeyBinding) mc.gameSettings.keyBindUseItem).setPressed(false);
                setToggled(false);
            }
        }
    }
}
