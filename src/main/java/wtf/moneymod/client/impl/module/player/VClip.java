package wtf.moneymod.client.impl.module.player;

import net.minecraft.network.play.client.CPacketPlayer;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.world.ChatUtil;

@Module.Register( label = "VClip", cat = Module.Category.PLAYER )
public class VClip extends Module {
    @Value(value = "Mode") public Mode mode = Mode.DEFAULT;
    @Value(value = "Offset") @Bounds(min = -5f, max = 5f) public float offset = 0.2f;
    @Value(value = "Disable") public boolean disable = true;
    @Override public void onTick() {
        if (nullCheck()) return;
        if (mode == Mode.DEFAULT){
            mc.player.setPosition(mc.player.posX,mc.player.posY + offset, mc.player.posZ);
            mc.player.setPositionAndUpdate(mc.player.posX, mc.player.posY + offset, mc.player.posZ);
            if (disable) setToggled(false);
        } else if (mode == Mode.BYPASS) {
            ChatUtil.INSTANCE.sendInfoMessage("Teleporting...");
            for (int i = 0; i < 3; i++) {
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY - 0.001, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, -1, mc.player.posZ, mc.player.onGround));
            }
            if (disable) setToggled(false);
        }
    }

    public enum Mode{DEFAULT, BYPASS}

}