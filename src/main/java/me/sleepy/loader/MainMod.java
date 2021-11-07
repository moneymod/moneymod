package me.sleepy.loader;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import wtf.moneymod.client.api.forge.Mod;

/**
 * @author yoursleep
 * @since 07 November 2021
 */
@net.minecraftforge.fml.common.Mod( modid = MainMod.MODID, name = MainMod.NAME, version = "1.0" )
public class MainMod {

    public static final String MODID = "moneymod";
    public static final String NAME = "Money Mod";
    public static String VERSION = "0.0";

    private wtf.moneymod.client.api.forge.Mod MOD = new Mod();

    @net.minecraftforge.fml.common.Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MOD.init(event);
    }

}

