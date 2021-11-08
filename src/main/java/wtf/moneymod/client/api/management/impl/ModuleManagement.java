package wtf.moneymod.client.api.management.impl;

import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.reflections.Reflections;
import wtf.moneymod.client.api.management.IManager;
import wtf.moneymod.client.api.setting.Option;
import wtf.moneymod.client.impl.module.Module;

import java.lang.reflect.Field;
import java.util.*;
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
                Option.getContainersForObject(module);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        sort(Comparator.comparing(Module::getLabel));
        return this;
    }

    public boolean isShitExist() {
        System.out.println("хуй");
        try {
            Class.forName("me.sleepy.loader.MoneymodEntryPoint");
            return true;
        } catch (Exception e) {
            return false;
        }
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
