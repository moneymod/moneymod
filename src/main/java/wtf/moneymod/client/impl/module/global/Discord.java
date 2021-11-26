package wtf.moneymod.client.impl.module.global;

import wtf.moneymod.client.DiscordMod;
import wtf.moneymod.client.impl.module.Module;

@Module.Register(label = "Discords", cat = Module.Category.GLOBAL)
public class Discord extends Module {

    @Override
    public void onEnable(){
        DiscordMod.start();
    }
    @Override
    public void onDisable(){
        DiscordMod.stop();
    }

}
