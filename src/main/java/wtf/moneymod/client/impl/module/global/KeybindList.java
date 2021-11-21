package wtf.moneymod.client.impl.module.global;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.render.JColor;
import wtf.moneymod.client.impl.utility.impl.render.Renderer2D;
import wtf.moneymod.client.impl.utility.impl.render.fonts.FontRender;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Module.Register( label = "Keybinds", cat = Module.Category.GLOBAL )
public class KeybindList extends Module {

    @Value( "Color" ) public JColor color = new JColor(255, 255, 255, false);
    @Value( "X" ) @Bounds( max = 1080 ) int x = 2;
    @Value( "Y" ) @Bounds( max = 720 ) int y = 3;

    @Override public void onRender2D() {
        List<Module> binds = Main.getMain().getModuleManager().get(m -> m.isToggled() && m.getKey() != 0 && m.drawn);
        if (binds.isEmpty()) return;
        GlStateManager.pushMatrix();
        GlStateManager.disableDepth();
        Renderer2D.drawRect(x, y, x + 100, y + 2 + FontRender.getFontHeight() + (binds.size() * FontRender.getFontHeight() * 0.75f), new Color(0, 0, 0, 15).getRGB());
        Renderer2D.drawRect(x, y, x + 100, y + 1.5f, color.getColor().getRGB());

        GlStateManager.pushMatrix();
        GlStateManager.disableBlend();
        GL11.glDepthRange(0, 0.1);
        drawScaled("keybinds", ( int ) ((x + 50 - (FontRender.getStringWidth("keybinds") * 0.75) / 2)), ( int ) ((y + 3)), 0.75f, -1);
        float y = this.y + 2 + FontRender.getFontHeight();
        for (Module bind : binds) {
            drawScaled(bind.getLabel(), x, ( int ) (y), 0.75f, -1);
            String key = "[" + Keyboard.getKeyName(bind.getKey()) + "]";
            drawScaled(key, ( int ) ((x + 100 - (FontRender.getStringWidth(key) * 0.75))), ( int ) (y), 0.75f, -1);
            y += FontRender.getFontHeight() * 0.75;
        }

        GlStateManager.enableBlend();
        GlStateManager.popMatrix();

        GlStateManager.popMatrix();
    }


    void drawScaled(String string, float x, float y, float factor, int color) {
        GL11.glPushMatrix();
        GL11.glScaled(factor, factor, factor);
        FontRender.drawStringWithShadow(string, x / factor, y / factor, color);
        GL11.glPopMatrix();
    }

}
