package wtf.moneymod.client.impl.module.misc;

import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import wtf.moneymod.client.api.events.PacketEvent;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.player.ItemUtil;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

@Module.Register( label = "AutoFish", cat = Module.Category.MISC )
public class AutoFish extends Module
{
    private int rodSlot = -1;

    @Override
    public void onEnable() {
        if (nullCheck()) {
            setToggled(false);
            return;
        }
        rodSlot = ItemUtil.findItem(ItemFishingRod.class);
    }

    @Handler
    public Listener< PacketEvent.Receive > onPacketReceive = new Listener< >( PacketEvent.Receive.class, event -> {
        if (event.getPacket() instanceof SPacketSoundEffect) {
            SPacketSoundEffect packet = (SPacketSoundEffect) event.getPacket();
            if (packet.getCategory() == SoundCategory.NEUTRAL && packet.getSound() == SoundEvents.ENTITY_BOBBER_SPLASH) {
                if( rodSlot == -1 )
                    rodSlot = ItemUtil.findItem(ItemFishingRod.class);
                if( rodSlot != -1 )
                {
                    int startSlot = mc.player.inventory.currentItem;
                    mc.getConnection().sendPacket(new CPacketHeldItemChange(rodSlot));
                    mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                    mc.player.swingArm(EnumHand.MAIN_HAND);
                    mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                    mc.player.swingArm(EnumHand.MAIN_HAND);
                    if (startSlot != -1)
                        mc.getConnection().sendPacket(new CPacketHeldItemChange(startSlot));
                }
            }
        }
    } );
}
