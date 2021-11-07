package wtf.moneymod.client.impl.module.misc;

import org.lwjgl.input.Keyboard;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.world.EntityUtil;

@Module.Register( label = "MiddleClick", cat = Module.Category.MISC)
public class MiddleClick extends Module {

    @Value(value = "Mode") public Mode mode = Mode.FRIEND;

    @Override public void onTick(){
        
    }

    public enum Mode{
        FRIEND,PEARL,XP
    }
}
