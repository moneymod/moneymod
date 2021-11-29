package wtf.moneymod.client.impl.module.render;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import org.lwjgl.opengl.GL11;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.events.PacketEvent;
import wtf.moneymod.client.api.management.impl.HoleManagement;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.render.JColor;
import wtf.moneymod.client.impl.utility.impl.render.Renderer3D;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

import java.awt.*;

@Module.Register( label = "ChinaHat", cat = Module.Category.RENDER )
public class ChinaHat extends Module {


    @Value( value = "Color" ) public JColor color = new JColor(255, 255, 255, 100, false);
    @Value( value = "Points" ) @Bounds( min = 3, max = 100 ) public float points = 3;
    @Value( value = "Width" ) @Bounds( min = 1, max = 10 ) public float widht = 1;
    @Value( value = "Repeat" ) @Bounds( min = 10, max = 500 ) public float repeat = 200;

    @Override public void onRender3D(float partialTicks) {
        if (mc.gameSettings.thirdPersonView != 0) {
            for (int i = 0; i < this.repeat; ++i) {
                this.drawHat(mc.player, 0.001 + i * 0.004f, partialTicks, (int) this.points, (float) this.widht, (mc.player.isSneaking() ? 2.0f : 2.15f) - i * 0.002f, color.getColor().getRGB());
            }
        }
    }

    public void drawHat(final Entity entity, final double radius, final float partialTicks, final int points, final float width, final float yAdd, final int color) {
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glDisable(2929);
        GL11.glLineWidth(width);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2929);
        GL11.glBegin(3);
        final double x = Renderer3D.interpolate(entity.prevPosX, entity.posX, partialTicks) - mc.getRenderManager().viewerPosX;
        final double y = Renderer3D.interpolate(entity.prevPosY + yAdd, entity.posY + yAdd, partialTicks) - mc.getRenderManager().viewerPosY;
        final double z = Renderer3D.interpolate(entity.prevPosZ, entity.posZ, partialTicks) - mc.getRenderManager().viewerPosZ;
        GL11.glColor4f(new Color(color).getRed() / 255.0f, new Color(color).getGreen() / 255.0f, new Color(color).getBlue() / 255.0f, 0.15f);
        for (int i = 0; i <= points; ++i) {
            GL11.glVertex3d(x + radius * Math.cos(i * 6.283185307179586 / points), y, z + radius * Math.sin(i * 6.283185307179586 / points));
        }
        GL11.glEnd();
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glEnable(2929);
        GL11.glEnable(3553);
        GL11.glPopMatrix();
    }
}
