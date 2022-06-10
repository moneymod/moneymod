package wtf.moneymod.client.impl.module.global;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.command.impl.WatermarkCommand;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.render.ColorUtil;
import wtf.moneymod.client.impl.utility.impl.render.JColor;
import wtf.moneymod.client.impl.utility.impl.render.Renderer2D;
import wtf.moneymod.client.impl.utility.impl.render.fonts.FontRender;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Module.Register(label = "Hud", cat = Module.Category.GLOBAL)
public class Hud extends Module {
    @Value(value = "Static Color") public boolean staticColor = true;
    @Value(value = "Watermark") public boolean watermark = true;
    @Value(value = "Build Date") public boolean builddate = true;
    @Value(value = "Ping") public boolean ping = true;
    @Value(value = "Fps") public boolean fps = true;
    @Value(value = "Welcomer") public boolean welcomer = true;
    @Value(value = "Coords") public boolean coords = true;
    @Value(value = "Armor") public boolean armor = true;
    @Value(value = "Facing") public boolean facing = true;
    @Value(value = "Feature List") public boolean features = true;
    @Value(value = "Offset") @Bounds(max = 5) public int offset = 1;
    @Value(value = "Color") public JColor color = new JColor(255, 255, 255, false);

    public static RenderItem itemRender = mc.getRenderItem();


    @Override public void onRender2D() {
        int ofs = 1; int offsets = 1;

        ScaledResolution sr = new ScaledResolution(mc);

        if (coords){
            //SHIT CODE
            String position = "null";
            double x = mc.player.posX, y = mc.player.posY, z = mc.player.posZ;
            /** Which dimension the player is in (-1 = the Nether, 0 = normal world) */
            if (mc.player.dimension == -1) {
                position = String.format("%.2f, %.2f, %.2f [%.2f, %.2f]", x, y, z, x * 8, z * 8);
            } else if (mc.player.dimension == 0) {
                position = String.format("%.2f, %.2f, %.2f [%.2f, %.2f]", x, y, z, x / 8, z / 8);
            } else {
                position = String.format("%.2f, %.2f, %.2f", x, y, z);
            }

            FontRender.drawStringWithShadow(position,1,  (mc.ingameGUI.getChatGUI().getChatOpen() ? sr.getScaledHeight() - 23 : sr.getScaledHeight() - 11) + ofs,  getColor(1));
            ofs -= FontRender.getFontHeight() + offset;
        }

        if (armor){
            GlStateManager.enableTexture2D();
            int i = sr.getScaledWidth() / 2;
            int iteration = 0;
            int y = sr.getScaledHeight() - 55 - (mc.player.isInWater() && mc.playerController.gameIsSurvivalOrAdventure() ? 10 : 0);
            for (ItemStack is : mc.player.inventory.armorInventory) {
                ++iteration;
                if (is.isEmpty()) continue;
                int x = i - 90 + (9 - iteration) * 20 + 2;
                GlStateManager.enableDepth();
                itemRender.zLevel = 200.0f;
                itemRender.renderItemAndEffectIntoGUI(is, x, y);
                itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, is, x, y, "");
                itemRender.zLevel = 0.0f;
                GlStateManager.enableTexture2D();
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                String s = is.getCount() > 1 ? is.getCount() + "" : "";
                FontRender.drawStringWithShadow(s, x + 19 - 2 - FontRender.getStringWidth(s), y + 9, 0xFFFFFF);
                int dmg = 0;
                int itemDurability = is.getMaxDamage() - is.getItemDamage();
                float green = ((float)is.getMaxDamage() - (float)is.getItemDamage()) / (float)is.getMaxDamage();
                float red = 1.0f - green;
                dmg = 100 - (int)(red * 100.0f);
                FontRender.drawStringWithShadow(dmg + "", x + 8 - FontRender.getStringWidth(dmg + "") / 2, y - 11, new Color((int)(red * 255.0f), (int)(green * 255.0f), 0).getRGB());
            }
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
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
            FontRender.drawStringWithShadow(facing, 1, (mc.ingameGUI.getChatGUI().getChatOpen() ? sr.getScaledHeight() - 23 : sr.getScaledHeight() - 11) + ofs,  getColor(1));
            ofs -= FontRender.getFontHeight() + offset;
        }

        if (watermark) {
            //FontRender.drawStringWithShadow("moneymod", 1, 1,  getColor(1));
            FontRender.drawStringWithShadow(WatermarkCommand.watermark + " b-", 1, 1,  getColor(1));
        }

        if (fps){
            String fps = "Fps " + Main.getMain().getFpsManagement().getFPS();
            FontRender.drawStringWithShadow(fps, sr.getScaledWidth() - FontRender.getStringWidth(fps) - 2, (sr.getScaledHeight() - 11) + offsets,  getColor(1));
            offsets -= FontRender.getFontHeight() + offset;
        }
        if (ping) {
            String ping = "Ping " + getPlayerPing();
            FontRender.drawStringWithShadow(ping, sr.getScaledWidth() - FontRender.getStringWidth(ping) - 2 ,(sr.getScaledHeight() - 11) + offsets,  getColor(1));
            offsets -= FontRender.getFontHeight() + offset;
        }
        if (welcomer){
            String text = "Welcome to " + WatermarkCommand.watermark + "!";
            FontRender.drawStringWithShadow(text, (int)(sr.getScaledWidth() / 2f - FontRender.getStringWidth(text) / 2f),1, getColor(1));
        }

        if (features) {
            ArrayList<Module> modules = Main.getMain().getModuleManager().stream().sorted(Comparator.comparing(m -> -FontRender.getStringWidth(m.getLabel()))).collect(Collectors.toCollection(ArrayList::new));
            int y = 0, count = 1;
            for (Module module : modules) {
                if (!module.isToggled() || !module.drawn) continue;
                FontRender.drawStringWithShadow(module.getLabel(), sr.getScaledWidth() - FontRender.getStringWidth(module.getLabel()) - 2, y + 1, getColor(y * 2));
                y += FontRender.getFontHeight() + offset;
                count++;
            }
        }
    }

    private int getColor(int diff) {
        if (staticColor){
            return color.getColor().getRGB();
        }
        return ColorUtil.injectBrightness(color.getColor(), Main.getMain().getPulseManagement().getDifference(diff) / 255f).getRGB();
    }

    static int getPlayerPing() {
        try {
            return Objects.requireNonNull(mc.getConnection()).getPlayerInfo(mc.getConnection().getGameProfile().getId()).getResponseTime();
        } catch (Exception e) {
            return 0;
        }
    }
}
