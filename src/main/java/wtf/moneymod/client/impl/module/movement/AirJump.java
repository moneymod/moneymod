package wtf.moneymod.client.impl.module.movement;

import net.minecraft.entity.MoverType;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import wtf.moneymod.client.api.events.PacketEvent;
import wtf.moneymod.client.api.events.UpdateWalkingPlayerEvent;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.world.EntityUtil;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

@Module.Register( label = "AirJump", cat = Module.Category.MOVEMENT)
public class AirJump extends Module {

    @Override
    public void onTick() {
        if (nullCheck()) return;
        if (mc.gameSettings.keyBindJump.isKeyDown()) mc.player.jump();
    }
}

