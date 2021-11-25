package wtf.moneymod.client.impl.module.global;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.math.MathUtil;
import wtf.moneymod.client.impl.utility.impl.render.JColor;
import wtf.moneymod.client.impl.utility.impl.render.Renderer2D;
import wtf.moneymod.client.impl.utility.impl.render.fonts.FontRender;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Module.Register( label = "Keybinds", cat = Module.Category.GLOBAL )
public class KeybindList extends Module {

    @Value( "Color" ) public JColor color = new JColor(255, 255, 255, false);
    @Value( "BackGround" ) public JColor background = new JColor(0, 0, 0, 15, false);
    @Value( "Animation" ) public boolean anim = true;
    @Value( "Offset" ) @Bounds( max = 6 ) public int offset = 2;
    @Value( "Blur" ) public boolean blur = true;
    @Value( "X" ) @Bounds( max = 1080 ) int x = 2;
    @Value( "Y" ) @Bounds( max = 720 ) int y = 3;

    List<Bind> binds;

    @Override protected void onEnable() {
        binds = Main.getMain().getModuleManager().stream().map(Bind::new).collect(Collectors.toList());
    }

    @Override public void onRender2D() {
        if (binds.isEmpty()) return;
        List<Bind> local = binds.stream().filter(b -> b.module.drawn && b.module.getKey() != 0 && (b.percent != 0 || b.module.isToggled())).collect(Collectors.toList());
        GlStateManager.pushMatrix();
        Renderer2D.drawRect(x, y, x + 100, y + 2 + FontRender.getFontHeight() + (local.size() * (FontRender.getFontHeight() + offset) * 0.75f), background.getColor().getRGB());
        Renderer2D.drawRect(x, y, x + 100, y + 1.5f, color.getColor().getRGB());

        GlStateManager.pushMatrix();
        GlStateManager.disableBlend();
        GL11.glDepthRange(0, 0.1);
        drawScaled("keybinds", ( int ) ((x + 50 - (FontRender.getStringWidth("keybinds") * 0.75) / 2)), ( int ) ((y + 3)), 0.75f, -1);
        float y = this.y + 2 + FontRender.getFontHeight();
        for (Bind bind : local) {
            bind.update();
            float t = anim ? bind.percent : 1f;
            float nigger;
            if(bind.module.isToggled()) {
                nigger =  ( float ) MathUtil.INSTANCE.clamp( 1.0f - ( t -= 1.0f ) * t * t * t, 0.0f, 1.0f );
            } else {
                nigger = ( float ) MathUtil.INSTANCE.clamp( 1.0 - ( t -= 1.0f ) * t * t * t, 0.0f, 1.0f );
            }
            drawScaled(bind.module.getLabel(), x - (5 - 5 * nigger) + 1, ( int ) (y), 0.75f, -1);
            String key = "[" + Keyboard.getKeyName(bind.module.getKey()) + "]";

            drawScaled(key, ( int ) ((x + 100 - (FontRender.getStringWidth(key) * 0.75)) - (5 - 5 * nigger)), ( int ) (y), 0.75f, -1);
            y += FontRender.getFontHeight() * 0.75 + offset;
        }
        GL11.glDepthRange(0, 1);

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

    private class Bind {

        Module module;
        float percent;

        public Bind(Module module) {
            this.module = module;
            this.percent = 0;
        }

        void update() {
            if (module.isToggled()) {
                percent += 0.02f;
            } else {
                percent -= 0.02f;
            }
            percent = ( float ) MathUtil.INSTANCE.clamp(percent, 0, 1);
        }

    }

}
