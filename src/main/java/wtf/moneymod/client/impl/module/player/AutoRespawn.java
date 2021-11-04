package wtf.moneymod.client.impl.module.player;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.GuiGameOver;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.world.ChatUtil;

@Module.Register( label = "AutoRespawn", cat = Module.Category.PLAYER)
public class AutoRespawn extends Module {

    @Value(value = "DeathCoords") public boolean deathcoords = false;
    boolean value = false;
    @Override public void onTick() {
        if (nullCheck()) return;
        if (mc.currentScreen instanceof GuiGameOver && !value) {
            mc.player.respawnPlayer();
            mc.displayGuiScreen(null);
            value = true;
        }
        if (value) {
            if (deathcoords) ChatUtil.INSTANCE.sendMessage(ChatFormatting.GOLD + "[PlayerDeath] " + ChatFormatting.YELLOW + (int) mc.player.posX + " " + (int) mc.player.posY + " " + (int) mc.player.posZ);
            value = false;
        }
    }
}
