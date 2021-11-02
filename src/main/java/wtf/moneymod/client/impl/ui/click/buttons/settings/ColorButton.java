package wtf.moneymod.client.impl.ui.click.buttons.settings;

import club.cafedevelopment.reflectionsettings.container.SettingContainer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.impl.module.global.ClickGui;
import wtf.moneymod.client.impl.ui.click.Component;
import wtf.moneymod.client.impl.ui.click.buttons.ModuleButton;
import wtf.moneymod.client.impl.utility.impl.render.JColor;
import wtf.moneymod.client.impl.utility.impl.render.Renderer2D;

import java.awt.*;

public class ColorButton extends Component {

    private final SettingContainer setting;
    private final ModuleButton button;
    private boolean isHovered;
    private int offset, x, y;
    private boolean open, dragging;
    private Color color;

    public ColorButton(SettingContainer setting, final ModuleButton button, final int offset) {
        this.setting = setting;
        this.button = button;
        this.x = button.panel.getX() + button.panel.getWidth();
        this.y = button.panel.getY() + button.offset;
        this.offset = offset;
        this.open = false;
        this.dragging = false;
    }

    @Override public void setOffset(int offset) {
        this.offset = offset;
    }

    @Override public void updateComponent(final double mouseX, final double mouseY) {
        isHovered = isHovered(mouseX, mouseY);
        y = button.panel.getY() + offset;
        x = button.panel.getX();
    }

    @Override
    public void render(int mouseX, int mouseY) {
        Gui.drawRect(button.panel.getX(), button.panel.getY() + offset, button.panel.getX() + button.panel.getWidth(), button.panel.getY() + offset + 12, isHovered ? new Color(0, 0, 0, 160).getRGB() : new Color(0, 0, 0, 140).getRGB());

        Renderer2D.drawVGradientRect(button.panel.getX() + button.panel.getWidth() - 12,
                button.panel.getY() + offset + 2,
                button.panel.getX() + button.panel.getWidth() - 4,
                button.panel.getY() + offset + 10,
                injectAlpha((( JColor ) setting.getValue()).getColor(), 255).getRGB(), (( JColor ) setting.getValue()).getColor().getRGB());

        Renderer2D.drawOutline(button.panel.getX() + button.panel.getWidth() - 12,
                button.panel.getY() + offset + 2,
                button.panel.getX() + button.panel.getWidth() - 4,
                button.panel.getY() + offset + 10,
                1f, new Color(255, 255, 255, 90).getRGB());

        mc.fontRenderer.drawStringWithShadow(setting.getId(), button.panel.getX() + 5, button.panel.getY() + offset + ((( ClickGui ) Main.getMain().getModuleManager().get(ClickGui.class)).bounding ? (isHovered ? 0 : 1) : 1), -1);

        if (open) {
            drawPicker(mouseX, mouseY);
            (( JColor ) setting.getValue()).setColor(color);
        }

    }

    @Override public void mouseClicked(double mouseX, double mouseY, int button) {
        if (isHovered(mouseX, mouseY) && button == 1) {
            open = !open;
            this.button.panel.update();
        }

        if (isHovered(mouseX, mouseY) && button == 0 && open) {
            dragging = true;
            if (mouseX > this.x && mouseX < this.x + 110 && mouseY > this.y + 140 && mouseY < this.y + getHeight())
                (( JColor ) setting.getValue()).setRainbow(!(( JColor ) setting.getValue()).isRainbow());
        }
    }

    @Override public void mouseReleased(double mouseX, double mouseY, int mouseButton) {
        dragging = false;
    }

    @Override public int getHeight() {
        if (open) return 150;
        return 12;
    }

    public boolean isHovered(final double x, final double y) {
        return x > this.x && x < this.x + 136 && y > this.y && y < this.y + getHeight();
    }

