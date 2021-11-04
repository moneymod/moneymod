package wtf.moneymod.client.impl.ui.click.buttons.settings;

import club.cafedevelopment.reflectionsettings.container.SettingContainer;
import net.minecraft.client.gui.Gui;
import scala.reflect.runtime.Settings;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.impl.module.global.ClickGui;
import wtf.moneymod.client.impl.ui.click.Component;
import wtf.moneymod.client.impl.ui.click.Screen;
import wtf.moneymod.client.impl.ui.click.buttons.ModuleButton;

import java.awt.*;

public class BooleanButton extends Component {

    private final SettingContainer setting;
    private final ModuleButton button;
    private boolean isHovered;
    private int offset;
    private int x;
    private int y;

    public BooleanButton(SettingContainer setting, final ModuleButton button, final int offset) {
        this.setting = setting;
        this.button = button;
        this.x = button.panel.getX() + button.panel.getWidth();
        this.y = button.panel.getY() + button.offset;
        this.offset = offset;
    }

    @Override public void setOffset(final int offset) {
        this.offset = offset;
    }

    @Override public void updateComponent(final double mouseX, final double mouseY) {
        isHovered = isHovered(mouseX, mouseY);
        y = button.panel.getY() + this.offset;
        x = button.panel.getX();
    }

    @Override public void mouseClicked(final double mouseX, final double mouseY, final int button) {
        if (isHovered(mouseX, mouseY) && button == 0 && this.button.open) {
            setting.setValue(!( boolean ) setting.getValue());
        }
    }

    @Override public void render(int mouseX, int mouseY) {
        Screen.abstractTheme.drawBooleanButton(setting, button.panel.getX(), button.panel.getY() + offset, button.panel.getWidth(),12, isHovered);
    }

    public boolean isHovered(final double x, final double y) {
        return x > this.x && x < this.x + 110 && y > this.y && y < this.y + 12;
    }

}
