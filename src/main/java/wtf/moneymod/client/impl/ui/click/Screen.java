package wtf.moneymod.client.impl.ui.click;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.module.global.ClickGui;
import wtf.moneymod.client.impl.ui.click.theme.AbstractTheme;
import wtf.moneymod.client.impl.ui.click.theme.impl.NodusTheme;

public class Screen extends GuiScreen {

    public static List<Panel> panels;
    public static Color color;
    public static Description description;
    public static AbstractTheme abstractTheme;

    public Screen() {
        abstractTheme = new NodusTheme();
        panels = new ArrayList<>();
        description = new Description();
        int frameX = 10;

        for (Module.Category category : Module.Category.values()) {
            Panel panel = new Panel(category);
            panel.setY(10);
            panel.setX(frameX);
            panels.add(panel);
            frameX += panel.getWidth() + 10;
        }
    }

    @Override public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        abstractTheme = (( ClickGui ) Main.getMain().getModuleManager().get(ClickGui.class)).theme.getTheme();
        description.reset();
        if (this.mc.player != null) {
            this.doScroll();
        }
        ClickGui globals = ( ClickGui ) Main.getMain().getModuleManager().get(ClickGui.class);
        color = globals.color.getColor();
        panels.forEach(panel -> {
            panel.renderPanel(mouseX, mouseY);
            panel.updatePosition(mouseX, mouseY);
            panel.getComponents().forEach(c -> c.updateComponent(mouseX, mouseY));
        });
        ScaledResolution sr = new ScaledResolution(mc);
        description.draw();
    }

    //Syka blat plutonium hello
    private void doScroll() {
        block3:
        {
            int n;
            block2:
            {
                n = Mouse.getDWheel();
                if (n >= 0) break block2;
                for (Panel panel : panels) {
                    panel.setY(panel.getY() - 8);
                }
                break block3;
            }
            if (n <= 0) break block3;
            for (Panel panel : panels) {
                panel.setY(panel.getY() + 8);
            }
        }
    }

    @Override public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        for (final Panel panel : panels) {
            if (panel.isHover(mouseX, mouseY) && mouseButton == 0) {
                panel.setDrag(true);
                panel.dragX = mouseX - panel.getX();
                panel.dragY = mouseY - panel.getY();
            }
            if (panel.isHover(mouseX, mouseY) && mouseButton == 1) panel.setOpen(!panel.isOpen());

            if (panel.isOpen() && !panel.getComponents().isEmpty()) {
                for (Component component : panel.getComponents()) {
                    component.mouseClicked(mouseX, mouseY, mouseButton);
                }
            }
        }
    }

    @Override public void mouseReleased(int mouseX, int mouseY, int state) {
        for (final Panel panel : panels) {
            panel.setDrag(false);
        }
        for (final Panel panel : panels) {
            if (panel.isOpen() && !panel.getComponents().isEmpty()) {
                for (final Component component : panel.getComponents()) {
                    component.mouseReleased(mouseX, mouseY, state);
                }
            }
        }
    }

    public void drawGradient(int left, int top, int right, int bottom, int startColor, int endColor) {
        drawGradientRect(left, top, right, bottom, startColor, endColor);
    }

    @Override public void keyTyped(char typedChar, int keyCode) {
        for (final Panel panel : panels) {
            if (panel.isOpen() && keyCode != 1 && !panel.getComponents().isEmpty()) {
                for (final Component component : panel.getComponents()) {
                    component.keyTyped(keyCode);
                }
            }
        }

        if (keyCode == Keyboard.KEY_ESCAPE) {
            mc.displayGuiScreen(null);

            if (mc.currentScreen == null) {
                mc.setIngameFocus();
            }
        }
    }


    @Override public void onGuiClosed() {
        if (mc.entityRenderer.isShaderActive()) mc.entityRenderer.stopUseShader();
        if (this.mc.entityRenderer.getShaderGroup() != null) {
            this.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
        }
    }

    @Override public boolean doesGuiPauseGame() {return false;}

    public Description getDescriptionManager() {
        return description;
    }

    public void initGui() {
        if (OpenGlHelper.shadersSupported && this.mc.getRenderViewEntity() instanceof EntityPlayer && (( ClickGui ) Main.getMain().getModuleManager().get(ClickGui.class)).blur) {
            if (this.mc.entityRenderer.getShaderGroup() != null) {
                this.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
            }
            this.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
        }
    }

}
