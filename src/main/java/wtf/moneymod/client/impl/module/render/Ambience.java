package wtf.moneymod.client.impl.module.render;

import net.minecraft.network.play.server.SPacketTimeUpdate;
import wtf.moneymod.client.api.events.PacketEvent;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.render.JColor;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

@Module.Register( label = "Ambience", cat = Module.Category.RENDER )
public class Ambience extends Module {

    @Value("Light") public boolean light = false;
    @Value("Color Light") public JColor color = new JColor(255, 255, 255, 100, false);
    @Value( value = "Time" ) @Bounds( min = 5, max = 24 ) public int timeValue = 24;
    @Value( value = "Infinity Cycle" ) public boolean infinity = true;
    @Value( value = "Speed" ) @Bounds( min = 10, max = 1000 ) public int speed = 100;
    int cycle = 0;
    @Override public void onTick() {
        cycle+=speed;
        mc.world.setWorldTime(infinity ? cycle : timeValue * 1000L);
        if (cycle >= 24000) cycle = 0;
    }

    @Handler
    public Listener<PacketEvent.Receive> packetEventReceive = new Listener<>(PacketEvent.Receive.class, e -> {
        if (nullCheck()) return;
        if (e.getPacket() instanceof SPacketTimeUpdate) {
            e.setCancelled(true);
        }
    });

}
