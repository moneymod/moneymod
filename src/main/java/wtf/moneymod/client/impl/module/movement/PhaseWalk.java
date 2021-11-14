package wtf.moneymod.client.impl.module.movement;

import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import wtf.moneymod.client.api.events.MoveEvent;
import wtf.moneymod.client.api.events.PacketEvent;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.misc.Timer;
import wtf.moneymod.client.impl.utility.impl.world.EntityUtil;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

@Module.Register( label = "PhaseWalk", cat = Module.Category.MOVEMENT)
public class PhaseWalk extends Module {

    @Value(value = "Mode") public Mode mode = Mode.DEFAULT;
    @Value(value = "Attempts") @Bounds(min = 1, max = 5) public int attempts = 1;
    @Value(value = "Speed") @Bounds(min = 1, max = 5) public int speed = 1;
    @Value(value = "CheckGround") public boolean checkGround = true;

    Timer timer = new Timer();
    int teleportID = 0;

    @Handler
    public Listener<PacketEvent.Send> packeEventSend = new Listener<>(PacketEvent.Send.class, e -> {
        if (nullCheck()) return;
        if (e.getPacket() instanceof SPacketPlayerPosLook) {
            teleportID = ((SPacketPlayerPosLook) ((Object) e.getPacket())).getTeleportId();
            mc.getConnection().sendPacket(new CPacketConfirmTeleport(teleportID + 1));
        }
    });

    @Override
    public void onTick() {
        if (nullCheck()) return;
        if (mode == Mode.DEFAULT) {
            mc.player.motionX = 0.0; mc.player.motionY = 0.0; mc.player.motionZ = 0.0;
            if (mc.player.collidedHorizontally) {
                if (timer.isPassed()) {
                    double[] move = EntityUtil.forward(get(Type.SPEED));
                    for (int i = 0; i < attempts; ++i) {
                        sendPackets(mc.player.posX + move[0], mc.player.posY + get(Type.UPPOS), mc.player.posZ + move[1]);
                    }
                    timer.reset();
                }
            }
        }

    }

    @Handler
    public Listener<MoveEvent> onMove = new Listener<>(MoveEvent.class, e -> {
        if (nullCheck()) return;
        if (mode == Mode.BYPASS) {
            double[] forward = EntityUtil.forward(get(Type.SPEED));
            for (int i = 0; i < this.attempts; ++i)
                this.sendPackets(mc.player.posX + forward[0], mc.player.posY + get(Type.UPPOS), mc.player.posZ + forward[1]);
            e.motionX = e.motionX * 0.0001 / 10.0; e.motionZ = e.motionZ * 0.0001 / 10.0; e.motionY = e.motionY * 0.0001 / 10.0;
        } else {
            if (mc.player.collidedHorizontally){
                e.motionX = 0; e.motionZ = 0; e.motionY = 0;
            }
        }

    });

    public void sendPackets(double q, double w, double r) {
        mc.getConnection().sendPacket(new CPacketPlayer.Position(q, w, r, checkGround));
        mc.getConnection().sendPacket(new CPacketPlayer.Position(0.0, 666, 0.0, checkGround));
    }

    double get(Type type){
        if (type == Type.SPEED){return this.speed / 100.0;
        } else return (double)(mc.gameSettings.keyBindJump.isKeyDown() ? 1 : (mc.gameSettings.keyBindSneak.isKeyDown() ? -1 : 0)) * speed / 100;
    }

    public enum Mode{DEFAULT, BYPASS}
    public enum Type{SPEED,UPPOS}

}