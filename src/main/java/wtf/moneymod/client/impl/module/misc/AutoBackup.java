package wtf.moneymod.client.impl.module.misc;

import net.minecraft.init.Items;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.module.combat.AutoCrystal;
import wtf.moneymod.client.impl.utility.impl.player.ItemUtil;

@Module.Register( label = "AutoBackup", cat = Module.Category.MISC)
public class AutoBackup extends Module {

    @Override
    public void onEnable(){
        if (nullCheck()) return;
        int x = mc.player.getPosition().getX(), y = mc.player.getPosition().getY(), z = mc.player.getPosition().getZ();
        Main.getMain().getCapeThread().sendChatMessage(String.format("I need Backup: %s, %s, %s!", x, y, z));
        setToggled(false);
    }
}
