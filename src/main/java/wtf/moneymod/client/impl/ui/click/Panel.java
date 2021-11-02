package wtf.moneymod.client.impl.ui.click;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.module.global.ClickGui;
import wtf.moneymod.client.impl.ui.click.buttons.ModuleButton;
import wtf.moneymod.client.impl.utility.Globals;

import java.awt.*;
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
        ScaledResolution sr = new ScaledResolution(mc);

        ClickGui clickgui = ( ClickGui ) Main.getMain().getModuleManager().get(ClickGui.class);
        Gui.drawRect(x - 2, y - 2, x + width + 2, y, new Color(255, 255, 255, 90).getRGB());
        Gui.drawRect(x, y, x + width, y + barHeight - 2, new Color(0, 0, 0, 210).getRGB());
        if (open)
            Gui.drawRect(x, y + barHeight - 2, x + width, y + barHeight, new Color(255, 255, 255, 90).getRGB());
        mc.fontRenderer.drawStringWithShadow(category.name() + ChatFormatting.WHITE + " (" + components.size() + ")", x + 3, y + 4, Screen.color.getRGB());
        mc.fontRenderer.drawStringWithShadow(open ? "-" : "+", x + width - 10, y + 4, Color.GRAY.getRGB());
        if (open && !components.isEmpty()) {
            components.forEach(component -> {
                component.render(mouseX, mouseY);
                if (component instanceof ModuleButton) {
                    ModuleButton button = ( ModuleButton ) component;
                }
            });
        }
        int localHeight = open ? height : barHeight - 2;
        Gui.drawRect(x - 2, y, x, y + localHeight, new Color(255, 255, 255, 90).getRGB());
        Gui.drawRect(x + width, y, x + width + 2, y + localHeight, new Color(255, 255, 255, 90).getRGB());
        Gui.drawRect(x - 2, y + localHeight, x + width + 2, y + localHeight + 2, new Color(255, 255, 255, 90).getRGB());
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

}
