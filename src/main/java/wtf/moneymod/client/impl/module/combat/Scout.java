package wtf.moneymod.client.impl.module.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wtf.moneymod.client.api.events.PacketEvent;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.render.Renderer2D;
import wtf.moneymod.client.impl.utility.impl.render.fonts.FontRender;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

import java.awt.*;

@Module.Register( label = "Scout", cat = Module.Category.COMBAT)
public class Scout extends Module {

    @Value(value = "Time") @Bounds(min = 1,max = 8) public int time = 2;
    @Value(value = "Spoof Ticks") @Bounds(min = 1,max = 100) public int spoof = 10;
    @Value(value = "Horizontally") public boolean horizontally = false;
    @Value(value = "Render") public boolean render = false;
    @Value(value = "AutoFire") public boolean autoFire = true;

    private boolean hs;
    int ticks;
    private long lastHsTime;
    long percent;


    @Override public void onToggle() {
        hs = false;
        ticks = 0;
        lastHsTime = System.currentTimeMillis();
    }

    @Override public void onTick(){
        if (autoFire && mc.player.getHeldItemMainhand().getItem() instanceof ItemBow && mc.player.isHandActive() && mc.player.getItemInUseMaxCount() >= 4  && percent >= 100){
            ticks++;
            if (ticks >= 14){
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
                mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                mc.player.stopActiveHand();
                ticks = 0;
            }
        }
        percent = (long) Math.min((System.currentTimeMillis() - lastHsTime) / (time * 1000L) * 100, 100);
    }

    @Handler public Listener<PacketEvent.Send> packeEventSend = new Listener<>(PacketEvent.Send.class, event -> {
        if (event.getPacket() instanceof CPacketPlayerDigging){
            CPacketPlayerDigging packet = (CPacketPlayerDigging) event.getPacket();
            if (packet.getAction() == CPacketPlayerDigging.Action.RELEASE_USE_ITEM){
                ItemStack handStack = mc.player.getHeldItem(EnumHand.MAIN_HAND);
                if (!handStack.isEmpty() && handStack.getItem() != null && handStack.getItem() instanceof ItemBow) {
                    if (System.currentTimeMillis() - lastHsTime >= time * 1000 ) {
                        hs = true;
                        lastHsTime = System.currentTimeMillis();
                        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SPRINTING));
                        for ( int i = 0; i < spoof; ++i ) {

                            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1e-10, mc.player.posZ, false));
                            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY - 1e-10, mc.player.posZ, true));

                        }
                        hs = false;
                    }
                }

            }
        }
    });

    @Override public void onRender2D() {
        GlStateManager.pushMatrix();
        ScaledResolution sr = new ScaledResolution(mc);
        if (render) {
            FontRender.drawStringWithShadow(percent >= 100 ? ChatFormatting.GREEN + "Ready" : "Not Ready", (int)(sr.getScaledWidth() / 2f - mc.fontRenderer.getStringWidth(percent >= 100 ? ChatFormatting.GREEN + "Ready" : "Not Ready") / 2f),
                    (int)(sr.getScaledHeight() / 2f + 10f), new Color(170, 170, 170 ).getRGB());
        }
        GlStateManager.popMatrix( );
    }
}
