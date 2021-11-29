package wtf.moneymod.client.impl.command.impl;

import wtf.moneymod.client.api.management.impl.ConfigManagement;
import wtf.moneymod.client.impl.command.Command;
import wtf.moneymod.client.impl.utility.impl.world.ChatUtil;

public class SaveCommand extends Command {

    public SaveCommand() {
        super("save", "save");
    }

    @Override public void execute(String[] args) {
        try {
            ConfigManagement.getInstance().start();
            ChatUtil.INSTANCE.sendMessage("Config saved", true);
        } catch (Exception e) {

        }
    }

}
