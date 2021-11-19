package wtf.moneymod.client.api.events;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import wtf.moneymod.eventhandler.event.Event;

public class FinishEatEvent extends Event {

    public FinishEatEvent(Entity entity, ItemStack result){
        this.entity = entity;
        this.itemStack = result;
    }

    private Entity entity;

    private ItemStack itemStack;

    public ItemStack getItemStack() {
        return itemStack;
    }

    public Entity getEntity() {
        return entity;
    }
}
