package wtf.moneymod.client.impl.module.player;

import net.minecraft.init.Items;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.util.text.TextComponentString;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;

@Module.Register( label = "AutoLog", cat = Module.Category.PLAYER)
public class AutoLog extends Module {

    @Value(value = "Health") @Bounds(min = 1,max = 36) public int hpLog = 8;

    @Override public void onTick() {
        float hp = mc.player.getHealth() + mc.player.getAbsorptionAmount();
        if (hp <= hpLog && mc.player.getHeldItemOffhand().getItem() != Items.TOTEM_OF_UNDYING)
            mc.getConnection().handleDisconnect(new SPacketDisconnect(new TextComponentString("AutoLog, gg")));
    }
}
