package wtf.moneymod.client.impl.module.global;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.render.ColorUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

@Module.Register(label = "Hud", cat = Module.Category.GLOBAL)
public class Hud extends Module {

    @Value(value = "Watermark") public boolean watermark = true;
    @Value(value = "Feature List") public boolean features = true;
    @Value(value = "Offset") @Bounds(max = 5) public int offset = 1;

    @SubscribeEvent public void onRenderer2D(RenderGameOverlayEvent.Text event) {
        FontRenderer fr = mc.fontRenderer;
        ScaledResolution sr = new ScaledResolution(mc);
        if(watermark) fr.drawStringWithShadow(Main.MODID, 1, 1, Color.HSBtoRGB(0, 0, Main.getMain().getPulseManagement().getDifference(1) / 255f));
        if(features) {
            ArrayList<Module> modules = Main.getMain().getModuleManager().stream().sorted(Comparator.comparing(m -> -fr.getStringWidth(m.getLabel()))).collect(Collectors.toCollection(ArrayList::new));
            int y = 0, count = 1;
            for(Module module : modules) {
                if(!module.isToggled() || !module.drawn) continue;
                fr.drawStringWithShadow(module.getLabel(), sr.getScaledWidth() - fr.getStringWidth(module.getLabel()), y, Color.HSBtoRGB(0, 0, Main.getMain().getPulseManagement().getDifference(y * 2) / 255f));
                y += fr.FONT_HEIGHT + offset;
                count++;
            }
        }
    }

}
