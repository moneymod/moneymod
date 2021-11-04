package wtf.moneymod.client.impl.module.player;

import club.cafedevelopment.reflectionsettings.annotation.Clamp;
import club.cafedevelopment.reflectionsettings.annotation.Setting;
import net.minecraft.init.Items;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.util.text.TextComponentString;
import wtf.moneymod.client.impl.module.Module;

@Module.Register( label = "AutoLog", cat = Module.Category.PLAYER)
public class AutoLog extends Module {

    @Setting(id = "Health", clamp = @Clamp(min = 1,max = 36)) public int hpLog = 8;

    @Override public void onTick() {
        float hp = mc.player.getHealth() + mc.player.getAbsorptionAmount();
        if (hp <= hpLog && mc.player.getHeldItemOffhand().getItem() != Items.TOTEM_OF_UNDYING)
            mc.getConnection().handleDisconnect(new SPacketDisconnect(new TextComponentString("AutoLog, gg")));
    }
}
