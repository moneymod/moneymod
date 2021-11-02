package wtf.moneymod.client.impl.ui.click.buttons.settings;

import club.cafedevelopment.reflectionsettings.container.SettingContainer;
import net.minecraft.client.gui.Gui;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.impl.module.global.ClickGui;
import wtf.moneymod.client.impl.ui.click.Component;
import wtf.moneymod.client.impl.ui.click.buttons.ModuleButton;
import wtf.moneymod.client.impl.ui.click.buttons.settings.sub.SubMode;
import wtf.moneymod.client.impl.utility.impl.misc.SettingUtils;

import java.awt.*;
import java.util.ArrayList;

public class ModeButton extends Component {

    private final SettingContainer setting;
    private final ArrayList<SubMode> modes;
    private final ModuleButton button;
    private boolean isHovered, open;
    private int offset;
    private int x;
    private int y;
    private int modeIndex;

    public ModeButton(final SettingContainer setting, final ModuleButton button, final int offset) {
        this.setting = setting;
        this.button = button;
        this.x = button.panel.getX() + button.panel.getWidth();
        this.y = button.panel.getY() + button.offset;
        this.offset = offset;
        this.modeIndex = 0;
        this.open = false;
        this.modes = new ArrayList<>();
        int off = 12;
        for (Enum e : (( Enum ) setting.getValue()).getClass().getEnumConstants()) {
            modes.add(new SubMode(this, SettingUtils.getProperName(e), off));
            off += 12;
        }
    }

    @Override public void setOffset(final int offset) {
        this.offset = offset;
    }

    @Override public void mouseClicked(final double mouseX, final double mouseY, final int button) {
        modes.forEach(v -> v.mouseClicked(mouseX, mouseY, button));
        if (isHovered(mouseX, mouseY) && this.button.open) {
            if (button == 0) {
                setting.setValue(SettingUtils.increaseEnum(setting.getValue()));
            }
            if (button == 1) {
                open = !open;
                this.button.panel.update();
            }
        }
    }

    @Override public void updateComponent(final double mouseX, final double mouseY) {
        isHovered = isHovered(mouseX, mouseY);
        y = button.panel.getY() + this.offset;
        x = button.panel.getX();
    }

    @Override public void render(int mouseX, int mouseY) {
        Gui.drawRect(button.panel.getX(), button.panel.getY() + offset, button.panel.getX() + button.panel.getWidth(), button.panel.getY() + offset + 12, isHovered ? new Color(0, 0, 0, 160).getRGB() : new Color(0, 0, 0, 140).getRGB());
        mc.fontRenderer.drawStringWithShadow(setting.getId(), button.panel.getX() + 5, button.panel.getY() + offset + 2, -1);
        mc.fontRenderer.drawStringWithShadow(SettingUtils.getProperName(setting.getValue()), button.panel.getX() + button.panel.getWidth() - 5 - mc.fontRenderer.getStringWidth(SettingUtils.getProperName(setting.getValue())), button.panel.getY() + offset + ((( ClickGui ) Main.getMain().getModuleManager().get(ClickGui.class)).bounding ? (isHovered ? 1 : 2) : 2), -1);
        if (open) modes.forEach(m -> m.render(mouseX, mouseY));
    }

    public boolean isHovered(final double x, final double y) {
        return x > this.x && x < this.x + 110 && y > this.y && y < this.y + 12;
    }

    @Override public int getHeight() {
        if (open) return 12 + setting.getValue().getClass().getEnumConstants().length * 12;
        return super.getHeight();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public ModuleButton getButton() {
        return button;
    }

    public int getOffset() {
        return offset;
    }

    public boolean isOpen() {
        return open;
    }

    public SettingContainer getSetting() {
        return setting;
    }

}
