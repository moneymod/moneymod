package wtf.moneymod.client.impl.module.movement;

import net.minecraft.entity.MoverType;
import org.lwjgl.input.Keyboard;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.mixin.accessors.AccessorEntity;

@Module.Register( label = "FastFall", cat = Module.Category.MOVEMENT)
public class FastFall extends Module {

    @Value(value = "Speed") @Bounds(max = 15) public float speed = 10;
    @Value(value = "Web") public boolean web = false;
    @Value(value = "Mode") public Mode mode = Mode.MOTION;
    @Value(value = "Timer Speed") @Bounds(max = 8) public float timerSpeed = 2;
    @Value(value = "Motion Speed") @Bounds(max = 4) public float motionSpeed = 0.4f;
    public enum Mode{
        TIMER, MOTION, SKIP
    }

    @Override
    public void onToggle(){
        Main.TICK_TIMER = 1;
    }

    @Override
    public void onTick() {
        if (mc.player.isInWater() || mc.player.isInLava()) return;
        if (mc.player.onGround) mc.player.motionY -= speed / 10;

        if (((AccessorEntity) mc.player).isInWeb() && !mc.player.onGround && mc.gameSettings.keyBindSneak.isKeyDown()) {
            switch (mode){
                case MOTION:
                    mc.player.motionY = -motionSpeed;
                    break;
                case TIMER:
                    Main.TICK_TIMER = timerSpeed * 4;
                    break;
                case SKIP:
                    mc.player.motionY = -55;
                    break;

            }
        } else
            Main.TICK_TIMER = 1;
    }
}
