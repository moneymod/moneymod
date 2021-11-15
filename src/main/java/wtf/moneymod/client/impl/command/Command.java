package wtf.moneymod.client.impl.command;

import wtf.moneymod.client.impl.utility.Globals;
import wtf.moneymod.client.impl.utility.impl.world.ChatUtil;

/**
 * @author cattyn
 * @since 11/02/21
 */

public abstract class Command implements ICommand, Globals {

    private String label, syntax;
    private String[] alias;

    public Command(String syntax, String... alias) {
        this.alias = alias;
        this.syntax = syntax;
        this.label = alias[ 0 ];
    }

    public String getLabel() { return label; }

    public String[] getAlias() { return alias; }

    public String getSyntax() { return syntax; }


    @Override public abstract void execute(String[] args);

    protected void print(String message){
        ChatUtil.INSTANCE.sendMessage(message);
    }

    protected void sendUsage() { ChatUtil.INSTANCE.sendMessage("Invalid Usage! " + getSyntax()); }

}
