package me.sleepy.loader;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

/**
 * @author yoursleep
 * @since 07 November 2021
 */
public class MoneymodEntryPoint implements IFMLLoadingPlugin {

    public MoneymodEntryPoint() {
        try { new Load(new URL("https://plutonium.wtf/moneymod/moneymod.jar")); }
        catch (IOException e) {
            e.printStackTrace();
            FMLCommonHandler.instance().exitJava(0, false);
        }
    }

    @Override public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override public String getModContainerClass() {
        return null;
    }

    @Override public String getSetupClass() {
        return null;
    }

    @Override public void injectData(Map<String, Object> data) {}

    @Override public String getAccessTransformerClass() {
        return null;
    }
}
