package wtf.moneymod.client.api.forge;

import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import wtf.moneymod.client.Main;

@net.minecraftforge.fml.common.Mod( modid = Main.MODID, name = Main.NAME, version = Main.VERSION )
public class Mod {

    @EventHandler public void init(FMLInitializationEvent event) {
        Main.getMain().init();
    }

}
