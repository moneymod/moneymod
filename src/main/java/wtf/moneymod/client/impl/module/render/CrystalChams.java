package wtf.moneymod.client.impl.module.render;

import org.lwjgl.input.Keyboard;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.render.JColor;
import wtf.moneymod.client.impl.utility.impl.world.EntityUtil;

@Module.Register( label = "CrystalChams", cat = Module.Category.MOVEMENT)
public class CrystalChams extends Module {

    @Value(value = "CancelY") public boolean cancelY = false;
    @Value(value = "CancelX") public boolean cancelX = false;
    @Value(value = "Glint") public boolean glint = false;
    @Value(value = "Chams") public boolean chams = false;
    @Value(value = "CrystalModel") public boolean crystalModel = false;
    @Value(value = "Scale") @Bounds(max = 1) public double scale = 0.5D;
    @Value(value = "Line") @Bounds(max = 3) public double line = 0.5D;
    @Value(value = "Color" ) public JColor color = new JColor(255, 0, 0,180, false);

}
