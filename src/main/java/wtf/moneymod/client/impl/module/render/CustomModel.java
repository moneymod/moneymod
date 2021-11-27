package wtf.moneymod.client.impl.module.render;

import net.minecraft.util.EnumHand;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;

@Module.Register( label = "CustomModel", cat = Module.Category.RENDER)
public class CustomModel extends Module {


    @Value(value = "ScaleX") @Bounds(min = -3, max = 3) public float scaleX = 1;
    @Value(value = "ScaleY") @Bounds(min = -3, max = 3) public float scaleY = 1;
    @Value(value = "ScaleZ") @Bounds(min = -3, max = 3) public float scaleZ = 1;

    @Value(value = "TranslateX") @Bounds(min = -5, max = 5) public float translateX = 0;
    @Value(value = "TranslateY") @Bounds(min = -5, max = 5) public float translateY = -0.5f;
    @Value(value = "TranslateZ") @Bounds(min = -5, max = 5) public float translateZ = 0;

}
