package wtf.moneymod.client.impl.module.render;

import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.misc.Timer;
import wtf.moneymod.client.impl.utility.impl.render.JColor;
import wtf.moneymod.client.impl.utility.impl.world.EntityUtil;

import java.util.ArrayList;

@Module.Register( label = "BreadCrumbs", cat = Module.Category.RENDER )
public class BreadCrumbs extends Module {

    ArrayList<BreadCrumb> bcs = new ArrayList<>();

    @Value( value = "Mode" ) public Mode mode = Mode.DEFAULT;
    @Value( value = "Line Widht" ) @Bounds( min = 0.1F, max = 5 ) public float lineWidht = 1;
    @Value( value = "Tick Clear" ) @Bounds( min = 1, max = 45 ) public int tickClear = 25;
    @Value("Fade Speed") @Bounds(min = 0.1f, max = 2f) public float fadeSpeed = 0.4f;
    @Value( "Color" ) public JColor color = new JColor(255, 255, 255, false);

    public enum Mode {
        DEFAULT,
        PARTICLE
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;
        if (mode == Mode.PARTICLE)
            mc.world.spawnParticle(EnumParticleTypes.DRIP_WATER, mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.motionX, mc.player.motionY, mc.player.motionZ, 2);
    }


    public static void putVertex3d(Vec3d vec) { GL11.glVertex3d(vec.x, vec.y, vec.z); }

    public static Vec3d getRenderPos(double x, double y, double z) {
        x = x - mc.getRenderManager().viewerPosX;
        y = y - mc.getRenderManager().viewerPosY;
        z = z - mc.getRenderManager().viewerPosZ;
        return new Vec3d(x, y, z);
    }

    @Override
    public void onToggle() {
        bcs.clear();
    }

    @Override
    public void onRender3D(float partialTicks) {
        if (mode == Mode.DEFAULT && EntityUtil.INSTANCE.isMoving(mc.player))
            bcs.add(new BreadCrumb(new Vec3d(mc.player.posX, mc.player.posY, mc.player.posZ)));
        if (mode == Mode.DEFAULT) {
            int time = tickClear * 50;
            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glBlendFunc(770, 771);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glLineWidth(lineWidht);
            GL11.glBegin(3);
            for (int i = 0; i < bcs.size(); i++) {
                BreadCrumb crumb = bcs.get(i);
                if (crumb.getTimer().passed(time)) {
                    crumb.update(bcs);
                }
                GL11.glColor4f(color.getColor().getRed() / 255.0F, color.getColor().getGreen() / 255.0f, color.getColor().getBlue() / 255.0f, ( float ) (crumb.getAlpha() / 255f));
                putVertex3d(getRenderPos(crumb.getVector().x, crumb.getVector().y + 0.3, crumb.getVector().z));
            }
            GL11.glEnd();
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
        }
    }

    public class BreadCrumb {

        Vec3d vector;
        Timer timer;
        double alpha;

        public BreadCrumb(Vec3d vector) {
            timer = new Timer();
            timer.reset();
            this.vector = vector;
            this.alpha = color.getColor().getAlpha();
        }

        public Timer getTimer() {
            return timer;
        }

        public Vec3d getVector() {
            return vector;
        }

        public double getAlpha() {
            return alpha;
        }

        public void update(ArrayList arrayList) {
            if (alpha <= 0) {
                alpha = 0;
                arrayList.remove(this);
            }
            this.alpha -= color.getColor().getAlpha() / fadeSpeed * Main.getMain().getFpsManagement().getFrametime();
        }

    }

}
