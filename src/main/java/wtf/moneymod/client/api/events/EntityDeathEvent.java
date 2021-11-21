package wtf.moneymod.client.api.events;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import wtf.moneymod.eventhandler.event.Event;

public class EntityDeathEvent extends Event {

    private DamageSource source;
    private Entity entity;

    public EntityDeathEvent(DamageSource source, Entity entity) {
        this.source = source;
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    public DamageSource getSource() {
        return source;
    }

}
