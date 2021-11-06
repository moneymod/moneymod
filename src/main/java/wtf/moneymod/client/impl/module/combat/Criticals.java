package wtf.moneymod.client.impl.module.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.events.PacketEvent;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.mixin.accessors.AccessorEntity;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

@Module.Register( label = "Criticals", cat = Module.Category.COMBAT)
public class Criticals extends Module {

    @Value(value = "Only KA") public boolean onlyKillaura = true;
    @Value(value = "Strict") public boolean strict = false;

    @Handler
    public Listener<PacketEvent.Send> packeEventSend = new Listener<>(PacketEvent.Send.class, e -> {
        if (e.getPacket() instanceof CPacketUseEntity) {
            CPacketUseEntity packet = (CPacketUseEntity) e.getPacket();
            if (packet.getAction().equals(CPacketUseEntity.Action.ATTACK) && mc.player.onGround && !mc.player.isInWater() && !mc.player.isInLava()) {
                if (onlyKillaura){
                    if (!Main.getMain().getModuleManager().get(Aura.class).isToggled()) return;
                }
                if (!((AccessorEntity) mc.player).isInWeb()) {
                    Entity entity = packet.getEntityFromWorld(mc.world);
                    if (entity instanceof EntityLivingBase) {
                        double x = mc.player.posX;
                        double y = mc.player.posY;
                        double z = mc.player.posZ;

                        if ( strict ) {
                            mc.player.connection.sendPacket( new CPacketPlayer.Position( x, y + 0.07, z, false ) );
                            mc.player.connection.sendPacket( new CPacketPlayer.Position( x, y + 0.08, z, false ) );
                            mc.player.connection.sendPacket( new CPacketPlayer.Position( x, y, z, false ) );
                        }


                        mc.player.connection.sendPacket(new CPacketPlayer.Position(x, y + 0.05, z, false));
                        mc.player.connection.sendPacket(new CPacketPlayer.Position(x, y, z, false));
                        mc.player.connection.sendPacket(new CPacketPlayer.Position(x, y + 0.012, z, false));
                        mc.player.connection.sendPacket(new CPacketPlayer.Position(x, y, z, false));
                        mc.player.onCriticalHit(entity);
                    }
                }
            }
        }
    });
}