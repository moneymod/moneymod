package wtf.moneymod.client.impl.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.management.impl.FriendManagement;
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
        super("f add/del <nick>", "f", "Friend", "friend");
    }

    @Override public void execute(String[] str) {
        str = Arrays.copyOfRange(str, 1, str.length);
        if (str.length < 2) {
            sendUsage();
            return;
        } else {
            switch (str[0].toLowerCase()) {
                case "add":
                    if (FriendManagement.getInstance().is(str[1].toLowerCase())) return;
                    FriendManagement.getInstance().add(str[1]);
                    print(ChatFormatting.GRAY + "friend " + ChatFormatting.WHITE + "> " + str[1].toLowerCase() + " " + ChatFormatting.GREEN + "added");
                    break;
                case "del":
                    FriendManagement.getInstance().remove(str[1]);
                    print(ChatFormatting.GRAY + "friend " + ChatFormatting.WHITE + "> " + str[1].toLowerCase() + " " + ChatFormatting.RED + "deleted");
                    break;
            }
        }
    }
}
