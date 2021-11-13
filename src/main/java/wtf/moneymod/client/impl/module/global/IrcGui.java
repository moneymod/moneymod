package wtf.moneymod.client.impl.module.global;

import org.lwjgl.input.Keyboard;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.ui.click.theme.Themes;
import wtf.moneymod.client.impl.utility.impl.render.JColor;

@Module.Register( label = "IrcGui", cat = Module.Category.GLOBAL, key = Keyboard.KEY_HOME )
public class IrcGui extends Module {

    @Override protected void onEnable() {
        if (mc.currentScreen == null) mc.displayGuiScreen(Main.getMain().getIrcScreen());
        disable();
    }

}
