package wtf.moneymod.client.impl.module.misc;

import akka.io.TcpListener;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.util.EnumHand;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import wtf.moneymod.client.api.management.impl.FriendManagement;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.player.ItemUtil;
import wtf.moneymod.client.impl.utility.impl.world.ChatUtil;
import wtf.moneymod.client.impl.utility.impl.world.EntityUtil;

@Module.Register( label = "MiddleClick", cat = Module.Category.MISC)
public class MiddleClick extends Module {

    @Value(value = "Mode") public Mode mode = Mode.FRIEND;
    @Value(value = "Silent") public boolean silent = true;
    boolean check = false;

    @Override public void onTick(){
        if (Mouse.isButtonDown(2)){
            if (!check) {
                if (mode == Mode.FRIEND) {

                    if (mc.objectMouseOver.entityHit instanceof EntityPlayer) {
                        String name = mc.objectMouseOver.entityHit.getName();
                        if (FriendManagement.getInstance().is(name)) {
                            ChatUtil.INSTANCE.sendMessage(ChatFormatting.GRAY + "friend " + ChatFormatting.WHITE + "> " + name + " " + ChatFormatting.GREEN + "added");
                            FriendManagement.getInstance().remove(name);
                            check = true;
                            return;
                        } else {
                            FriendManagement.getInstance().add(name);
                            ChatUtil.INSTANCE.sendMessage(ChatFormatting.GRAY + "friend " + ChatFormatting.WHITE + "> " + name + " " + ChatFormatting.RED + "deleted");
                            check = true;
                            return;
                        }
                    }
                }
                if (mode == Mode.PEARL){
                    int old = mc.player.inventory.currentItem;
                    int pearl = ItemUtil.findItem(ItemEnderPearl.class);
                    if (pearl != -1){
                        ItemUtil.swapToHotbarSlot(pearl, false);
                        mc.playerController.processRightClick(mc.player,mc.world, EnumHand.MAIN_HAND);
                        if (silent) ItemUtil.swapToHotbarSlot(old, false);
                        check = true;
                    }
                }
                if (mode == Mode.XP){
                    int old = mc.player.inventory.currentItem;
                    int xp = ItemUtil.findItem(ItemExpBottle.class);
                    if (xp != -1){
                        ItemUtil.swapToHotbarSlot(xp, false);
                        mc.playerController.processRightClick(mc.player,mc.world, EnumHand.MAIN_HAND);
                        if (silent) ItemUtil.swapToHotbarSlot(old, false);
                    }
                }
            }
        } else check = false;
    }

    public enum Mode{
        FRIEND,PEARL,XP
    }
}
