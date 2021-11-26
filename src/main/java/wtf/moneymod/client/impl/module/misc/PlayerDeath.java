package wtf.moneymod.client.impl.module.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.GuiGameOver;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.world.ChatUtil;

@Module.Register( label = "PlayerDeath", cat = Module.Category.MISC )
public class PlayerDeath extends Module
{
    @Value( "AutoRespawn" ) public boolean autorespawn = false;
    @Value( "DeathCoords" ) public boolean deathcoords = false;

    @Override
    public void onTick( )
    {
        if (nullCheck()) return;
        if (mc.currentScreen instanceof GuiGameOver){
            if (autorespawn){
                mc.player.respawnPlayer();
                mc.displayGuiScreen(null);
            }
            if (deathcoords){
                ChatUtil.INSTANCE.sendMessage(ChatFormatting.GOLD + "[PlayerDeath] " + ChatFormatting.YELLOW + (int) mc.player.posX + " " + (int) mc.player.posY + " " + (int) mc.player.posZ);
            }
        }
    }
}
