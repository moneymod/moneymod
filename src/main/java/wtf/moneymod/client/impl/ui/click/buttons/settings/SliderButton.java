package wtf.moneymod.client.impl.ui.click.buttons.settings;

import club.cafedevelopment.reflectionsettings.container.SettingContainer;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.Gui;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.impl.module.global.ClickGui;
import wtf.moneymod.client.impl.ui.click.Component;
import wtf.moneymod.client.impl.ui.click.Screen;
import wtf.moneymod.client.impl.ui.click.buttons.ModuleButton;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class SliderButton extends Component {

    private final SettingContainer setting;
    private final ModuleButton button;
    private boolean isHovered;
    private int offset;
    private int x;
    private int y;
    private boolean dragging;
    private double renderWidth;

    public SliderButton(SettingContainer setting, ModuleButton button, int offset) {
        this.setting = setting;
        this.button = button;
        this.x = button.panel.getX() + button.panel.getWidth();
        this.y = button.panel.getY() + button.offset;
        this.offset = offset;
    }

    private static double round(final double value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Override public void setOffset(final int offset) {
        this.offset = offset;
    }

    @Override public void mouseClicked(final double mouseX, final double mouseY, final int button) {
        if (isHovered(mouseX, mouseY) && button == 0 && this.button.open) {
            dragging = true;
        }
    }

    @Override public void mouseReleased(final double mouseX, final double mouseY, final int mouseButton) {
        dragging = false;
    }

    @Override public void updateComponent(final double mouseX, final double mouseY) {
        isHovered = isHovered(mouseX, mouseY);
        y = button.panel.getY() + offset;
        x = button.panel.getX();
        final double diff = Math.min(106, Math.max(0, mouseX - x));
        final double min = setting.getMin();
        final double max = setting.getMax();
        double inc = setting.getValue().getClass().getSimpleName().equalsIgnoreCase("Integer") ? 1 : 0.1;
        renderWidth = 104 * ((inc == 1 ? ( int ) setting.getValue() : ( double ) setting.getValue()) - min) / (max - min);
        if (dragging) {
            if (diff == 0) setting.setValue(setting.getMin());
            else {
                final double newValue = round(diff / 104 * (max - min) + min, 1);
                double precision = 1.0D / inc;
                if(inc == 0.1) setting.setValue(Math.round(Math.max(min, Math.min(max, newValue)) * precision) / precision);
                else setting.setValue((int) Math.max(min, Math.min(max, newValue)));
            }
        }
    }

    @Override public void render(int mouseX, int mouseY) {
        Gui.drawRect(button.panel.getX(), button.panel.getY() + offset, button.panel.getX() + button.panel.getWidth(), button.panel.getY() + offset + 12, isHovered ? new Color(0, 0, 0, 160).getRGB() : new Color(0, 0, 0, 140).getRGB());
        Gui.drawRect(button.panel.getX() + 3, button.panel.getY() + offset + 11, ( int ) (button.panel.getX() + 3 + renderWidth), button.panel.getY() + offset + 10, isHovered ? Screen.color.darker().getRGB() : Screen.color.getRGB());
        mc.fontRenderer.drawStringWithShadow(setting.getId() + ": " + ChatFormatting.GRAY + setting.getValue(), button.panel.getX() + 5, button.panel.getY() + offset + ((( ClickGui ) Main.getMain().getModuleManager().get(ClickGui.class)).bounding ? (isHovered ? 0 : 1.5f) : 1.5f), isHovered ? new Color(170, 170, 170).getRGB() : -1);        //FontUtil.drawText(String.valueOf(round(setting.getValue(), setting.getDecimalPlaces())), button.panel.getX() + button.panel.getWidth() - 1 - FontUtil.getTextWidth(String.valueOf(round(setting.getValue(), setting.getDecimalPlaces()))), button.panel.getY() + offset + 1, isHovered ? new Color(170, 170, 170).getRGB() : -1);
    }

    public boolean isHovered(final double x, final double y) {
        return x > this.x && x < this.x + 110 && y > this.y && y < this.y + 12;
    }

}
