package wtf.moneymod.client.impl.module.render;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wtf.moneymod.client.api.events.PacketEvent;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.misc.Timer;
import wtf.moneymod.client.impl.utility.impl.render.JColor;
import wtf.moneymod.client.impl.utility.impl.render.Renderer3D;
import wtf.moneymod.client.impl.utility.impl.shader.FramebufferShader;
import wtf.moneymod.client.impl.utility.impl.shader.impl.GlowShader;
import wtf.moneymod.client.impl.utility.impl.shader.impl.OutlineShader;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

@Module.Register( label = "ESP", cat = Module.Category.RENDER, exception = true )
public class ESP extends Module {

    @Value( value = "Shaders" ) public boolean shaders = true;
    @Value( value = "Shader" ) public Shader shader = Shader.OUTLINE;

    @Value( value = "Color" ) public JColor color = new JColor(0, 255, 0, false);

    @Value( value = "ChorusPredict" ) public boolean chorusPredict = true;
    @Value( value = "Delay (Sec)" ) @Bounds( max = 32 ) public int delay = 5;

    @Value( value = "Speed" ) @Bounds( min = 1, max = 255 ) public float speed = 5;
    @Value( value = "x" ) @Bounds( min = 1, max = 255 ) public float x = 5;
    @Value( value = "y" ) @Bounds( min = 1, max = 255 ) public float y = 5;
    @Value(value = "ChorusPredict") public boolean chorusPredict = true;
    BlockPos predictChorus;
    private final Timer timer = new Timer();

    @Override
    public void onToggle() {
        predictChorus = null;
    }

    @Handler public Listener<PacketEvent.Receive> packetEventReceive = new Listener<>(PacketEvent.Receive.class, e -> {
        if (e.getPacket() instanceof SPacketSoundEffect && chorusPredict) {
            final SPacketSoundEffect packet = ( SPacketSoundEffect ) e.getPacket();
            if (packet.getSound() == SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT) {
                predictChorus = new BlockPos(packet.getX(), packet.getY(), packet.getZ());
            }
        }
    });

    @SubscribeEvent public void onRender(RenderWorldLastEvent event) {
        if (predictChorus != null) {
            if (timer.passed(delay * 1000)) {
                predictChorus = null;
                timer.reset();
                return;
            }
            Renderer3D.drawBoxESP(predictChorus, color.getColor(), 1, true, true, color.getColor().getAlpha(), color.getColor().getAlpha(), 1);
        }
    }

    @SubscribeEvent public void onRender2d(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS) {
            if (shaders) {
                FramebufferShader framebuffer = null;
                switch (shader) {
                    case OUTLINE:
                        framebuffer = OutlineShader.INSTANCE;
                        break;
                    case GLOW:
                        framebuffer = GlowShader.INSTANCE;
                        break;
                }
                framebuffer.startDraw(event.getPartialTicks());
                mc.world.loadedEntityList.forEach(e -> {
                    if (e != mc.player && e instanceof EntityPlayer)
                        mc.getRenderManager().renderEntityStatic(e, event.getPartialTicks(), true);
                });
                framebuffer.stopDraw(color.getColor(), 1f, 1f, 0.8f, speed, x, y);
            }
        }
    }

    public enum Shader {
        OUTLINE, GLOW
    }

}
