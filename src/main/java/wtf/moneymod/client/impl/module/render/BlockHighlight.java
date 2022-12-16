package wtf.moneymod.client.impl.module.render;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.render.JColor;
import wtf.moneymod.client.impl.utility.impl.render.Renderer3D;

@Module.Register( label = "BlockHighlight", cat = Module.Category.RENDER )
public class BlockHighlight extends Module {

    @Value("Fade") public boolean fade = false;
    @Value( "Fade Speed" ) @Bounds( min = 0.1f, max = 1f ) public float fadeSpeed = 0.1f;
    @Value(value = "Fill") public boolean fill = false;
    @Value(value = "Outline") public boolean outline = true;
    @Value(value = "Widht") @Bounds(max = 3) public double widht = 0.3d;
    @Value(value = "Color" ) public JColor color = new JColor(255, 0, 0,180, false);
    private BlockPos last = null;
    private int alpha;

    public BlockHighlight() {
        last = null;
    }

    @Override public void onRender3D(float partialTicks) {
        RayTraceResult ray = mc.objectMouseOver;
        if (ray != null && ray.typeOfHit == RayTraceResult.Type.BLOCK) {
            if (last == null || ray.getBlockPos().toLong() != last.toLong()) {
                last = ray.getBlockPos();
                alpha = 0;
            }
            BlockPos positionRender = ray.getBlockPos();
            Renderer3D.drawBoxESP( mc.world.getBlockState( positionRender ).getSelectedBoundingBox( mc.world, positionRender ), color.getColor(), (float) widht, outline, fill, alpha, alpha);
            if (fade) {
                alpha += color.getColor().getAlpha() / fadeSpeed * Main.getMain().getFpsManagement().getFrametime();
                alpha = Math.min(alpha, color.getColor().getAlpha());
            } else {
                alpha = 255;
            }
        } else {
            last = null;
        }
    }
}
