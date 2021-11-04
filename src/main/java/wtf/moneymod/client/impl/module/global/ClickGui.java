package wtf.moneymod.client.impl.module.global;

import club.cafedevelopment.reflectionsettings.annotation.Setting;
import org.lwjgl.input.Keyboard;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.ui.click.theme.Themes;
import wtf.moneymod.client.impl.utility.impl.render.JColor;

import java.awt.*;

@Module.Register( label = "ClickGui", cat = Module.Category.GLOBAL, key = Keyboard.KEY_INSERT )
public class ClickGui extends Module {

    @Setting( id = "Theme" ) public Themes theme = Themes.NODUS;
    @Setting( id = "Descriptions" ) public boolean desc = true;
    @Setting( id = "Color" ) public JColor color = new JColor(0, 255, 0, true);
    @Setting( id = "Blur" ) public boolean blur = false;
    @Setting( id = "Bounding" ) public boolean bounding = true;

    @Override protected void onEnable() {
        if (mc.currentScreen == null) mc.displayGuiScreen(Main.getMain().getScreen());
        disable();
    }

}
