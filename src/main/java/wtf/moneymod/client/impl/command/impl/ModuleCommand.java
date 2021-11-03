package wtf.moneymod.client.impl.command.impl;

import club.cafedevelopment.reflectionsettings.container.SettingContainer;
import club.cafedevelopment.reflectionsettings.container.SettingManager;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.impl.command.Command;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.world.ChatUtil;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author cattyn
 * @since 11/02/21
 */

public class ModuleCommand extends Command {

    public ModuleCommand() {
        super("<ModuleName> || <ModuleName> <SettingName/List> || <ModuleName> <SettingName> <NewValue>",
                Main
                        .getMain()
                        .getModuleManager()
                        .stream()
                        .map(m -> m.getLabel().toLowerCase())
                        .toArray(String[]::new));
    }

    @Override public void execute(String[] args) {
        Module module = Main.getMain().getModuleManager().get(args[ 0 ]);
        if (args.length == 1) {
            module.toggle();
            return;
        }
        Optional<SettingContainer> container = SettingManager.getInstance().getByTargetAndId(module, args[ 1 ], true);
        if (args.length == 2) {
            if (args[ 1 ].equalsIgnoreCase("list")) {
                ChatUtil.INSTANCE.sendMessage(SettingManager.getInstance().acquireFrom(module).stream().map(s -> String.format("%s[%s]", s.getId(), s.getValue())).collect(Collectors.joining(", ")));
                return;
            }
            if (container.isPresent()) {
                ChatUtil.INSTANCE.sendMessage(container.get().getDebugInfo() + ", Type: " + container.get().getHost().getClass().getSimpleName());
            } else {
                sendUsage();
            }
            return;
        }
        if (args.length == 3) {
            if (container.isPresent()) {
                try {
                    if (container.get().getValue() instanceof Double) {
                        container.get().setValue(Double.parseDouble(args[ 2 ]));
                    } else if (container.get().getValue() instanceof Float) {
                        container.get().setValue(Float.parseFloat(args[ 2 ]));
                    } else if (container.get().getValue() instanceof Integer) {
                        container.get().setValue(Integer.parseInt(args[ 2 ]));
                    } else if (container.get().getValue() instanceof Boolean) {
                        container.get().setValue(Boolean.parseBoolean(args[ 2 ]));
                    } else if (container.get().getValue().getClass().isEnum()) {
                        ChatUtil.INSTANCE.sendMessage("wip");
                    } else {
                        System.out.println(container.get().getValue().getClass().getSimpleName());
                    }
                    ChatUtil.INSTANCE.sendMessage(String.format("%s set value to %s", container.get().getId(), container.get().getValue()));
                } catch (Exception e) {
                    sendUsage();
                    e.printStackTrace();
                }
            } else {
                sendUsage();
                System.out.println("he");
            }
        }
    }


}
