package wtf.moneymod.client.impl.utility.impl.render.fonts;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.impl.module.global.ClickGui;
import wtf.moneymod.client.impl.utility.Globals;;

public class FontRender implements Globals {
    public static boolean isCustomFontEnabled() {
        try {
            ClickGui clickgui = (ClickGui) Main.getMain().getModuleManager().get(ClickGui.class);
            return clickgui.font;
        } catch (Exception e) {

        }

        return false;
    }

    // floats
    public static float drawStringWithShadow(String text, float x, float y, int color) {
        return drawStringWithShadow(text, (int) x, (int) y, color);
    }

    public static void prepare() {
        GlStateManager.pushMatrix();
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.depthMask((boolean) false);
        GlStateManager.disableAlpha();
        GlStateManager.disableCull();
        GlStateManager.enableBlend();
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glBlendFunc(770, 771);
    }

    public static float drawString(String text, float x, float y, int color) {
        return drawString(text, (int) x, (int) y, color);
    }

    // ints
    public static float drawStringWithShadow(String text, int x, int y, int color) {
        if (isCustomFontEnabled())
            return Main.getMain().getFontRenderer().drawStringWithShadow(text, x, y, color);

        return mc.fontRenderer.drawStringWithShadow(text, x, y, color);
    }

    public static float drawString(String text, int x, int y, int color) {
        if (isCustomFontEnabled())
            return Main.getMain().getFontRenderer().drawString(text, x, y, color);

        return mc.fontRenderer.drawString(text, x, y, color);
    }

    public static int getStringWidth(String str) {
        if (isCustomFontEnabled())
            return Main.getMain().getFontRenderer().getStringWidth(str);

        return mc.fontRenderer.getStringWidth(str);
    }

    public static int getFontHeight() {
        if (isCustomFontEnabled())
            return Main.getMain().getFontRenderer().getHeight() + 2;

        return mc.fontRenderer.FONT_HEIGHT;
    }

}