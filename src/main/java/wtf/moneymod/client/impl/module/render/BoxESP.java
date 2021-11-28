package wtf.moneymod.client.impl.module.render;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.render.JColor;
import wtf.moneymod.client.impl.utility.impl.render.Renderer3D;

import java.util.List;

@Module.Register( label = "Box", cat = Module.Category.RENDER )
public class BoxESP extends Module {

    @Value( "Players" ) public boolean players = false;
    @Value( "Hostiles" ) public boolean hostiles = false;
    @Value( "Animals" ) public boolean animals = false;
    @Value( "Crystals" ) public boolean crystals = false;
    @Value( "Other" ) public boolean other = true;
    @Value( "Color" ) public JColor color = new JColor(255, 255, 255, 100, false);
    @Value( "Color Line" ) public JColor colorLine = new JColor(255, 255, 255, 100, false);
    @Value( "Box" ) public boolean box = true;
    @Value( "Outline" ) public boolean outline = true;
    @Value( "Line Width" ) @Bounds( min = 0.1f, max = 3f ) public float lineWidth = 1f;

    @Override public void onRender3D(float partialTicks) {
        mc.world.loadedEntityList.stream()
                .filter(e -> (e instanceof EntityAnimal && animals) || (e instanceof EntityCreature && hostiles)
                        || (e instanceof EntityPlayer && players) || (e instanceof EntityEnderCrystal && crystals) ||
                        (e instanceof EntityItem || e instanceof EntityExpBottle || e instanceof EntityEnderPearl && other))
                .forEach(e -> {
                    if (e == mc.player) return;
                    Renderer3D.drawBoxESP(e.getEntityBoundingBox(), color.getColor(), colorLine.getColor(), lineWidth, outline, box, color.getColor().getAlpha(), colorLine.getColor().getAlpha());
                });
    }

}
