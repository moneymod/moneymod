package wtf.moneymod.client.impl.module.render;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import wtf.moneymod.client.api.events.EntityDeathEvent;
import wtf.moneymod.client.api.events.PacketEvent;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

@Module.Register( label = "KillEffect", cat = Module.Category.RENDER )
public class KillEffect extends Module {

    @Value( "Effect" ) public Effect effect = Effect.BOLT;
    @Value( "Sound" ) public boolean sound = true;
    @Value("KillStreak") public boolean killStreak = false;

    @Handler public Listener<EntityDeathEvent> listener = new Listener<>(EntityDeathEvent.class, event -> {
        if (!nullCheck() && event.getEntity() != null) {
            Entity entity = event.getEntity();
            if (entity != null) {
                if (entity.isDead || ((EntityPlayer) entity).getHealth() <= 0) {
                    mc.world.spawnEntity(new EntityLightningBolt(mc.world, entity.posX, entity.posY, entity.posZ, true));
                    if (sound) mc.player.playSound(SoundEvents.ENTITY_LIGHTNING_THUNDER, 0.5f, 1.f);
                }
            }
        }
    });

    public enum Effect {
        BOLT
    }

}
