package wtf.moneymod.client.api.forge;

import club.cafedevelopment.reflectionsettings.container.SettingContainer;
import club.cafedevelopment.reflectionsettings.container.SettingManager;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.impl.command.Command;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.Globals;
import wtf.moneymod.client.impl.utility.impl.render.ColorUtil;
import wtf.moneymod.client.impl.utility.impl.render.JColor;

import java.awt.*;

public class EventHandler implements Globals {

    @SubscribeEvent public void onInput(InputEvent event) {
        Main.getMain().getModuleManager().get(m -> Keyboard.getEventKey() != 0 && Keyboard.getEventKeyState() && Keyboard.getEventKey() == m.getKey()).forEach(Module::toggle);
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
            for (SettingContainer setting : SettingManager.getInstance().acquireFrom(m)) {
                if (setting.getValue().getClass().getSimpleName().equalsIgnoreCase("JColor")) {
                    JColor color = setting.getValue();
                    float[] hsb = Color.RGBtoHSB(color.getColor().getRed(), color.getColor().getGreen(), color.getColor().getBlue(), null);
                    if (color.isRainbow()) color.setColor(ColorUtil.rainbowColor(0, hsb[ 1 ], hsb[ 2 ]));
                }
            }
        }
    }

}
