package wtf.moneymod.client.impl.module.movement;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wtf.moneymod.client.api.events.MoveEvent;
import wtf.moneymod.client.api.management.impl.PacketManagement;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

import java.nio.file.Path;

@Module.Register(label = "NoSlow", cat = Module.Category.MOVEMENT)
public class NoSlow extends Module {

    @Value("Items") public boolean items = true;
    @Value("Bypass") public boolean megaBypass = false;

    @SubscribeEvent public void onInputUpdate(InputUpdateEvent event) {
        if(nullCheck()) return;
        if(items) {
            Item item = null;
            if(mc.player.isHandActive() && (item = mc.player.getActiveItemStack().getItem()) != null &&
                    ( item instanceof ItemPotion || item instanceof ItemBow || item instanceof ItemFood) ) {

                if(megaBypass) {
                    PacketManagement.getInstance().add(new CPacketHeldItemChange(mc.player.inventory.currentItem));
                }

                event.getMovementInput().moveForward *= 5f;
                event.getMovementInput().moveStrafe *= 5f;

            }

        }
    }

}
