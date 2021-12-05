package wtf.moneymod.client.impl.module.misc;

import akka.io.TcpListener;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.util.EnumHand;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import wtf.moneymod.client.api.management.impl.FriendManagement;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.ui.click.Panel;
import wtf.moneymod.client.impl.utility.impl.player.ItemUtil;
import wtf.moneymod.client.impl.utility.impl.render.fonts.FontRender;
import wtf.moneymod.client.impl.utility.impl.world.ChatUtil;
import wtf.moneymod.client.impl.utility.impl.world.EntityUtil;

import java.awt.*;

@Module.Register( label = "MiddleSelections", cat = Module.Category.MISC)
public class MiddleSelections extends Module {


    @Value(value = "Only Shift") public boolean onlyShift = true;

    String text = "none", name = null;
    boolean check = false;
    int act = 0, item = -1;

    public void doNull(){
        name = null;
        act = 0;
        check = false;
    }

    public void doScroll() {
        int dWheel = Mouse.getDWheel();
        if (dWheel > 0) act++;
        if (dWheel < 0) act++;
    }

    @Override public void onTick(){
        if (nullCheck()) return;
        if (onlyShift && !mc.gameSettings.keyBindSneak.isKeyDown()) {
            doNull();
            return;
        }
        if (act > 2) act = 0;
        if (!Mouse.isButtonDown(2)) {
            item = mc.player.inventory.currentItem;
        }
        if (mc.objectMouseOver.entityHit instanceof EntityPlayer) {
            if (Mouse.isButtonDown(2)) {
                name = mc.objectMouseOver.entityHit.getName();
                ItemUtil.swapToHotbarSlot(item,false);
                doScroll();
                check = true;
            } else {
                if (check) {
                    if (act == 1) {
                        if (FriendManagement.getInstance().is(name)){
                            FriendManagement.getInstance().remove(name);
                            ChatUtil.INSTANCE.sendInfoMessage(name + " unfriended");
                        } else {
                            FriendManagement.getInstance().add(name);
                            ChatUtil.INSTANCE.sendInfoMessage(name + " friended");
                        }
                    } else if (act == 2) mc.player.sendChatMessage("/duel " + name);
                    doNull();
                }
                return;
            }
        } else {
            doNull();
            return;
        }
    }

    @Override public void onRender2D() {
        if (act == 0) text = ChatFormatting.GREEN + "[ none ]";
        if (act == 1){
            if (FriendManagement.getInstance().is(name)){
                text = ChatFormatting.GREEN + "[ del ]";
            } else {
                text = ChatFormatting.GREEN + "[ add ]";
            }
        }
        if (act == 2) text = ChatFormatting.GREEN + "[ duel ]";

        ScaledResolution sr = new ScaledResolution(mc);
        if (check) {
            GlStateManager.pushMatrix();
            FontRender.drawStringWithShadow(text, (int) (sr.getScaledWidth() / 2f - mc.fontRenderer.getStringWidth(text) / 2f),
                    (int) (sr.getScaledHeight() / 2f + 10f), new Color(170, 170, 170).getRGB());
            GlStateManager.popMatrix();
        }
    }
}
