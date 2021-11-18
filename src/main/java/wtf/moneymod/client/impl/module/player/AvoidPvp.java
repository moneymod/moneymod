package wtf.moneymod.client.impl.module.player;

import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;

@Module.Register( label = "AvoidPvp", cat = Module.Category.PLAYER )
public class AvoidPvp extends Module {

    @Value(value = "Tick") @Bounds(max = 16f) public int ticks = 16;

    @Override
    public void onTick() {
        if (nullCheck()) return;
        if (mc.player.posY == 1 && mc.player.onGround) {
            for (int i = 0; i < ticks; i++) {
                if (i > ticks) {
                    setToggled(false);
                    break;
                }
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.1f, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY - 5, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            }
        } else setToggled(false);
    }

}