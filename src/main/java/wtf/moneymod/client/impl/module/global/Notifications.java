package wtf.moneymod.client.impl.module.global;

import com.mojang.realmsclient.gui.ChatFormatting;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.events.ToggleEvent;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.world.ChatUtil;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

import java.util.ArrayList;
import java.util.List;

@Module.Register( label = "Notifications", cat = Module.Category.GLOBAL )
public class Notifications extends Module {

    private List<Module> blacklist = new ArrayList<>();

    @Override public void onEnable() {
        blacklist.clear();
        blacklist.add(Main.getMain().getModuleManager().get(ClickGui.class));
    }

    @Handler public Listener<ToggleEvent> eventListener = new Listener<>(ToggleEvent.class, e -> {
        if (blacklist.contains(e.getModule()) || nullCheck()) return;
        if (e.getAction() == ToggleEvent.Action.ENABLE) {
            ChatUtil.INSTANCE.sendMessage("" + ChatFormatting.WHITE + ChatFormatting.BOLD + e.getModule().getLabel() + " : " + ChatFormatting.GREEN + "Enabled", true);
        } else {
            ChatUtil.INSTANCE.sendMessage("" + ChatFormatting.WHITE + ChatFormatting.BOLD + e.getModule().getLabel() + " : " + ChatFormatting.RED + "Disabled", true);
        }
    });

}
