package wtf.moneymod.client.impl.module.movement;

import net.minecraft.init.Items;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.util.text.TextComponentString;
import wtf.moneymod.client.api.events.MoveEvent;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.world.EntityUtil;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

@Module.Register( label = "GlideFly", cat = Module.Category.MOVEMENT)
public class Fly extends Module {

    @Value(value = "Speed") @Bounds(max = 8) public float speed = 1;

    @Handler
    public Listener<MoveEvent> moveListener = new Listener<>(MoveEvent.class, event -> {
        if (nullCheck()) return;
        EntityUtil.INSTANCE.setVanilaSpeed(event, speed);
    });

    @Override
    public void onTick(){
        if (nullCheck()) return;
        mc.player.motionY = (mc.gameSettings.keyBindJump.isKeyDown() ? +0.01 : -0.01);
    }

}
