package wtf.moneymod.client.impl.module.render;

import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.management.impl.HoleManagement;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.render.Renderer3D;

import java.awt.*;

@Module.Register( label = "HoleESP", cat = Module.Category.RENDER )
public class HoleEsp extends Module {

    @SubscribeEvent public void onRender3D(RenderWorldLastEvent e) {
        Main.getMain().getHoleManagement().forEach(hole -> {
            System.out.println(hole.getBlockPos());
            if(hole.getType().equals(HoleManagement.HoleType.BEDROCK)) {
                Renderer3D.drawBoxESP(hole.getBlockPos(), new Color(0, 255, 0), 1, true, true, 120, 255, 1f);
            } else {
                Renderer3D.drawBoxESP(hole.getBlockPos(), new Color(255, 0, 0), 1, true, true, 120, 255, 1f);
            }

        });
    }

}
