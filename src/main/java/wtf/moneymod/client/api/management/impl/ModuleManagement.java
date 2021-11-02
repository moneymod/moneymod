package wtf.moneymod.client.api.management.impl;

import club.cafedevelopment.reflectionsettings.container.SettingManager;
import org.reflections.Reflections;
import wtf.moneymod.client.api.management.IManager;
import wtf.moneymod.client.impl.module.Module;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author cattyn
 * @since 11/02/21
 */

public class ModuleManagement implements IManager<ModuleManagement> {

    ArrayList<Module> modules = new ArrayList<>();

    @Override public ModuleManagement register() {
        new Reflections("wtf.moneymod.client.impl.module").getSubTypesOf(Module.class).forEach(c -> {
            try {
                Module module = c.newInstance();
                modules.add(module);
                SettingManager.getInstance().acquireFrom(module);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        modules.sort(Comparator.comparing(Module::getLabel));
        return this;
    }

    public ArrayList<Module> get() {
        return modules;
    }

    public ArrayList<Module> get(Predicate<Module> predicate) {
        return modules.stream().filter(predicate).collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Module> get(Module.Category category) {
        return get(m -> m.getCategory().equals(category));
    }

    public Module getFirst(Predicate<Module> predicate) {
        return modules.stream().filter(predicate).findFirst().orElse(null);
    }

    public Module get(String name) {
        return getFirst(m -> m.getLabel().equalsIgnoreCase(name));
    }

    public Module get(Class<? extends Module> clazz) {
        return getFirst(m -> m.getClass().equals(clazz));
    }

}
