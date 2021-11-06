package wtf.moneymod.client.impl.ui.click;

import wtf.moneymod.client.Main;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.ui.click.buttons.ModuleButton;
import wtf.moneymod.client.impl.utility.Globals;

import java.util.ArrayList;

public class Panel implements Globals {

    public Module.Category category;
    public ArrayList<Component> components;
    private boolean open;
    private final int width;
    private int y;
    private int x;
    private final int barHeight;
    private boolean isDragging;
    public int dragX;
    public int dragY;
    private int height;

    public Panel(Module.Category category) {
        this.category = category;
        this.components = new ArrayList<>();
        this.width = 110;
        this.barHeight = 18;
        this.dragX = 0;
        this.open = true;
        this.isDragging = false;
        int componentY = this.barHeight;
        for (Module m : Main.getMain().getModuleManager().get(category)) {
            ModuleButton moduleButton = new ModuleButton(m, this, componentY);
            components.add(moduleButton);
            componentY += 12;
        }

        update();
    }

    public void renderPanel(int mouseX, int mouseY) {

        Screen.abstractTheme.drawHeader(this, x, y, width, barHeight, isHover(mouseX, mouseY));

        if (open && !components.isEmpty()) {
            components.forEach(component -> {
                component.render(mouseX, mouseY);
                if (component instanceof ModuleButton) {
                    ModuleButton button = ( ModuleButton ) component;
                }
            });
        }

        Screen.abstractTheme.drawPanelOutline(this, x, y, width, height, isHover(mouseX, mouseY));
    }

    public ArrayList<Component> getComponents() {
        return this.components;
    }

    public void setDrag(final boolean drag) {
        this.isDragging = drag;
    }

    public boolean isOpen() {
        return this.open;
    }

    public void setOpen(final boolean open) {
        this.open = open;
    }

    public void update() {
        int off = this.barHeight;
        for (final Component comp : this.components) {
            comp.setOffset(off);
            off += comp.getHeight();
        }
        this.height = off;
    }

    public int getX() {
        return this.x;
    }

    public void setX(final int newX) {
        this.x = newX;
    }

    public int getY() {
        return this.y;
    }

    public void setY(final int newY) {
        this.y = newY;
    }

    public int getWidth() {
        return this.width;
    }

    public void updatePosition(final int mouseX, final int mouseY) {
        if (this.isDragging) {
            this.setX(mouseX - this.dragX);
            this.setY(mouseY - this.dragY);
        }
    }

    public boolean isHover(final double x, final double y) {
        return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.barHeight;
    }

    public int getBarHeight() {
        return barHeight;
    }

}
