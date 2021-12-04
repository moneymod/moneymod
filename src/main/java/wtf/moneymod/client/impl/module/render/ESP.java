package wtf.moneymod.client.impl.module.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.math.BlockPos;
import wtf.moneymod.client.api.events.PacketEvent;
import wtf.moneymod.client.api.events.RenderNameTagEvent;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.misc.Timer;
import wtf.moneymod.client.impl.utility.impl.render.JColor;
import wtf.moneymod.client.impl.utility.impl.render.Renderer3D;
import wtf.moneymod.client.impl.utility.impl.shader.FramebufferShader;
import wtf.moneymod.client.impl.utility.impl.shader.impl.OutlineShader;
import wtf.moneymod.client.impl.utility.impl.world.ChatUtil;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

@Module.Register( label = "ESP", cat = Module.Category.RENDER, exception = true )
public class ESP extends Module {

    @Value( value = "Players" ) public boolean players = true;
    @Value( value = "Crystals" ) public boolean crystals = true;

    @Value( value = "Shaders" ) public boolean shaders = true;
    @Value( value = "Shader" ) public Shader shader = Shader.OUTLINE;

    @Value( value = "Color" ) public JColor color = new JColor(0, 255, 0, false);

    @Value( value = "ChorusPredict" ) public boolean chorusPredict = true;
    @Value( value = "Delay (Sec)" ) @Bounds( min = 1, max = 32 ) public int delay = 5;

    @Value( "RainbowSpeed" ) @Bounds( min = 0.0f, max = 1.0f ) public float rainbowspeed = 0.4f;
    @Value( "RainbowStrength" ) @Bounds( min = 0.0f, max = 1.0f ) public float rainbowstrength = 0.3f;
    @Value( "Saturation" ) @Bounds( min = 0.0f, max = 1.0f ) public float saturation = 0.5f;
    @Value( "Radius" ) @Bounds( min = 0.1f, max = 5.0f ) public float radius = 1f;
    @Value( "Quality" ) @Bounds( min = 0.1f, max = 5.0f ) public float quality = 1f;

    ItemRenderer itemRenderer = new ItemRenderer(mc);
    BlockPos predictChorus;
    private final Timer timer = new Timer();
    boolean nameTags;
    public static ESP INSTANCE;
    public FramebufferShader framebuffer = null;

    public ESP() {
        INSTANCE = this;
    }


    @Override
    public void onToggle() {
        predictChorus = null;
        if (shader != Shader.OUTLINE)
            ChatUtil.INSTANCE.sendMessage("Shaders other than outline are not supported right now", true);
    }

    @Handler public Listener<PacketEvent.Receive> packetEventReceive = new Listener<>(PacketEvent.Receive.class, e -> {
        if (e.getPacket() instanceof SPacketSoundEffect && chorusPredict) {
            final SPacketSoundEffect packet = ( SPacketSoundEffect ) e.getPacket();
            if (packet.getSound() == SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT) {
                predictChorus = new BlockPos(packet.getX(), packet.getY(), packet.getZ());
            }
        }
    });

    @Override public void onRender3D(float partialTicks) {
        if (predictChorus != null) {
            if (timer.passed(delay * 1000)) {
                predictChorus = null;
                timer.reset();
                return;
            }
            Renderer3D.drawBoxESP(predictChorus, color.getColor(), 1, true, true, color.getColor().getAlpha(), color.getColor().getAlpha(), 1);
        }
    }

    @Override
    public void onRenderGameOverlay(float partialTicks) {
        if (shaders) {
            if (shader == Shader.OUTLINE) {
                GlStateManager.pushMatrix();

                framebuffer = OutlineShader.INSTANCE;
                OutlineShader.INSTANCE.setCustomValues(rainbowspeed, rainbowstrength, saturation);
                OutlineShader.INSTANCE.startDraw(partialTicks);
                nameTags = true;
                mc.world.loadedEntityList.forEach(e -> {
                    if (e != mc.player && ((e instanceof EntityPlayer && players) || (e instanceof EntityEnderCrystal && crystals))) {
                        mc.getRenderManager().renderEntityStatic(e, partialTicks, true);
                    }
                });
                nameTags = false;
                OutlineShader.INSTANCE.stopDraw(color.getColor(), radius, quality, saturation, 1, 0.5f, 0.5f);

                GlStateManager.popMatrix();
            }
        }
    }

    //    @Override public void onRender3D(float partialTicks) {
    //        if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
    //            if (shaders) {
    //                switch (shader) {
    //                    case OUTLINE:
    //                        framebuffer = OutlineShader.INSTANCE;
    //                        break;
    //                    case GLOW:
    //                        framebuffer = GlowShader.INSTANCE;
    //                        break;
    //                    case SPACE:
    //                        framebuffer = SpaceShader.INSTANCE;
    //                        break;
    //                    case SPACESMOKE:
    //                        framebuffer = SpaceSmokeShader.INSTANCE;
    //                        break;
    //                }
    //                framebuffer.startDraw(event.getPartialTicks());
    //                nameTags = true;
    //                mc.world.loadedEntityList.forEach(e -> {
    //                    if (e != mc.player && ((e instanceof EntityPlayer && players) || (e instanceof EntityEnderCrystal && crystals))) {
    //                        mc.getRenderManager().renderEntityStatic(e, event.getPartialTicks(), true);
    //                    }
    //                });
    //                nameTags = false;
    //                framebuffer.stopDraw(color.getColor(), 1f, 1f, 0.8f, 1, 0.5f, 0.5f);
    //            }
    //        }
    //    }

    @Handler public Listener<RenderNameTagEvent> eventListener = new Listener<>(RenderNameTagEvent.class, e -> {
        if (nameTags) e.cancel();
    });

    public enum Shader {
        OUTLINE,
        GLOW,
        SPACE,
        SPACESMOKE
    }

}
