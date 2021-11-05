package wtf.moneymod.client.api.forge;

import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.setting.Option;
import wtf.moneymod.client.impl.command.Command;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.Globals;
import wtf.moneymod.client.impl.utility.impl.render.ColorUtil;
import wtf.moneymod.client.impl.utility.impl.render.JColor;

import java.awt.*;

public class EventHandler implements Globals {

    @SubscribeEvent public void onInput(InputEvent.KeyInputEvent event) {
        Main.getMain().getModuleManager().get(module -> Keyboard.getEventKey() != 0 && Keyboard.getEventKeyState() && Keyboard.getEventKey() == module.getKey()).forEach(Module::toggle);
    }

    @SubscribeEvent public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START || nullCheck()) return;
        Main.getMain().getModuleManager().get(Module::isToggled).forEach(Module::onTick);
    }

    @SubscribeEvent public void onClientChat(ClientChatEvent event) {
        String message = event.getMessage();
        if (message.startsWith(Main.getMain().getCommandManagement().getPrefix())) {
            String temp = message.substring(1);
            for (Command command : Main.getMain().getCommandManagement()) {
                String[] split = temp.split(" ");
                for (String name : command.getAlias()) {
                    if (name.equalsIgnoreCase(split[ 0 ])) {
                        command.execute(split);
                        event.setCanceled(true);
                        mc.ingameGUI.getChatGUI().addToSentMessages(event.getMessage());
                        return;
                    }
                }
            }
            event.setCanceled(true);
        }
    }

    @SubscribeEvent public void onRenderUpdate(RenderWorldLastEvent event) {
        for (Module m : Main.getMain().getModuleManager()) {
            for (Option<?> setting : Option.getContainersForObject(m)) {
                if (setting.getValue() instanceof JColor) {
                    JColor color = ( JColor ) setting.getValue();
                    float[] hsb = Color.RGBtoHSB(color.getColor().getRed(), color.getColor().getGreen(), color.getColor().getBlue(), null);
                    if (color.isRainbow()) {
                        ((Option<JColor>)setting).setValue(new JColor(ColorUtil.injectAlpha(ColorUtil.rainbowColor(0, hsb[ 1 ], hsb[ 2 ]), color.getColor().getAlpha()), color.isRainbow()));
                    }
                }
            }
        }
    }

}
