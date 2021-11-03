package wtf.moneymod.client.impl.module.render;

import club.cafedevelopment.reflectionsettings.annotation.Clamp;
import club.cafedevelopment.reflectionsettings.annotation.Setting;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import wtf.moneymod.client.api.events.PacketEvent;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

@Module.Register( label = "Changer", cat = Module.Category.MOVEMENT)
public class Changer extends Module {

    @Setting(id = "Fps") public boolean fps = true;
    @Setting(id = "Fps Value", clamp = @Clamp(min = 5,max = 1000)) public int fpsValue = 240;
    @Setting(id = "Fov") public boolean fov = true;
    @Setting(id = "Fov Value", clamp = @Clamp(min = 5,max = 169)) public int fovValue = 110;
    @Setting(id = "Gamma") public boolean gamma = true;
    @Setting(id = "Gamma Value", clamp = @Clamp(min = 5,max = 250)) public int gammaValue = 250;
    @Setting(id = "Time") public boolean time = true;
    @Setting(id = "Time Value", clamp = @Clamp(min = 5,max = 24)) public int timeValue = 24;

    @Override public void onTick(){
        if (fps) mc.gameSettings.limitFramerate = fpsValue;
        if (fov) mc.gameSettings.fovSetting = fovValue;
        if (gamma) mc.gameSettings.gammaSetting = gammaValue;
        if (time) mc.world.setWorldTime(timeValue * 1000);
    }

    @Handler
    public Listener<PacketEvent.Receive> packetEventReceive = new Listener<>(PacketEvent.Receive.class, e -> {
        if (nullCheck()) return;
        if (e.getPacket() instanceof SPacketTimeUpdate){
            if (time) e.setCancelled(true);
        }
    });

}
