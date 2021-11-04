package wtf.moneymod.client.impl.module.render;

import net.minecraft.util.EnumHand;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;

@Module.Register( label = "Swing", cat = Module.Category.MOVEMENT)
public class Swing extends Module {

    @Value(value = "Offhand") public boolean offhand = true;

    @Override public void onTick(){
        mc.player.swingingHand = (offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
    }

}
