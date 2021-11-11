package wtf.moneymod.client.impl.module.player;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import wtf.moneymod.client.api.events.PacketEvent;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Module.Register( label = "Blink", cat = Module.Category.PLAYER)
public class Blink extends Module {

    @Value(value = "Mode") public Mode mode = Mode.MANUAL;
    @Value(value = "Ticks") @Bounds(max = 32) public int tick = 8;

    int ticks;
    Queue<Packet<?>> packets = new ConcurrentLinkedQueue<>();

    @Override public void onToggle(){
        ticks = 0;
        while (!this.packets.isEmpty()) {
            mc.getConnection().sendPacket(this.packets.poll());
        }
    }

    @Override public void onTick() {
        if (nullCheck()) return;
        if (mode == Mode.TICKS){
            ticks++;
            if (ticks >= tick) setToggled(false);
        }
    }

    @Handler
    public Listener<PacketEvent.Send> packetEventSend = new Listener<>(PacketEvent.Send.class, e -> {
        if (e.getPacket() instanceof CPacketPlayer) {
            packets.add(e.getPacket());
            e.setCancelled(true);
        }
    });

    public enum Mode{MANUAL, TICKS}
}
