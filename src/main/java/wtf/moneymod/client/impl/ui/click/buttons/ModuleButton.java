package wtf.moneymod.client.impl.ui.click.buttons;

import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.setting.Option;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.ui.click.Component;
import wtf.moneymod.client.impl.ui.click.Panel;
import wtf.moneymod.client.impl.ui.click.Screen;
import wtf.moneymod.client.impl.ui.click.buttons.settings.*;

import java.util.ArrayList;

public class ModuleButton extends Component {

    public Module module;
    public Panel panel;
    public int offset;
    private boolean isHovered;
    public final ArrayList<Component> components;
    public boolean open;

    private String openText = "+";

    public ModuleButton(Module module, Panel panel, int offset) {
        this.module = module;
        this.panel = panel;
        this.offset = offset;
        this.components = new ArrayList<>();
        this.open = false;
        int settingY = this.offset + 12;
        if (!Option.getContainersForObject(module).isEmpty()) {
            for (Option s : Option.getContainersForObject(module)) {
                if (s.getValue().getClass().isEnum()) {
                    components.add(new ModeButton(s, this, settingY));
                    continue;
                }
                switch (s.getValue().getClass().getSimpleName()) {
                    case "Boolean":
                        components.add(new BooleanButton(s, this, settingY));
                        continue;
                    case "Double":
                    case "Integer":
                    case "Float":
                        components.add(new SliderButton(s, this, settingY));
                        continue;
                    case "JColor":
                        components.add(new ColorButton(s, this, settingY));
                        continue;
                    default:
                        settingY += 12;
                }
            }
        }

        components.add(new KeyButton(this, settingY));
    }

    @Override public void setOffset(final int offset) {
        this.offset = offset;
        int settingY = this.offset + 12;
        for (Component c : components) {
            c.setOffset(settingY);
            settingY += c.getHeight();
        }
    }

    @Override public int getHeight() {
        int height = 12;
        if (open) {
            for (Component component : components) {
                height += component.getHeight();
            }
        }

        return height;
    }

    @Override public void updateComponent(final double mouseX, final double mouseY) {
        isHovered = isHovered(mouseX, mouseY);
        if (!components.isEmpty()) {
            components.forEach(c -> {
                c.updateComponent(mouseX, mouseY);
            });
        }
    }

    @Override public void mouseClicked(final double mouseX, final double mouseY, final int button) {
        if (isHovered(mouseX, mouseY) && button == 0) {
            module.toggle();
        }
        if (isHovered(mouseX, mouseY) && button == 1) {
            open = !open;
            panel.update();
        }
        components.forEach(c -> {
            c.mouseClicked(mouseX, mouseY, button);
        });
    }

    @Override public void mouseReleased(final double mouseX, final double mouseY, final int mouseButton) {
        components.forEach(c -> {
            c.mouseReleased(mouseX, mouseY, mouseButton);
        });
    }

    @Override public void keyTyped(final int key) {
        components.forEach(c -> {
            c.keyTyped(key);
        });
    }

    @Override public void render(int mouseX, int mouseY) {

        Screen.abstractTheme.drawModuleButton(this, panel.getX(), panel.getY() + offset, panel.getWidth(), 12, isHovered(mouseX, mouseY));

        if (open) components.forEach(component -> component.render(mouseX, mouseY));

        if (isHovered)
            Main.getMain().getScreen().getDescriptionManager().update(module.getDesc(), mouseX, mouseY);
    }

    public boolean isHovered(final double x, final double y) {
        return x > this.panel.getX() && x < this.panel.getX() + this.panel.getWidth() && y > this.panel.getY() + this.offset && y < this.panel.getY() + 12 + this.offset;
    }

}
