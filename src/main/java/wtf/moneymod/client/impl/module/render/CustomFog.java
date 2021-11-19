package wtf.moneymod.client.impl.module.render;

import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.render.JColor;

@Module.Register( label = "CustomFog", cat = Module.Category.RENDER)
public class CustomFog extends Module {

    @Value( value = "Color" ) public JColor color = new JColor(255, 0, 0, 180, true);


}
