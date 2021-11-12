package wtf.moneymod.client.impl.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.impl.command.Command;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.world.ChatUtil;

import java.util.Arrays;

public class DrawnCommand extends Command {

    public DrawnCommand() {
        super("d <Module>", "drawn", "d");
    }

    @Override public void execute(String[] args) {
        args = Arrays.copyOfRange(args, 1, args.length);
        if (args.length < 1) {
            sendUsage();
        } else {
            Module module = Main.getMain().getModuleManager().get(args[ 0 ]);
            if (module != null) {
                module.drawn = !module.drawn;
                ChatUtil.INSTANCE.sendMessage(module.getLabel() + " " + (module.drawn ? ChatFormatting.GREEN : ChatFormatting.RED) + "drawn", true);
            } else {
                sendUsage();
            }
        }
    }

}
