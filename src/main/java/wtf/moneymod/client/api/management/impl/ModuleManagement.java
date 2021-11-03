package wtf.moneymod.client.api.management.impl;

import club.cafedevelopment.reflectionsettings.container.SettingContainer;
import club.cafedevelopment.reflectionsettings.container.SettingManager;
import com.google.common.reflect.ClassPath;
import org.reflections.Reflections;
import wtf.moneymod.client.api.management.IManager;
import wtf.moneymod.client.impl.command.Command;
import wtf.moneymod.client.impl.module.Module;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author cattyn
 * @since 11/02/21
 */

public class ModuleManagement extends ArrayList<Module> implements IManager<ModuleManagement> {

    @Override public ModuleManagement register() {
        new Reflections("wtf.moneymod.client.impl.module").getSubTypesOf(Module.class).forEach(c -> {
            try {
                Module module = c.newInstance();
                add(module);
                SettingManager.getInstance().acquireFrom(module);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        sort(Comparator.comparing(Module::getLabel));
        return this;
    }

    public ArrayList<Module> get(Predicate<Module> predicate) {
        return stream().filter(predicate).collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Module> get(Module.Category category) {
        return get(m -> m.getCategory().equals(category));
    }

    public Module getFirst(Predicate<Module> predicate) {
        return stream().filter(predicate).findFirst().orElse(null);
    }

    public Module get(String name) {
        return getFirst(m -> m.getLabel().equalsIgnoreCase(name));
    }

    public Module get(Class<? extends Module> clazz) {
        return getFirst(m -> m.getClass().equals(clazz));
    }

}
