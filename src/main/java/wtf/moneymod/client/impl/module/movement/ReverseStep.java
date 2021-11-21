package wtf.moneymod.client.impl.module.movement;

import org.lwjgl.input.Keyboard;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;

@Module.Register( label = "FastFall", cat = Module.Category.MOVEMENT)
public class ReverseStep extends Module {

    @Value(value = "Speed") @Bounds(max = 15) public float speed = 10;

    @Override
    public void onTick() {
        if (mc.player.isInWater() || mc.player.isInLava()) return;
        if (mc.player.onGround) mc.player.motionY -= speed / 10;
    }
}
