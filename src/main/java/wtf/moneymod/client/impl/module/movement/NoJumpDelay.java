package wtf.moneymod.client.impl.module.movement;

import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.mixin.mixins.ducks.AccessorMinecraft;

@Module.Register( label = "dshit", cat = Module.Category.MOVEMENT)
public class NoJumpDelay extends Module {

    @Override
    public void onTick(){
        if (nullCheck()) return;
    }
}
