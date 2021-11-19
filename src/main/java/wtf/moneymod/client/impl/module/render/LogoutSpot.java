package wtf.moneymod.client.impl.module.render;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import wtf.moneymod.client.api.events.ConnectionEvent;
import wtf.moneymod.client.api.events.DisconnectEvent;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.math.MathUtil;
import wtf.moneymod.client.impl.utility.impl.render.ColorUtil;
import wtf.moneymod.client.impl.utility.impl.render.JColor;
import wtf.moneymod.client.impl.utility.impl.render.Renderer2D;
import wtf.moneymod.client.impl.utility.impl.render.Renderer3D;
import wtf.moneymod.client.impl.utility.impl.render.fonts.FontRender;
import wtf.moneymod.client.impl.utility.impl.world.ChatUtil;
import wtf.moneymod.client.mixin.mixins.ducks.AccessorRenderManager;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Module.Register( label = "LogoutSpot", cat = Module.Category.RENDER )
public class LogoutSpot extends Module {

    @Value( value = "Distance" ) @Bounds( min = 25, max = 400 ) public float distance = 200;
    @Value( value = "Chat" ) public boolean chat = true;
    @Value( value = "Chams" ) public boolean chams = true;
    @Value( value = "Pulse" ) public boolean pulse = false;
    @Value( value = "Text" ) public boolean text = true;
    @Value( value = "Fill" ) public boolean fill = true;
    @Value( value = "Coords" ) public boolean coords = true;
    @Value( value = "Box" ) public boolean box = true;
    @Value( value = "Color" ) public JColor color = new JColor(255, 0, 0, 140, false);
    CopyOnWriteArrayList<Person> spots = new CopyOnWriteArrayList<>();
    AccessorRenderManager renderManager = ( AccessorRenderManager ) mc.getRenderManager();

    @Override protected void onToggle() {
        spots.clear();
    }

    @Handler
    public Listener<DisconnectEvent> disconnectEventListener = new Listener<>(DisconnectEvent.class, e -> {
        spots.clear();
    });

