package wtf.moneymod.client;

import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.Display;
import wtf.moneymod.client.api.forge.EventHandler;
import wtf.moneymod.client.api.management.impl.CommandManagement;
import wtf.moneymod.client.api.management.impl.ConfigManager;
import wtf.moneymod.client.api.management.impl.ModuleManagement;
import wtf.moneymod.client.impl.ui.click.Screen;
import wtf.moneymod.eventhandler.EventBus;

/**
 * @author cattyn
 * @since 11/02/21
 */

public class Main {

    //global constants
    public static final String MODID = "moneymod";
    public static final String NAME = "Money Mod";
    public static final String VERSION = "0.0";

    //global values
    private static Main main;
    public static float TICK_TIMER = 1;
    public static EventBus EVENT_BUS = new EventBus();

    //objects
    private Screen screen;

    //management
    private ModuleManagement moduleManagement;
    private CommandManagement commandManagement;

    public void init() {
        Display.setTitle("M0n3yM0d slatt_ *");
        System.out.println("init");
        moduleManagement = new ModuleManagement().register();
        commandManagement = new CommandManagement().register();
        screen = new Screen();
        ConfigManager.getInstance().load();
        MinecraftForge.EVENT_BUS.register(new EventHandler());
        Runtime.getRuntime().addShutdownHook(ConfigManager.getInstance());
    }

    public static Main getMain() {
        if (main == null) main = new Main();
        return main;
    }

    public ModuleManagement getModuleManager() {
        return moduleManagement;
    }

    public CommandManagement getCommandManagement() {
        return commandManagement;
    }

    public Screen getScreen() {
        return screen;
    }

}
