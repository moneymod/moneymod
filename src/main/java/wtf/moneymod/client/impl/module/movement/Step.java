package wtf.moneymod.client.impl.module.movement;

import club.cafedevelopment.reflectionsettings.annotation.Clamp;
import club.cafedevelopment.reflectionsettings.annotation.Setting;
import org.lwjgl.input.Keyboard;
import wtf.moneymod.client.impl.module.Module;

@Module.Register( label = "Step", cat = Module.Category.MOVEMENT, key = Keyboard.KEY_R)
public class Step extends Module {
    @Setting(id = "Heighttt", clamp = @Clamp(min = 0, max = 4)) public double height = 2;

    @Override public void onTick() {
        if (!mc.player.isInLava() && !mc.player.isInWater()){
            if (mc.player.onGround)
                mc.player.stepHeight = ( float ) height;
        }
    }

    @Override public void onDisable(){
        mc.player.stepHeight = 0.6F;
    }
}
