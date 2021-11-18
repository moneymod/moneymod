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


public class CryptCommand extends Command {

    public CryptCommand() {
        super("crypt", "crypt");
    }

    public static boolean crypting = false;

    @Override public void execute(String[] args) {

        if (crypting){
            print("false");
            crypting = false;
        } else {
            print("true");
            crypting = true;
        }
    }
}
