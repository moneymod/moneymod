package wtf.moneymod.client;

import net.minecraftforge.common.MinecraftForge;
import wtf.moneymod.client.api.forge.EventHandler;
import wtf.moneymod.client.api.managment.impl.ModuleManager;

public class Main {

    public static final String MODID = "moneymod";
    public static final String NAME = "Money Mod";
    public static final String VERSION = "0.0";

    private static Main main;
    private ModuleManager moduleManager;

    public void init() {
        System.out.println("init");
        moduleManager = new ModuleManager().register();
        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }

    public static Main getMain() {
        if (main == null) main = new Main();
        return main;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

}
