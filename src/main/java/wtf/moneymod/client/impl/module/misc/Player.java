package wtf.moneymod.client.impl.module.misc;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import wtf.moneymod.client.api.events.DisconnectEvent;
import wtf.moneymod.client.api.events.PacketEvent;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

import java.util.UUID;

@Module.Register( label = "Player", cat = Module.Category.MISC, exception = true )
public class Player extends Module {

    @Value(value = "Rotations") public boolean rotations = false;
    @Value(value = "Attack") public boolean attack = false;
    @Value(value = "Jump") public boolean jump = false;
    private EntityOtherPlayerMP player;
    int aim;

    @Handler
    public Listener<DisconnectEvent> disconnectEventListener = new Listener<>(DisconnectEvent.class, e -> {
        setToggled(false);
    });

    @Override public void onEnable() {
        aim = 0;
        if (mc.player == null) {
            disable();
            return;
        }
        if (player == null) {
            player = new EntityOtherPlayerMP(mc.world, new GameProfile(UUID.randomUUID(), "Player"));
            player.copyLocationAndAnglesFrom(mc.player);
            player.inventory.copyInventory(mc.player.inventory);
        }
        mc.world.spawnEntity(player);
    }

    @Override public void onTick(){
        aim++;
        if (player.onGround) player.motionY = 0.4;
        if (attack) player.swingArm(EnumHand.MAIN_HAND);
        if (aim >= 360) aim = 0;
        if (player != null){
             if (rotations){
                player.rotationPitch = aim;
                player.rotationYaw = aim;
                player.rotationYawHead = aim;
            }
        }
    }

    @Override public void onDisable() {
        if (player != null) {
            mc.world.removeEntity(player);
            player = null;
        }
    }
}
