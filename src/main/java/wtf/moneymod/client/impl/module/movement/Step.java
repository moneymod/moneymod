package wtf.moneymod.client.impl.module.movement;

import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import wtf.moneymod.client.api.events.UpdateWalkingPlayerEvent;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.world.EntityUtil;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

@Module.Register( label = "Step", cat = Module.Category.MOVEMENT )
public class Step extends Module {

    @Value( value = "Height" ) @Bounds( min = 0,max = 2 ) public float height = 2;
    @Value( value = "Packet" ) public boolean packet = false;
    private double currentStepHeight = 0;



    @Handler
    public Listener<UpdateWalkingPlayerEvent> playerEventListener = new Listener<>(UpdateWalkingPlayerEvent.class, event -> {
        if (packet) {
            if (!mc.player.isInWater() && mc.player.onGround && !mc.player.isOnLadder() && !mc.player.movementInput.jump && mc.player.collidedVertically && (double) mc.player.fallDistance < 0.1) {
                currentStepHeight = EntityUtil.INSTANCE.getCurrentStepHeight(this.height);
                if (currentStepHeight == 0.0D) {
                    return;
                }

                if (currentStepHeight <= 1.0D) {
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.42D, mc.player.posZ, mc.player.onGround));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.75D, mc.player.posZ, mc.player.onGround));
                    mc.player.setPosition(mc.player.posX, mc.player.posY + 1.0D, mc.player.posZ);
                    return;
                }

                if (currentStepHeight <= height && currentStepHeight <= 1.5D) {
                    event.setCancelled(true);
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.42D, mc.player.posZ, mc.player.onGround));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.75D, mc.player.posZ, mc.player.onGround));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.0D, mc.player.posZ, mc.player.onGround));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.16D, mc.player.posZ, mc.player.onGround));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.23D, mc.player.posZ, mc.player.onGround));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.2D, mc.player.posZ, mc.player.onGround));
                    mc.player.setPosition(mc.player.posX, mc.player.posY + currentStepHeight, mc.player.posZ);
                    return;
                }

                if (currentStepHeight <= height) {
                    event.setCancelled(true);
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.42D, mc.player.posZ, mc.player.onGround));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.7800000000000002D, mc.player.posZ, mc.player.onGround));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.63D, mc.player.posZ, mc.player.onGround));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.51D, mc.player.posZ, mc.player.onGround));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.9D, mc.player.posZ, mc.player.onGround));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.21D, mc.player.posZ, mc.player.onGround));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.45D, mc.player.posZ, mc.player.onGround));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.43D, mc.player.posZ, mc.player.onGround));
                    mc.player.setPosition(mc.player.posX, mc.player.posY + currentStepHeight, mc.player.posZ);
                }
            }
        }
    });

    @Override public void onTick() {
        if (!mc.player.isInLava() && !mc.player.isInWater() && !packet) {
            if (mc.player.onGround)
                mc.player.stepHeight = ( float ) height;
        }

    }

    @Override public void onDisable() {
        mc.player.stepHeight = 0.6F;
    }
}