    private void drawPicker(int mouseX, int mouseY) {
        float[] hsb = new float[] {
                Color.RGBtoHSB((( JColor ) setting.getValue()).getColor().getRed(), (( JColor ) setting.getValue()).getColor().getGreen(), (( JColor ) setting.getValue()).getColor().getBlue(), null)[ 0 ],
                Color.RGBtoHSB((( JColor ) setting.getValue()).getColor().getRed(), (( JColor ) setting.getValue()).getColor().getGreen(), (( JColor ) setting.getValue()).getColor().getBlue(), null)[ 1 ],
                Color.RGBtoHSB((( JColor ) setting.getValue()).getColor().getRed(), (( JColor ) setting.getValue()).getColor().getGreen(), (( JColor ) setting.getValue()).getColor().getBlue(), null)[ 2 ]
        };

        int alphas = (( JColor ) setting.getValue()).getColor().getAlpha();

        boolean picker = mouseX > button.panel.getX() + 2 && mouseX < button.panel.getX() + 108 && mouseY > button.panel.getY() + offset + 18 && mouseY < button.panel.getY() + offset + 126,
                hue = mouseX > button.panel.getX() + 2 && mouseX < button.panel.getX() + 109 && mouseY > button.panel.getY() + offset + 126 && mouseY < button.panel.getY() + offset + 134,
                alpha = mouseX > button.panel.getX() + 2 && mouseX < button.panel.getX() + 109 && mouseY > button.panel.getY() + offset + 134 && mouseY < button.panel.getY() + offset + 141;

        if (dragging) {
            if (picker) {
                float restrictedX = ( float ) Math.min(Math.max(button.panel.getX() + 2, mouseX), button.panel.getX() + 2 + 106);
                float restrictedY = ( float ) Math.min(Math.max(button.panel.getY() + offset + 18, mouseY), button.panel.getY() + offset + 18 + 106);

                hsb[ 1 ] = Math.max(Math.min((restrictedX - ( float ) button.panel.getX() + 2) / 106, 1), 0);
                hsb[ 2 ] = Math.max(Math.min(1f - (restrictedY - ( float ) (this.y + 18)) / 108, 1), 0);
            } else if (hue && !(( JColor ) setting.getValue()).isRainbow()) {
                float restrictedX = ( float ) Math.min(Math.max(button.panel.getX() - 2, mouseX), button.panel.getX() + 104);

                hsb[ 0 ] = Math.min((restrictedX - ( float ) button.panel.getX() - 2) / 104, 1);
            } else if (alpha) {
                float restrictedX = ( float ) Math.min(Math.max(button.panel.getX() - 2, mouseX), button.panel.getX() + 104);
                alphas = ( int ) (Math.min(1 - (restrictedX - ( float ) button.panel.getX() - 2) / 104, 1) * 255);
            }

        }

        int selectedColor = Color.HSBtoRGB(hsb[ 0 ], 1.0f, 1.0f);

        float selectedRed = (selectedColor >> 16 & 0xFF) / 255.0f;
        float selectedGreen = (selectedColor >> 8 & 0xFF) / 255.0f;
        float selectedBlue = (selectedColor & 0xFF) / 255.0f;
        Gui.drawRect(button.panel.getX(), button.panel.getY() + offset + 12, button.panel.getX() + 110, button.panel.getY() + offset + 150, new Color(0, 0, 0, 140).getRGB());
        drawPickerBase(button.panel.getX() + 2,
                button.panel.getY() + offset + 18,
                106,
                108,
                selectedRed,
                selectedGreen,
                selectedBlue,
                1f);

        drawHueSlider(button.panel.getX() + 2,
                button.panel.getY() + offset + 127,
                109,
                6,
                hsb[ 0 ]);

        drawAlphaSlider(button.panel.getX() + 2,
                button.panel.getY() + offset + 134,
                105,
                6,
                selectedRed,
                selectedGreen,
                selectedBlue,
                alphas / 255f);

        float xPos = button.panel.getX() + 2 + hsb[ 1 ] * 106,
                yPos = (button.panel.getY() + offset + 18 + 108) - hsb[ 2 ] * 108;


        GlStateManager.pushMatrix();
        //        Gui.drawRect( ( int ) xPos - 3, ( int ) yPos + 3, ( int ) xPos + 1, ( int ) yPos - 1, Color.BLACK.getRGB( ) );
        //        Gui.drawRect( ( int ) xPos - 2, ( int ) yPos + 2, ( int ) xPos, ( int ) yPos, -1 );
        Renderer2D.drawPolygonOutline(xPos - 2, yPos + 2, 2f, 3, 360, Color.black.getRGB());
        //        Renderer2D.drawPolygon( xPos - 2, yPos + 2, 1, 360, -1 );
        GlStateManager.popMatrix();

        mc.fontRenderer.drawStringWithShadow("Rainbow", ( int ) (button.panel.getX() + button.panel.getWidth() / 2f - mc.fontRenderer.getStringWidth("Rainbow") / 2f), button.panel.getY() + offset + 142, (( JColor ) setting.getValue()).isRainbow() ? (( JColor ) setting.getValue()).getColor().getRGB() : -1);

        color = injectAlpha(new Color(Color.HSBtoRGB(hsb[ 0 ], hsb[ 1 ], hsb[ 2 ])), alphas);

    }

    private Color injectAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    private void drawPickerBase(int pickerX, int pickerY, int pickerWidth, int pickerHeight, float red, float green, float blue, float alpha) {
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

    private void drawHueSlider(int x, int y, int width, int height, float hue) {
        int step = 0;

        for (int colorIndex = 0; colorIndex < 5; colorIndex++) {
            int previousStep = Color.HSBtoRGB(( float ) step / 5, 1.0f, 1.0f);
            int nextStep = Color.HSBtoRGB(( float ) (step + 1) / 5, 1.0f, 1.0f);

            Renderer2D.drawHGradientRect(x + step * (width / 5), y, x + (step + 1) * (width / 5), y + height, previousStep, nextStep);

            step++;
        }

        int sliderMinX = ( int ) (x + (width * hue));

        Renderer2D.drawRect(sliderMinX - 1, y, sliderMinX + 1, y + height, -1);
        Renderer2D.drawOutline(sliderMinX - 1, y, sliderMinX + 1, y + height, 1f, Color.BLACK.getRGB());
    }

    private void drawAlphaSlider(int x, int y, int width, int height, float red, float green, float blue, float alpha) {
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
        Renderer2D.drawOutline(sliderMinX - 1, y, sliderMinX + 1, y + height, 1f, Color.BLACK.getRGB());
    }

    private void drawLeftGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
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
