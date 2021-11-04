package wtf.moneymod.client.impl.module.render;

import club.cafedevelopment.reflectionsettings.annotation.Setting;
import net.minecraft.util.EnumHand;
import org.lwjgl.input.Keyboard;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.world.EntityUtil;

@Module.Register( label = "Swing", cat = Module.Category.MOVEMENT)
public class Swing extends Module {

    @Setting(id = "Offhand") public boolean offhand = true;

    @Override public void onTick(){
        mc.player.swingingHand = (offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
    }

}