    @Override public void onRender3D(float partialTicks) {
        if (!spots.isEmpty()) {
            spots.forEach(person -> {

                if (chams) {

                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                    GlStateManager.glLineWidth(1.5F);
                    GlStateManager.disableTexture2D();
                    GlStateManager.depthMask(false);
                    GlStateManager.enableBlend();
                    GlStateManager.disableDepth();
                    GlStateManager.disableLighting();
                    GlStateManager.disableCull();
                    GlStateManager.enableAlpha();

                    ModelPlayer modelPlayer = new ModelPlayer(0, false);

                    modelPlayer.bipedLeftLegwear.showModel = false;
                    modelPlayer.bipedRightLegwear.showModel = false;
                    modelPlayer.bipedLeftArmwear.showModel = false;
                    modelPlayer.bipedRightArmwear.showModel = false;
                    modelPlayer.bipedBodyWear.showModel = false;
                    modelPlayer.bipedHead.showModel = false;
                    modelPlayer.bipedHeadwear.showModel = true;
                    GlStateManager.color(color.getColor().getRed() / 255f, color.getColor().getGreen() / 255f, color.getColor().getBlue() / 255f, pulse ? ColorUtil.sinFunction(0, color.getColor().getAlpha(), 1) : ( float ) color.getColor().getAlpha() / 255f);
                    GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
                    PopChams.renderEntity(person.entity, modelPlayer, person.entity.limbSwing,
                            person.entity.limbSwingAmount, person.entity.ticksExisted, person.entity.rotationYawHead, person.entity.rotationPitch, 1);

                    GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
                    PopChams.renderEntity(person.entity, modelPlayer, person.entity.limbSwing,
                            person.entity.limbSwingAmount, person.entity.ticksExisted, person.entity.rotationYawHead, person.entity.rotationPitch, 1);
                    GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);

                    GlStateManager.enableCull();
                    GlStateManager.depthMask(true);
                    GlStateManager.enableTexture2D();
                    GlStateManager.enableBlend();
                    GlStateManager.enableDepth();

                }

                if (box) {
                    Renderer3D.drawBoxESP(new AxisAlignedBB(person.vec3d.x - 0.3f, person.vec3d.y, person.vec3d.z - 0.3f, person.vec3d.x + 0.3f, person.vec3d.y + 1.9f, person.vec3d.z + 0.3f), color.getColor(), 1f, true, false, color.getColor().getAlpha(), 255);
                }

                if (text) {
                    double x = MathUtil.INSTANCE.interpolate(person.entity.lastTickPosX, person.entity.posX, partialTicks) - renderManager.getRenderPosX();
                    double y = MathUtil.INSTANCE.interpolate(person.entity.lastTickPosY, person.entity.posY, partialTicks) - renderManager.getRenderPosY();
                    double z = MathUtil.INSTANCE.interpolate(person.entity.lastTickPosZ, person.entity.posZ, partialTicks) - renderManager.getRenderPosZ();
                    this.renderNameTag(person.name, x, y, z, partialTicks, person.vec3d.x, person.vec3d.y, person.vec3d.z);
                }
            });
        }
    }

    @Handler public Listener<ConnectionEvent> eventListener = new Listener<>(ConnectionEvent.class, e -> {

        if (e.getAction() == SPacketPlayerListItem.Action.ADD_PLAYER) {
            spots.removeIf(p -> p.uuid == e.getUuid() || p.name.equalsIgnoreCase(e.getName()));
        } else {
            if (chat)
                ChatUtil.INSTANCE.sendMessage(String.format("%s logged out at X:%.1f, Y:%.1f, Z:%.1f", e.getName(), e.getEntity().posX, e.getEntity().posY, e.getEntity().posZ));
            if (e.getName() == null || e.getEntity() == null || e.getUuid() == null) return;
            EntityPlayer entity = new EntityPlayer(mc.world, new GameProfile(e.getUuid(), e.getName())) {
                @Override public boolean isSpectator() {return false;}

                @Override public boolean isCreative() {return false;}
            };
            entity.copyLocationAndAnglesFrom(e.getEntity());

            spots.add(new Person(e.getName(), e.getUuid(), entity));
        }

    });

    private void renderNameTag(String name, double x, double yi, double z, float delta, double xPos, double yPos, double zPos) {
        double y = yi + 0.7;
        Entity camera = mc.getRenderViewEntity();
        assert (camera != null);
        double originalPositionX = camera.posX;
        double originalPositionY = camera.posY;
        double originalPositionZ = camera.posZ;
        camera.posX = MathUtil.INSTANCE.interpolate(camera.prevPosX, camera.posX, delta);
        camera.posY = MathUtil.INSTANCE.interpolate(camera.prevPosY, camera.posY, delta);
        camera.posZ = MathUtil.INSTANCE.interpolate(camera.prevPosZ, camera.posZ, delta);
        String displayTag = name + (coords ? " XYZ: " + ( int ) xPos + ", " + ( int ) yPos + ", " + ( int ) zPos : "");
        double distance = camera.getDistance(x + mc.getRenderManager().viewerPosX, y + mc.getRenderManager().viewerPosY, z + mc.getRenderManager().viewerPosZ);
        int width = mc.fontRenderer.getStringWidth(displayTag) / 2;
        double scale = (0.0018 + 0.4 * (distance * 0.3)) / 1000.0;
        if (distance <= 8.0) {
            scale = 0.0245;
        }
        GlStateManager.pushMatrix();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0f, -1500000.0f);
        GlStateManager.disableLighting();
        GlStateManager.translate(( float ) x, ( float ) y + 1.4f, ( float ) z);
        GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(mc.getRenderManager().playerViewX, mc.gameSettings.thirdPersonView == 2 ? -1.0f : 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(-scale, -scale, scale);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.enableBlend();
        if (fill) {
            Renderer2D.drawRect(-width - 2, -(mc.fontRenderer.FONT_HEIGHT + 1), ( float ) width + 2.0f, 1.5f, 0x55000000);
        }
        GlStateManager.disableBlend();
        FontRender.drawStringWithShadow(displayTag, -width, -(mc.fontRenderer.FONT_HEIGHT - 1), color.getColor().getRGB());
        camera.posX = originalPositionX;
        camera.posY = originalPositionY;
        camera.posZ = originalPositionZ;
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.disablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0f, 1500000.0f);
        GlStateManager.popMatrix();
    }

    private class Person {

        public final String name;
        public final UUID uuid;
        public final EntityPlayer entity;
        public final Vec3d vec3d;

        public Person(String name, UUID uuid, EntityPlayer player) {
            this.name = name;
            this.uuid = uuid;
            this.entity = player;
            this.vec3d = player.getPositionVector();
        }

    }

}
