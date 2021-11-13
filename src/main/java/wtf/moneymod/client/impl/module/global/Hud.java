package wtf.moneymod.client.impl.module.global;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.render.fonts.FontRender;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;

@Module.Register(label = "Hud", cat = Module.Category.GLOBAL)
public class Hud extends Module {

    @Value(value = "Watermark") public boolean watermark = true;
    @Value(value = "Ping") public boolean ping = true;
    @Value(value = "Fps") public boolean fps = true;
    @Value(value = "Welcomer") public boolean welcomer = true;
    @Value(value = "Coords") public boolean coords = true;
    @Value(value = "Facing") public boolean facing = true;
    @Value(value = "Feature List") public boolean features = true;
    @Value(value = "Offset") @Bounds(max = 5) public int offset = 1;

    @SubscribeEvent
    public void onRenderer2D(RenderGameOverlayEvent.Text event) {
        int ofs = 1; int offsets = 1;

        ScaledResolution sr = new ScaledResolution(mc);

        if (coords){
            //SHIT CODE
            String position = "null";
            int x = (int) mc.player.posX; int y = (int) mc.player.posY; int z = (int) mc.player.posZ;
            /** Which dimension the player is in (-1 = the Nether, 0 = normal world) */
            switch (mc.player.dimension){
                case -1:
                    position = String.format("%s, %s, %s : [%s,%s]",x,y,z, x * 8, z * 8);
                    break;
                case 0:
                    position = String.format("%s, %s, %s : [%s,%s]",x,y,z, x / 8, z / 8);
                    break;
            }
            FontRender.drawStringWithShadow(position,1,  (mc.ingameGUI.getChatGUI().getChatOpen() ? sr.getScaledHeight() - 23 : sr.getScaledHeight() - 11) + ofs, Color.HSBtoRGB(0, 0, Main.getMain().getPulseManagement().getDifference(1) / 255f));
            ofs -= FontRender.getFontHeight() + offset;
        }

        if (facing){
            String facing = "null";
            switch (mc.player.getHorizontalFacing()){
                case EAST:
                    facing = "East [+X]";
                    break;
                case SOUTH:
                    facing = "South [+Z]";
                    break;
                case WEST:
                    facing = "West [-X]";
                    break;
                case NORTH:
                    facing = "North [-Z]";
                    break;
            }
            FontRender.drawStringWithShadow(facing, 1, (mc.ingameGUI.getChatGUI().getChatOpen() ? sr.getScaledHeight() - 23 : sr.getScaledHeight() - 11) + ofs, Color.HSBtoRGB(0, 0, Main.getMain().getPulseManagement().getDifference(1) / 255f));
            ofs -= FontRender.getFontHeight() + offset;
        }

        if (watermark) {
            FontRender.drawStringWithShadow("moneymod", 1, 1, Color.HSBtoRGB(0, 0, Main.getMain().getPulseManagement().getDifference(1) / 255f));
        }
        if (fps){
            String fps = "Fps " + Main.getMain().getFpsManagement().getFPS();
            FontRender.drawStringWithShadow(fps, sr.getScaledWidth() - FontRender.getStringWidth(fps) - 2, (sr.getScaledHeight() - 11) + offsets, Color.HSBtoRGB(0,0, Main.getMain().getPulseManagement().getDifference(1) / 255f));
            offsets -= FontRender.getFontHeight() + offset;
        }
        if (ping) {
            String ping = "Ping " + getPlayerPing();
            FontRender.drawStringWithShadow(ping, sr.getScaledWidth() - FontRender.getStringWidth(ping) - 2 ,(sr.getScaledHeight() - 11) + offsets, Color.HSBtoRGB(0,0,Main.getMain().getPulseManagement().getDifference(1) / 255f));
            offsets -= FontRender.getFontHeight() + offset;
        }
        if (welcomer){
            String text = "Welcome to Moneymod!";
            FontRender.drawStringWithShadow(text, (int)(sr.getScaledWidth() / 2f - FontRender.getStringWidth(text) / 2f),1, Color.HSBtoRGB(0,0, Main.getMain().getPulseManagement().getDifference(1) / 255f));
        }

        if (features) {
            ArrayList<Module> modules = Main.getMain().getModuleManager().stream().sorted(Comparator.comparing(m -> -FontRender.getStringWidth(m.getLabel()))).collect(Collectors.toCollection(ArrayList::new));
            int y = 0, count = 1;
            for (Module module : modules) {
                if (!module.isToggled() || !module.drawn) continue;
                FontRender.drawStringWithShadow(module.getLabel(), sr.getScaledWidth() - FontRender.getStringWidth(module.getLabel()) - 2, y + 1, Color.HSBtoRGB(0, 0, Main.getMain().getPulseManagement().getDifference(y * 2) / 255f));
                y += FontRender.getFontHeight() + offset;
                count++;
            }
        }
    }

    static int getPlayerPing() {
        try {
            return Objects.requireNonNull(mc.getConnection()).getPlayerInfo(mc.getConnection().getGameProfile().getId()).getResponseTime();
        } catch (Exception e) {
            return 0;
        }
    }
}
