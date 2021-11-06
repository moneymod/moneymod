package wtf.moneymod.client.impl.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.setting.Option;
import wtf.moneymod.client.impl.command.Command;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.misc.SettingUtils;
import wtf.moneymod.client.impl.utility.impl.world.ChatUtil;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;


public class FriendCommand extends Command {

    public FriendCommand() {
        super("<ModuleName> || <ModuleName> <SettingName/List> || <ModuleName> <SettingName> <NewValue>",
                Main
                        .getMain()
                        .getModuleManager()
                        .stream()
                        .map(m -> m.getLabel().toLowerCase())
                        .toArray(String[]::new));
    }

    @Override public void execute(String[] str) {
        str = Arrays.copyOfRange(str, 1, str.length);
        if (str.length < 2) {
            return;
        } else {
            switch (str[0].toLowerCase()) {
                case "add":
                    if (Main.getMain().getFriendManagement().is(str[1].toLowerCase())) return;
                    Main.getMain().getFriendManagement().add(str[1]);
                    ChatUtil.INSTANCE.sendMessage(ChatFormatting.GRAY + "friend " + ChatFormatting.WHITE + "> " + str[1].toLowerCase() + " " + ChatFormatting.GREEN + "added");
                    break;
                case "del":
                    Main.getMain().getFriendManagement().remove(str[1]);
                    ChatUtil.INSTANCE.sendMessage(ChatFormatting.GRAY + "friend " + ChatFormatting.WHITE + "> " + str[1].toLowerCase() + " " + ChatFormatting.RED + "deleted");
                    break;
            }
        }
    }
}
