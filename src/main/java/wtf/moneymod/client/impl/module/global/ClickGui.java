package wtf.moneymod.client.impl.module.global;

import org.lwjgl.input.Keyboard;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.ui.click.theme.Themes;
import wtf.moneymod.client.impl.utility.impl.render.JColor;

@Module.Register( label = "ClickGui", cat = Module.Category.GLOBAL, key = Keyboard.KEY_RETURN )
public class ClickGui extends Module {

    @Value( value = "Theme" ) public Themes theme = Themes.NODUS;
    @Value( value = "Descriptions" ) public boolean desc = true;
    @Value( value = "Color" ) public JColor color = new JColor(0, 255, 0, true);
    @Value( value = "Blur" ) public boolean blur = false;
    @Value( value = "Bounding" ) public boolean bounding = true;

    @Override protected void onEnable() {
        if (mc.currentScreen == null) mc.displayGuiScreen(Main.getMain().getScreen());
        disable();
    }

}
