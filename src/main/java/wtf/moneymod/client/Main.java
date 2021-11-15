package wtf.moneymod.client;

import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.Display;
import wtf.moneymod.client.api.forge.EventHandler;
import wtf.moneymod.client.api.management.impl.*;
import wtf.moneymod.client.impl.ui.click.Screen;
import wtf.moneymod.client.impl.ui.irc.IrcScreen;
import wtf.moneymod.client.impl.utility.impl.render.fonts.CFontRenderer;
import wtf.moneymod.client.impl.utility.impl.cape.CapeThread;
import wtf.moneymod.eventhandler.EventBus;

import java.awt.*;

/**
 * @author cattyn
 * @since 11/02/21
 */

public class Main {

    //global constants
    public static final String MODID = "moneymod";
    public static final String NAME = "Money Mod";
    public static String VERSION = "0.7";

    //global values
    private static Main main;
    public static float TICK_TIMER = 1;
    public static final EventBus EVENT_BUS = new EventBus();

    //objects
    private Screen screen;

    //management
    private PulseManagement pulseManagement;
    private FpsManagement fpsManagement;
    private ModuleManagement moduleManagement;
    private CommandManagement commandManagement;
    private RotationManagement rotationManagement;
    private CFontRenderer fontRenderer;
    private HoleManagement holeManagement;
    private CapeThread capeThread;
    private IrcScreen ircScreen;
    private SessionManagement sessionManagement;

    public void init() {
        System.out.println("init");

        try {
            Font verdanapro = Font.createFont( Font.TRUETYPE_FONT, Main.class.getResourceAsStream( "/fonts/VerdanaPro-Regular.ttf" ) );
            verdanapro = verdanapro.deriveFont( 18.f );
            fontRenderer = new CFontRenderer( verdanapro, true, true );
        } catch ( Exception e ) {
            e.printStackTrace( );
            return;
        }

        capeThread = new CapeThread();
        ircScreen = new IrcScreen();

        sessionManagement = new SessionManagement();
        fpsManagement = new FpsManagement();
        rotationManagement = new RotationManagement();
        holeManagement = new HoleManagement();
        moduleManagement = new ModuleManagement().register();
        commandManagement = new CommandManagement().register();
        ConfigManager.getInstance().load();
        sessionManagement.reset();
        screen = new Screen();
        pulseManagement = new PulseManagement();
        EventHandler handler = new EventHandler();
        MinecraftForge.EVENT_BUS.register(handler);
        EVENT_BUS.register(handler);
        MinecraftForge.EVENT_BUS.register(PacketManagement.getInstance());
        Runtime.getRuntime().addShutdownHook(ConfigManager.getInstance());
        Display.setTitle(String.format("moneymod build-%s", GitInfo.GIT_SHA.substring(0,7)));
    }

    public static Main getMain() {
        if (main == null) main = new Main();
        return main;
    }

    public ModuleManagement getModuleManager() {
        return moduleManagement;
    }

    public CFontRenderer getFontRenderer() {return fontRenderer;}

    public CommandManagement getCommandManagement() {
        return commandManagement;
    }

    public FpsManagement getFpsManagement() {return fpsManagement;}

    public RotationManagement getRotationManagement() {return rotationManagement;}

    public SessionManagement getSessionManagement(){return sessionManagement;}

    public Screen getScreen() {
        return screen;
    }

    public PulseManagement getPulseManagement() {
        return pulseManagement;
    }

    public IrcScreen getIrcScreen(){ return ircScreen; }

    public CapeThread getCapeThread() { return capeThread; }

    public HoleManagement getHoleManagement() {
        return holeManagement;
    }
}
