package wtf.moneymod.client.impl.module.render;

import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.module.misc.ServerFlag;
import wtf.moneymod.client.impl.utility.impl.render.CrumbsUtil;
import wtf.moneymod.client.impl.utility.impl.render.JColor;

import java.util.ArrayList;

@Module.Register( label = "BreadCrumbs", cat = Module.Category.RENDER)
public class BreadCrumbs extends Module {

    ArrayList<CrumbsUtil> bcs = new ArrayList<CrumbsUtil>();

    @Value(value = "Mode") public Mode mode = Mode.DEFAULT;
    @Value(value = "Line Widht") @Bounds(min = 0.1F, max = 5) public float lineWidht = 1;
    @Value(value = "Tick Clear") @Bounds(min = 1, max = 45) public int tickClear = 25;
    @Value( "Color" ) public JColor color = new JColor(255, 255, 255, false);

    public enum Mode{
        DEFAULT, PARTICLE
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;
        bcs.add(new CrumbsUtil(mc.player.getPositionVector()));
        if (mode == Mode.PARTICLE) mc.world.spawnParticle(EnumParticleTypes.DRIP_WATER, mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.motionX, mc.player.motionY, mc.player.motionZ, 2);
    }


    public static void putVertex3d(Vec3d vec) { GL11.glVertex3d(vec.x, vec.y, vec.z); }

    public static Vec3d getRenderPos(double x, double y, double z) {
        x = x - mc.getRenderManager().viewerPosX;
        y = y - mc.getRenderManager().viewerPosY;
        z = z - mc.getRenderManager().viewerPosZ;
        return new Vec3d(x, y, z);
    }

    @Override
    public void onToggle(){
        bcs.clear();
    }

    @Override
    public void onRender3D(float partialTicks) {
        if (mode == Mode.DEFAULT) {
            int time = tickClear * 500;
            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glBlendFunc(770, 771);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glLineWidth(lineWidht);
            GL11.glColor4f(color.getColor().getRed() / 255.0F, color.getColor().getGreen() / 255.0f, color.getColor().getBlue() / 255.0f, color.getColor().getAlpha() / 255.0f);
            GL11.glBegin(3);
            for (int i = 0; i < bcs.size(); i++) {
                CrumbsUtil bc = bcs.get(i);
                if (bc.getTimer().passed(time)) {
                    bcs.remove(bc);
                }
                putVertex3d(getRenderPos(bc.getVector().x, bc.getVector().y + 0.3, bc.getVector().z));
            }
            GL11.glEnd();
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
        }
    }
}
