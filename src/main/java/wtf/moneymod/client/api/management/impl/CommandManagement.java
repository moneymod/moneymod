package wtf.moneymod.client.api.management.impl;

import org.reflections.Reflections;
import wtf.moneymod.client.api.management.IManager;
import wtf.moneymod.client.impl.command.Command;
import wtf.moneymod.client.impl.module.Module;

import java.util.ArrayList;

/**
 * @author cattyn
 * @since 11/02/21
 */

public class CommandManagement implements IManager<CommandManagement> {

    String prefix = "$";
    ArrayList<Command> commands = new ArrayList<>();

    @Override public CommandManagement register() {
        new Reflections("wtf.moneymod.client.impl.command").getSubTypesOf(Command.class).forEach(c -> {
            try {
                commands.add(c.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        commands.forEach(c -> {
            for (int j = 0; j < c.getAlias().length; j++) {
                System.out.println(c.getAlias()[j]);
            }
        });
        return this;
    }

    public String getPrefix() {
        return prefix;
    }

    public ArrayList<Command> getCommands() {
        return commands;
    }

}
