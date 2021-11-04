package wtf.moneymod.client.impl.module.render;

import net.minecraft.network.play.server.SPacketTimeUpdate;
import wtf.moneymod.client.api.events.PacketEvent;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

@Module.Register( label = "Changer", cat = Module.Category.MOVEMENT )
public class Changer extends Module {

    @Value( value = "Fps" ) public boolean fps = true;
    @Value( value = "Fps Value" ) @Bounds( min = 5, max = 1000 ) public int fpsValue = 240;
    @Value( value = "Fov" ) public boolean fov = true;
    @Value( value = "Fov Value" ) @Bounds( min = 5, max = 169 ) public int fovValue = 110;
    @Value( value = "Gamma" ) public boolean gamma = true;
    @Value( value = "Gamma Value" ) @Bounds( min = 5, max = 250 ) public int gammaValue = 250;
    @Value( value = "Time" ) public boolean time = true;
    @Value( value = "Time Value" ) @Bounds( min = 5, max = 24 ) public int timeValue = 24;

    @Override public void onTick() {
        if (fps) mc.gameSettings.limitFramerate = fpsValue;
        if (fov) mc.gameSettings.fovSetting = fovValue;
        if (gamma) mc.gameSettings.gammaSetting = gammaValue;
        if (time) mc.world.setWorldTime(timeValue * 1000);
    }

    @Handler
    public Listener<PacketEvent.Receive> packetEventReceive = new Listener<>(PacketEvent.Receive.class, e -> {
        if (nullCheck()) return;
        if (e.getPacket() instanceof SPacketTimeUpdate) {
            if (time) e.setCancelled(true);
        }
    });

}
