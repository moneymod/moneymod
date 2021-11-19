package wtf.moneymod.client.impl.module.render;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.render.JColor;
import wtf.moneymod.client.impl.utility.impl.render.Renderer3D;

@Module.Register( label = "BlockHighlight", cat = Module.Category.RENDER )
public class BlockHighlight extends Module {

    @Value(value = "Fill") public boolean fill = false;
    @Value(value = "Outline") public boolean outline = true;
    @Value(value = "Widht") @Bounds(max = 3) public double widht = 0.3d;
    @Value(value = "Color" ) public JColor color = new JColor(255, 0, 0,180, false);

    @Override public void onRender3D(float partialTicks) {
        RayTraceResult ray = mc.objectMouseOver;
        if (ray != null && ray.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos positionRender = ray.getBlockPos();
            Renderer3D.drawBoxESP( mc.world.getBlockState( positionRender ).getSelectedBoundingBox( mc.world, positionRender ), color.getColor(), (float) widht, outline, fill, color.getColor().getAlpha(), color.getColor().getAlpha());
        }
    }
}
