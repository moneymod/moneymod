package wtf.moneymod.client.impl.module.render;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.management.impl.WaypointManager;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.math.MathUtil;
import wtf.moneymod.client.impl.utility.impl.render.Renderer2D;
import wtf.moneymod.client.impl.utility.impl.render.fonts.CFontRenderer;
import wtf.moneymod.client.impl.utility.impl.render.fonts.FontRender;
import wtf.moneymod.client.impl.waypoint.Waypoint;
import wtf.moneymod.client.mixin.mixins.ducks.AccessorRenderManager;

@Module.Register( label = "Waypoints", cat = Module.Category.RENDER )
public class Waypoints extends Module {

    @Value("Text") public boolean text = true;
    @Value("Distance") public boolean distance = true;

    AccessorRenderManager renderManager = ( AccessorRenderManager ) mc.getRenderManager();

    @SubscribeEvent public void onRenderer3D(RenderWorldLastEvent e) {
        if (WaypointManager.getInstance().isEmpty()) return;
        for (Waypoint waypoint : WaypointManager.getInstance()) {
            String sd = mc.getCurrentServerData() == null ? "singleplayer" : mc.getCurrentServerData().serverIP;
            if (waypoint.getDimension() != mc.world.provider.getDimension() || !waypoint.getServer().equalsIgnoreCase(sd))
                continue;
            //String.format("%s\n%s, %s, %s", waypoint.getName(), waypoint.getPos().getX(), waypoint.getPos().getY(), waypoint.getPos().getZ())
            if(text) renderWaypointText(waypoint.getName(), waypoint.getPos(), e.getPartialTicks());
        }
    }

    public void renderWaypointText(String str, BlockPos loc, float partialTicks) {
        GlStateManager.alphaFunc(516, 0.1F);

        GlStateManager.pushMatrix();

        Entity viewer = Minecraft.getMinecraft().getRenderViewEntity();
        double viewerX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * partialTicks;
        double viewerY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * partialTicks;
        double viewerZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * partialTicks;

        double x = loc.getX() + 0.5 - viewerX;
        double y = loc.getY() - viewerY - viewer.getEyeHeight();
        double z = loc.getZ() + 0.5 - viewerZ;

        double distSq = x * x + y * y + z * z;
        double dist = Math.sqrt(distSq);
        if (distSq > 144) {
            x *= 12 / dist;
            y *= 12 / dist;
            z *= 12 / dist;
        }
        GlStateManager.translate(x, y, z);
        GlStateManager.translate(0, viewer.getEyeHeight(), 0);

        drawNametag(str);

        GlStateManager.rotate(-Minecraft.getMinecraft().getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(Minecraft.getMinecraft().getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.translate(0, -0.25f, 0);
        GlStateManager.rotate(-Minecraft.getMinecraft().getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(Minecraft.getMinecraft().getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);

        if(distance) drawNametag(ChatFormatting.GRAY + "" + Math.round(dist) + "m");

        GlStateManager.popMatrix();

        GlStateManager.disableLighting();
    }

    public void drawNametag(String str) {
        CFontRenderer fontrenderer = Main.getMain().getFontRenderer();
        float f = 1.6F;
        float f1 = 0.016666668F * f;
        GlStateManager.pushMatrix();
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(mc.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(-f1, -f1, f1);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldrenderer = tessellator.getBuffer();
        int i = 0;

        int j = fontrenderer.getStringWidth(str) / 2;
        GlStateManager.disableTexture2D();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(-j - 1, -1 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos(-j - 1, 8 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos(j + 1, 8 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos(j + 1, -1 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2f, i, 553648127);
        GlStateManager.depthMask(true);

        fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2f, i, -1);

        GlStateManager.enableDepth();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }

}
