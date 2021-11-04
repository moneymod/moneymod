package wtf.moneymod.client.impl.ui.click.theme;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.setting.Option;
import wtf.moneymod.client.impl.module.global.ClickGui;
import wtf.moneymod.client.impl.ui.click.Panel;
import wtf.moneymod.client.impl.ui.click.buttons.ModuleButton;
import wtf.moneymod.client.impl.ui.click.buttons.settings.KeyButton;
import wtf.moneymod.client.impl.ui.click.buttons.settings.sub.SubMode;
import wtf.moneymod.client.impl.utility.Globals;
import wtf.moneymod.client.impl.utility.impl.render.JColor;
import wtf.moneymod.client.impl.utility.impl.render.Renderer2D;

import java.awt.*;

public abstract class AbstractTheme extends Renderer2D implements Globals {

    public abstract void drawHeader(Panel panel, int x, int y, int w, int h, boolean hovered);

    public abstract void drawPanelOutline(Panel panel, int x, int y, int w, int h, boolean hovered);

    public abstract void drawModuleButton(ModuleButton module, int x, int y, int w, int h, boolean hovered);

    public abstract void drawBooleanButton(Option<Boolean> container, int x, int y, int w, int h, boolean hovered);

    public abstract void drawSliderButton(Option<Number> container, int x, int y, int w, int h, double sliderWidth, boolean hovered);

    public abstract void drawKeyButton(KeyButton button, int x, int y, int w, int h, boolean hovered);

    public abstract void drawModeButton(Option<Enum> container, int x, int y, int w, int h, boolean hovered);

    public abstract void drawSubModeButton(SubMode container, String current, int x, int y, int w, int h, boolean hovered);

    public abstract void drawColorButton(Option<JColor> container, int x, int y, int w, int h, boolean hovered);

    public abstract void drawPickerButton(Option<JColor> container, int x, int y, int w, int h, boolean hovered);

    protected ScaledResolution getResolution() {
        return new ScaledResolution(mc);
    }

    protected Color getAbsoluteColor() {
        return (( ClickGui ) Main.getMain().getModuleManager().get(ClickGui.class)).color.getColor();
    }

    protected void drawPickerBase(int pickerX, int pickerY, int pickerWidth, int pickerHeight, float red, float green, float blue, float alpha) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glBegin(GL11.GL_POLYGON);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glVertex2f(pickerX, pickerY);
        GL11.glVertex2f(pickerX, pickerY + pickerHeight);
        GL11.glColor4f(red, green, blue, alpha);
        GL11.glVertex2f(pickerX + pickerWidth, pickerY + pickerHeight);
        GL11.glVertex2f(pickerX + pickerWidth, pickerY);
        GL11.glEnd();
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glBegin(GL11.GL_POLYGON);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glVertex2f(pickerX, pickerY);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
        GL11.glVertex2f(pickerX, pickerY + pickerHeight);
        GL11.glVertex2f(pickerX + pickerWidth, pickerY + pickerHeight);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glVertex2f(pickerX + pickerWidth, pickerY);
        GL11.glEnd();
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    protected void drawHueSlider(int x, int y, int width, int height, float hue) {
        int step = 0;

        for (int colorIndex = 0; colorIndex < 5; colorIndex++) {
            int previousStep = Color.HSBtoRGB(( float ) step / 5, 1.0f, 1.0f);
            int nextStep = Color.HSBtoRGB(( float ) (step + 1) / 5, 1.0f, 1.0f);

            drawHGradientRect(x + step * (width / 5f), y, x + (step + 1) * (width / 5f), y + height, previousStep, nextStep);

            step++;
        }

        int sliderMinX = ( int ) (x + (width * hue));

        drawRect(sliderMinX - 1, y, sliderMinX + 1, y + height, -1);
        drawOutline(sliderMinX - 1, y, sliderMinX + 1, y + height, 1f, Color.BLACK.getRGB());
    }

    protected void drawAlphaSlider(int x, int y, int width, int height, float red, float green, float blue, float alpha) {
        boolean left = true;

        int checkerBoardSquareSize = height / 2;

        for (int squareIndex = -checkerBoardSquareSize; squareIndex < width; squareIndex += checkerBoardSquareSize) {
            if (!left) {
                Gui.drawRect(x + squareIndex, y, x + squareIndex + checkerBoardSquareSize, y + height, 0xFFFFFFFF);
                Gui.drawRect(x + squareIndex, y + checkerBoardSquareSize, x + squareIndex + checkerBoardSquareSize, y + height, 0xFF909090);

                if (squareIndex < width - checkerBoardSquareSize) {
                    int minX = x + squareIndex + checkerBoardSquareSize;
                    int maxX = Math.min(x + width, x + squareIndex + checkerBoardSquareSize * 2);

                    Gui.drawRect(minX, y, maxX, y + height, 0xFF909090);
                    Gui.drawRect(minX, y + checkerBoardSquareSize, maxX, y + height, 0xFFFFFFFF);
                }
            }

            left = !left;
        }

        drawLeftGradientRect(x, y, x + width, y + height, new Color(red, green, blue, 1).getRGB(), 0);

        int sliderMinX = ( int ) (x + width - (width * alpha));

        Gui.drawRect(sliderMinX - 1, y, sliderMinX + 1, y + height, -1);
        drawOutline(sliderMinX - 1, y, sliderMinX + 1, y + height, 1f, Color.BLACK.getRGB());
    }

    protected void drawLeftGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
        float f = ( float ) (startColor >> 24 & 255) / 255.0f;
        float f1 = ( float ) (startColor >> 16 & 255) / 255.0f;
        float f2 = ( float ) (startColor >> 8 & 255) / 255.0f;
        float f3 = ( float ) (startColor & 255) / 255.0f;
        float f4 = ( float ) (endColor >> 24 & 255) / 255.0f;
        float f5 = ( float ) (endColor >> 16 & 255) / 255.0f;
        float f6 = ( float ) (endColor >> 8 & 255) / 255.0f;

        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(right, top, 0).color(f4, f5, f6, f4).endVertex();
        bufferbuilder.pos(left, top, 0).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(left, bottom, 0).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(right, bottom, 0).color(f4, f5, f6, f4).endVertex();

        tessellator.draw();

        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

}
