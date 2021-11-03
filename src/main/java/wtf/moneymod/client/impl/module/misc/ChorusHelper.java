package wtf.moneymod.client.impl.module.misc;

import club.cafedevelopment.reflectionsettings.annotation.Clamp;
import club.cafedevelopment.reflectionsettings.annotation.Setting;
import net.minecraft.init.Items;
import net.minecraft.item.ItemChorusFruit;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wtf.moneymod.client.api.events.PacketEvent;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.Timer;
import wtf.moneymod.client.impl.utility.impl.render.JColor;
import wtf.moneymod.client.impl.utility.impl.render.Renderer3D;

import java.util.LinkedList;
import java.util.Queue;

@Module.Register( label = "ChorusHelper", cat = Module.Category.MOVEMENT)
public class ChorusHelper extends Module {

    boolean checkChorus = false,hackPacket = false, posTp = false;
    double posX, posY, posZ;
    Queue<CPacketPlayer> packets = new LinkedList<>();
    Queue<CPacketConfirmTeleport> packetss = new LinkedList<>();
    SPacketPlayerPosLook serverPos;
    BlockPos playerPos;
    @Setting(id = "Render") public boolean render = true;
    @Setting(id = "Color" ) public JColor color = new JColor(0, 255, 0, true);

    @Override
    public void onToggle() {
        checkChorus = false;
        hackPacket = false;
        posTp = false;
        playerPos = null;
    }


    @Override
    public void onTick() {
        if (checkChorus) {
            if (!mc.player.getPosition().equals(new BlockPos(posX, posY, posZ)) && !posTp) {
                if (mc.player.getDistance(posX, posY, posZ) > 1) {
                    mc.player.setPosition(posX, posY, posZ);
                    posTp = true;
                }
            }
        }
        if (checkChorus && mc.player.isSneaking()) doTeleport();
    }

    public void doTeleport(){
        checkChorus = false;
        hackPacket = true;
        playerPos = null;
        sendPackets();
    }
    public void sendPackets() {
        while (!packets.isEmpty()) {
            mc.player.connection.sendPacket(packets.poll());
        }
        while (!packetss.isEmpty()) {
            mc.player.connection.sendPacket(packetss.poll());
        }
        hackPacket = false;
        checkChorus = false;
    }




    @SubscribeEvent
    public void finishEating(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntity() == mc.player) {
            if (event.getResultStack().getItem().equals(Items.CHORUS_FRUIT)) {
                posX = mc.player.posX;
                posY = mc.player.posY;
                posZ = mc.player.posZ;
                posTp = false;
                checkChorus = true;
            }
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketConfirmTeleport && checkChorus) {
            packetss.add((CPacketConfirmTeleport) event.getPacket());
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketPlayer && checkChorus) {
            packets.add((CPacketPlayer) event.getPacket());
            event.setCanceled(true);
        }
    }
    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            serverPos = (SPacketPlayerPosLook) event.getPacket();
        }
    }

    @SubscribeEvent public void onRender(RenderWorldLastEvent event) {
        if (playerPos != null && checkChorus) {
            playerPos = new BlockPos(serverPos.getX(), serverPos.getY(),serverPos.getZ())
            Renderer3D.drawBoxESP(new BlockPos(serverPos.getX(), serverPos.getY(), serverPos.getZ()), color.getColor(), 0.2f, true, true, color.getColor().getAlpha(), color.getColor().getAlpha(), 2);
        }
    }
}
