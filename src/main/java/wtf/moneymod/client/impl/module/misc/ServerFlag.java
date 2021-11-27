package wtf.moneymod.client.impl.module.misc;

import net.minecraft.network.play.client.CPacketPlayer;
import org.lwjgl.input.Keyboard;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;

import java.util.Arrays;
import java.util.List;

@Module.Register( label = "ServerFlag", cat = Module.Category.MISC)
public class ServerFlag extends Module {

    @Value(value = "Ticks") @Bounds(max = 64) public int ticks = 6;
    @Value(value = "Height") @Bounds(min = -5, max = 5) public float height = 4;
    @Value(value = "Timer") @Bounds(max = 50) public int timer = 6;
    @Value(value = "Timer-MiniJumo") public boolean miniJumpTimer = false;
    @Value(value = "Mode") public Mode mode = Mode.MOTION;

    int delay;
    private final List<Double> offsets = Arrays.asList( 0.4199999, 0.7531999, 1.0013359, 1.1661092 );

    @Override
    public void onToggle(){
        delay = 0;
        Main.TICK_TIMER = 1;
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;
        if (mode != Mode.TIMER) {
            for (int i = 0; i < ticks; i++) {
                if (i >= ticks){
                    setToggled(false);
                    break;
                }
                switch (mode) {
                    case PACKET:
                        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + height, mc.player.posZ, false));
                        break;
                    case MOTION:
                        mc.player.motionY = height;
                        break;
                    case FAKEJUMP:
                        offsets.forEach(offset -> mc.getConnection().sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + offset, mc.player.posZ, true)));
                        break;
                    case PACKET1:
                        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY - height, mc.player.posZ, false));
                        offsets.forEach(offset -> mc.getConnection().sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + offset, mc.player.posZ, true)));
                        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + height, mc.player.posZ, false));
                        break;
                    case PACKET2:





                        mc.player.setPositionAndUpdate(mc.player.posX, mc.player.posY + height, mc.player.posZ);
                        break;
                    case POSITON3:
                        mc.player.setPosition(mc.player.posX, mc.player.posY + height, mc.player.posZ);
                        mc.player.setPositionAndUpdate(mc.player.posX, mc.player.posY + height, mc.player.posZ);
                        break;
                    case POSITIONP:
                        mc.player.setPositionAndUpdate(mc.player.posX, mc.player.posY + height, mc.player.posZ);
                        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + height, mc.player.posZ, false));

                    case TIMER:
                        break;
                }
            }
        } else {
            delay++;
            Main.TICK_TIMER = timer;
            if (delay >= ticks){
                Main.TICK_TIMER = 1;
                delay = 0;
                setToggled(false);
            }
            if (mc.player.onGround){
                mc.player.jump();
                if (miniJumpTimer) mc.player.motionY -= 0.25;
            }

        }
    }
    public enum Mode{
        MOTION, PACKET, TIMER, FAKEJUMP, PACKET1, PACKET2, POSITION, POSITION2, POSITON3, POSITIONP
    }
}
