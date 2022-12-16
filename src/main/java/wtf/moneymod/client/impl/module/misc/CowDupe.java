package wtf.moneymod.client.impl.module.misc;

import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;

/**
 *  @author cattyn
 * @since 08.05.2022
 */
@Module.Register(label = "CowDupe", cat = Module.Category.MISC)
public class CowDupe extends Module{

    @Value("Times") @Bounds(min = 1, max = 2000) public int times = 150;

    @Override public void onTick() {
        for (int i = 0; i < times; ++i) {
            if (mc.pointedEntity == null) continue;
            mc.getConnection().sendPacket(new CPacketUseEntity(mc.pointedEntity, EnumHand.MAIN_HAND));
        }
        setToggled(false);
    }

}
