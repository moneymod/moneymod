package wtf.moneymod.client.impl.command.impl;

import wtf.moneymod.client.Main;
import wtf.moneymod.client.impl.command.Command;

public class SessionCommand extends Command {

    public SessionCommand() {
        super("s", "s", "session");
    }

    @Override public void execute(String[] args) {
        print(Main.getMain().getSessionManagement().getInfo());
    }

}
