package wtf.moneymod.client.impl.module.render;

import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.management.impl.HoleManagement;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.render.JColor;
import wtf.moneymod.client.impl.utility.impl.render.Renderer3D;

import java.awt.*;

@Module.Register( label = "HoleESP", cat = Module.Category.RENDER )
public class HoleEsp extends Module {

    @Value(value = "Box") public boolean box = true;
    @Value(value = "Height") @Bounds(max = 1) public double height = 1;
    @Value(value = "Line Widht") @Bounds(max = 1) public double widht = 1;

    @Value(value = "B-Color" ) public JColor bedrockColor = new JColor(0, 255, 0, false);
    @Value(value = "O-Color" ) public JColor obsidianColor = new JColor(255, 0, 0, false);


    @SubscribeEvent public void onRender3D(RenderWorldLastEvent e) {
        Main.getMain().getHoleManagement().forEach(hole -> {
            if(hole.getType().equals(HoleManagement.HoleType.BEDROCK)) {
                Renderer3D.drawBoxESP(hole.getBlockPos(), bedrockColor.getColor(), (float) widht, (widht > 0.1 ? true : false), box, bedrockColor.getColor().getAlpha(), bedrockColor.getColor().getAlpha(), (float) height);
            } else {
                Renderer3D.drawBoxESP(hole.getBlockPos(), obsidianColor.getColor(), (float) widht, (widht > 0.1 ? true : false), box, obsidianColor.getColor().getAlpha(), obsidianColor.getColor().getAlpha(), (float) height);
            }
        });
    }
}
