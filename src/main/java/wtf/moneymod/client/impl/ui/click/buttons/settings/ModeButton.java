package wtf.moneymod.client.impl.ui.click.buttons.settings;

import wtf.moneymod.client.api.setting.Option;
import wtf.moneymod.client.impl.ui.click.Component;
import wtf.moneymod.client.impl.ui.click.Screen;
import wtf.moneymod.client.impl.ui.click.buttons.ModuleButton;
import wtf.moneymod.client.impl.ui.click.buttons.settings.sub.SubMode;
import wtf.moneymod.client.impl.utility.impl.misc.SettingUtils;

import java.util.ArrayList;

public class ModeButton extends Component {

    private final Option<Enum> setting;
    private final ArrayList<SubMode> modes;
    private final ModuleButton button;
    private boolean isHovered, open;
    private int offset;
    private int x;
    private int y;
    private int modeIndex;

    public ModeButton(final Option<Enum> setting, final ModuleButton button, final int offset) {
        this.setting = setting;
        this.button = button;
        this.x = button.panel.getX() + button.panel.getWidth();
        this.y = button.panel.getY() + button.offset;
        this.offset = offset;
        this.modeIndex = 0;
        this.open = false;
        this.modes = new ArrayList<>();
        int off = 12;
        for (Enum e : setting.getValue().getClass().getEnumConstants()) {
            modes.add(new SubMode(this, SettingUtils.INSTANCE.getProperName(e), off));
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
                setting.setValue(SettingUtils.INSTANCE.increaseEnum(setting.getValue()));
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
        Screen.abstractTheme.drawModeButton(setting, button.panel.getX(), button.panel.getY() + offset, button.panel.getWidth(),12, isHovered);

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

    public Option<Enum> getSetting() {
        return setting;
    }

}
