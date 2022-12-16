package wtf.moneymod.client.impl.module.render;

import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.management.impl.HoleManagement;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.render.JColor;
import wtf.moneymod.client.impl.utility.impl.render.Renderer2D;
import wtf.moneymod.client.impl.utility.impl.render.Renderer3D;

import java.awt.*;

@Module.Register( label = "HoleESP", cat = Module.Category.RENDER )
public class HoleEsp extends Module {

    @Value( value = "Box" ) public boolean box = true;
    @Value( value = "Outline" ) public boolean outline = true;
    @Value( value = "Height" ) @Bounds( max = 1 ) public double height = 1;
    @Value( value = "Line Width" ) @Bounds( max = 1 ) public double widht = 1;

    @Value( value = "B-Color" ) public JColor bedrockColor = new JColor(0, 255, 0, false);
    @Value( value = "B-Line" ) public JColor bedrockLine = new JColor(0, 255, 0, false);
    @Value("B-Wireframe") public boolean bedrockWireframe = false;

    @Value( value = "O-Line" ) public JColor obsidianLine = new JColor(255, 0, 0, false);
    @Value( value = "O-Color" ) public JColor obsidianColor = new JColor(255, 0, 0, false);
    @Value("O-Wireframe") public boolean obsidianWireframe = false;


    @Override public void onRender3D(float partialTicks) {
        Main.getMain().getHoleManagement().forEach(hole -> {
            if (hole.getType().equals(HoleManagement.HoleType.BEDROCK)) {
                Renderer3D.drawBoxESP(hole.getBlockPos(), bedrockColor.getColor(), bedrockLine.getColor(), ( float ) widht, outline, box, bedrockColor.getColor().getAlpha(), bedrockLine.getColor().getAlpha(), ( float ) height);
                if (bedrockWireframe) Renderer2D.drawBlockWireframe(hole.getBlockPos(), height, ( float ) widht, bedrockLine.getColor());
            } else {
                Renderer3D.drawBoxESP(hole.getBlockPos(), obsidianColor.getColor(), obsidianLine.getColor(), ( float ) widht, outline, box, obsidianColor.getColor().getAlpha(), obsidianLine.getColor().getAlpha(), ( float ) height);
                if (obsidianWireframe) Renderer2D.drawBlockWireframe(hole.getBlockPos(), height, ( float ) widht, obsidianLine.getColor());
            }
        });
    }

}
