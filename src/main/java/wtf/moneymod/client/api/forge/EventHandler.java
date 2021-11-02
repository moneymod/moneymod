package wtf.moneymod.client.api.forge;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.Globals;

public class EventHandler implements Globals {

    @SubscribeEvent public void onInput(InputEvent event) {
        Main.getMain().getModuleManager().get(m -> Keyboard.getEventKey() != 0 && Keyboard.getEventKeyState() && Keyboard.getEventKey() == m.getKey()).forEach(Module::toggle);
    }

    @SubscribeEvent public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START || nullCheck()) return;
        Main.getMain().getModuleManager().get(Module::isToggled).forEach(Module::onTick);
    }

}
