package wtf.moneymod.client;

import akka.io.TcpListener;
import com.google.common.hash.Hashing;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.Display;
import wtf.moneymod.client.api.forge.EventHandler;
import wtf.moneymod.client.api.management.impl.*;
import wtf.moneymod.client.impl.ui.click.Screen;
import wtf.moneymod.client.impl.utility.Globals;
import wtf.moneymod.client.impl.utility.impl.render.fonts.CFontRenderer;
import wtf.moneymod.eventhandler.EventBus;

import java.awt.*;
import java.nio.charset.StandardCharsets;

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

        fpsManagement = new FpsManagement();
        rotationManagement = new RotationManagement();
        holeManagement = new HoleManagement();
        moduleManagement = new ModuleManagement().register();
        VERSION = getHash(moduleManagement);
        commandManagement = new CommandManagement().register();
        ConfigManager.getInstance().load();
        screen = new Screen();
        pulseManagement = new PulseManagement();
        EventHandler handler = new EventHandler();
        MinecraftForge.EVENT_BUS.register(handler);
        EVENT_BUS.register(handler);
        MinecraftForge.EVENT_BUS.register(PacketManagement.getInstance());
        Runtime.getRuntime().addShutdownHook(ConfigManager.getInstance());
        Display.setTitle(String.format("moneymod build-%s", VERSION));
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

    public Screen getScreen() {
        return screen;
    }

    public PulseManagement getPulseManagement() {
        return pulseManagement;
    }

    public HoleManagement getHoleManagement() {
        return holeManagement;
    }

    private String getHash(ModuleManagement module) {
        StringBuilder sb = new StringBuilder();
        module.forEach(m -> sb.append(m.getClass().hashCode()));
        sb.append(getClass().hashCode());
        return Hashing.sha256().hashString(sb.toString(), StandardCharsets.UTF_8).toString().substring(0, 10);
    }

}
