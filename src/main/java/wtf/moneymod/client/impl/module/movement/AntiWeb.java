package wtf.moneymod.client.impl.module.movement;

import net.minecraft.init.Items;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.util.text.TextComponentString;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.events.MoveEvent;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.world.EntityUtil;
import wtf.moneymod.client.mixin.accessors.AccessorEntity;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

@Module.Register( label = "AntiWeb", cat = Module.Category.MOVEMENT)
public class AntiWeb extends Module {


    @Value(value = "Timer Speed") @Bounds(max = 12) public float timerSpeed = 2;
    @Value(value = "Motion Speed") @Bounds(max = 4) public float motionSpeed = 0.4f;
    @Value(value = "Mode") public Mode mode = Mode.MOTION;
    public enum Mode{
        TIMER, MOTION, SKIP
    }

    @Override
    public void onToggle(){
        Main.TICK_TIMER = 1;
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;
        if (((AccessorEntity) mc.player).isInWeb() && !mc.player.onGround && mc.gameSettings.keyBindSneak.isKeyDown()) {
            switch (mode){
                case MOTION:
                    mc.player.motionY = -motionSpeed;
                    break;
                case TIMER:
                    Main.TICK_TIMER = timerSpeed * 2;
                    break;
                case SKIP:
                    mc.player.motionY = -50;
                    break;

            }
        } else
            Main.TICK_TIMER = 1;
    }

}
