package wtf.moneymod.client.impl.module.render;

import net.minecraft.util.EnumHand;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;

@Module.Register( label = "CustomModel", cat = Module.Category.RENDER)
public class CustomModel extends Module {


    @Value(value = "Scale") @Bounds(max = 1) public float scaleX = 1;

}
