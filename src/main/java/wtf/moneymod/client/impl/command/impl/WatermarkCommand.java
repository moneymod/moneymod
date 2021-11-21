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


public class WatermarkCommand extends Command {

    public WatermarkCommand() {
        super("watermak <name>", "watermark");
    }

    public static String watermark = "moneymod";

    @Override public void execute(String[] args) {
        if ( args.length < 2 ) sendUsage();
        else {
            String msg = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
            watermark = msg;
            print("new watermark name! " + msg);
        }
    }
}
