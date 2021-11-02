package wtf.moneymod.client.other.module.movement;

import club.cafedevelopment.reflectionsettings.annotation.Clamp;
import club.cafedevelopment.reflectionsettings.annotation.Setting;
import org.lwjgl.input.Keyboard;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.other.module.Module;;

@Module.Register( label = "Warp", cat = Module.Category.MOVEMENT, key = Keyboard.KEY_X )
public class Warp extends Module {

    //Pro module

    @Setting(id = "Time", clamp = @Clamp(min = 1, max = 16)) int time = 4;
    @Setting(id = "Tick", clamp = @Clamp(min = 1, max = 8)) int tick = 4;
    @Setting(id = "Mode") Mode mode = Mode.SKIP;
    //pov when you not pasting ^

    int delay;
    @Override public void onTick() {
        if(mode == Mode.TICK)
        delay++;
        Main.TICK_TIMER = tick;
        if (delay >= time){
            setToggled(false);
            delay = 0;
            return;
        }
    }
    public enum Mode {
        SKIP, TICK
    }
}
