package wtf.moneymod.client.api.management.impl;

import com.google.common.reflect.ClassPath;
import org.reflections.Reflections;
import wtf.moneymod.client.api.management.IManager;
import wtf.moneymod.client.impl.command.Command;
import wtf.moneymod.client.impl.module.Module;

import java.util.ArrayList;

/**
 * @author cattyn
 * @since 11/02/21
 */

public class CommandManagement extends ArrayList<Command> implements IManager<CommandManagement> {

    String prefix = "$";

    @Override public CommandManagement register() {
        new Reflections("wtf.moneymod.client.impl.command.impl").getSubTypesOf(Command.class).forEach(c -> {
            try {
                add(c.newInstance());
                System.out.println(c.getName());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return this;
    }

    public String getPrefix() {
        return prefix;
    }

}
