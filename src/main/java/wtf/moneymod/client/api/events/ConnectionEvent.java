package wtf.moneymod.client.api.events;

import net.minecraft.entity.Entity;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import wtf.moneymod.eventhandler.event.Event;

import java.util.UUID;

public class ConnectionEvent extends Event {

    private final SPacketPlayerListItem.Action action;
    private final Entity entity;
    private final UUID uuid;
    private final String name;

    public ConnectionEvent(SPacketPlayerListItem.Action action, Entity entity, UUID uuid, String name) {
        this.action = action;
        this.entity = entity;
        this.uuid = uuid;
        this.name = name;
    }

    public SPacketPlayerListItem.Action getAction() {
        return action;
    }

    public String getName() {
        return name;
    }

    public Entity getEntity() {
        return entity;
    }

    public UUID getUuid() {
        return uuid;
    }

}
