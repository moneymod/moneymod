package wtf.moneymod.client.impl.module.movement;

import net.minecraft.entity.MoverType;
import org.lwjgl.input.Keyboard;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;

@Module.Register( label = "FastFall", cat = Module.Category.MOVEMENT)
public class ReverseStep extends Module {

    @Value(value = "Speed") @Bounds(max = 15) public float speed = 10;
    @Value(value = "Test") public boolean test = false;
    @Override
    public void onTick() {
        if (mc.player.isInWater() || mc.player.isInLava()) return;
        if (mc.player.onGround && !test) mc.player.motionY -= speed / 10;

        if (test && mc.player.onGround){

            if (mc.player.fallDistance <= 1){
                mc.player.move(MoverType.PLAYER, 0, -1, 0);
            } else {
                mc.player.motionY -= speed / 10;
            }

        }
    }
}
