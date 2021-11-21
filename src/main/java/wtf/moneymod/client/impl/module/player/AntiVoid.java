package wtf.moneymod.client.impl.module.player;

import net.minecraft.init.Items;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.util.text.TextComponentString;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;

@Module.Register( label = "AntiVoid", cat = Module.Category.PLAYER)
public class AntiVoid extends Module {

    @Value(value = "Mode") public Mode mode = Mode.GLIDE;

    public enum Mode{
        GLIDE, JUMP
    }

    @Override public void onTick() {
        if (nullCheck()) return;
        if (mc.player.posY <= 0){
            if (mode == Mode.GLIDE){
                mc.player.motionY = -0.001;
            } else {
                mc.player.jump();
            }
        }
    }
}
