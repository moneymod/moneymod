package wtf.moneymod.client;

import akka.io.TcpListener;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.Display;
import wtf.moneymod.client.api.forge.EventHandler;
import wtf.moneymod.client.api.management.impl.*;
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
    private FpsManagement fpsManagement;
    private FriendManagement friendManagement;
    private ModuleManagement moduleManagement;
    private CommandManagement commandManagement;
    private RotationManagement rotationManagement;

    public void init() {
        Display.setTitle("M0n3yM0d slatt_ *");
        System.out.println("init");
        fpsManagement = new FpsManagement();
        rotationManagement = new RotationManagement();
        moduleManagement = new ModuleManagement().register();
        commandManagement = new CommandManagement().register();
        friendManagement = new FriendManagement().register();
        ConfigManager.getInstance().load();
        screen = new Screen();
        MinecraftForge.EVENT_BUS.register(new EventHandler());
        MinecraftForge.EVENT_BUS.register(PacketManagement.getInstance());
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

    public FriendManagement getFriendManagement() {
        return friendManagement;
    }

    public FpsManagement getFpsManagement(){return fpsManagement;}

    public RotationManagement getRotationManagement(){return rotationManagement;}

    public Screen getScreen() {
        return screen;
    }

}
