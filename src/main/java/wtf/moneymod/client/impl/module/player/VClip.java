package wtf.moneymod.client.impl.module.player;

import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;

@Module.Register( label = "VClip", cat = Module.Category.PLAYER )
public class VClip extends Module {

    @Value(value = "Offset") @Bounds(min = -5f, max = 5f) public float offset = 0.2f;
    @Value(value = "Disable") public boolean disable = true;
    @Value(value = "Mode") public Mode mode = Mode.DEFAULT;

    double[] packets = new double[] {0.41, 0.75, 1, 1.16 };

    @Override public void onTick() {
        if (nullCheck()) return;
        if (mode == Mode.DEFAULT){
            mc.player.setPosition(mc.player.posX,mc.player.posY + offset, mc.player.posZ);
            mc.player.setPositionAndUpdate(mc.player.posX, mc.player.posY + offset, mc.player.posZ);
            if (disable) setToggled(false);
        } else if (mode == Mode.BYPASS) {
            if (mc.player.onGround && mc.player.posY == 1) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
                for (double jumpOffset : packets)
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + jumpOffset, mc.player.posZ, true));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, -(mc.player.posY + offset), mc.player.posZ, true));
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                if (disable) setToggled(false);
            }
        }
    }

    public enum Mode{DEFAULT, BYPASS}

}