package wtf.moneymod.client.impl.module.render;

import net.minecraft.network.play.server.SPacketTimeUpdate;
import wtf.moneymod.client.api.events.PacketEvent;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

@Module.Register( label = "Changer", cat = Module.Category.RENDER )
public class Changer extends Module {

    @Value( value = "Fps" ) public boolean fps = true;
    @Value( value = "Fps Value" ) @Bounds( min = 5, max = 1000 ) public int fpsValue = 240;
    @Value( value = "Fov" ) public boolean fov = true;
    @Value( value = "Fov Value" ) @Bounds( min = 5, max = 169 ) public int fovValue = 110;
    @Value( value = "Gamma" ) public boolean gamma = true;
    int cycle = 0;
    @Override public void onTick() {
        if (fps) mc.gameSettings.limitFramerate = fpsValue;
        if (fov) mc.gameSettings.fovSetting = fovValue;
        if (gamma) mc.gameSettings.gammaSetting = 400;

    }
}
