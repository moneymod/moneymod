package wtf.moneymod.client.impl.command.impl;

import wtf.moneymod.client.api.management.impl.ConfigManagement;
import wtf.moneymod.client.impl.command.Command;
import wtf.moneymod.client.impl.utility.impl.world.ChatUtil;

public class LoadCommand extends Command {

    public LoadCommand() {
        super("load", "load");
    }

    @Override public void execute(String[] args) {
        ConfigManagement.getInstance().load();
        ChatUtil.INSTANCE.sendMessage("Config loaded", true);
    }

}
