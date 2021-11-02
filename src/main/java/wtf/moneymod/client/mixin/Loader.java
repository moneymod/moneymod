package wtf.moneymod.client.mixin;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

import java.util.Map;

public class Loader implements IFMLLoadingPlugin {
    private static boolean isObfuscatedEnvironment = false;

    public Loader( ) {
        MixinBootstrap.init( );
        Mixins.addConfiguration( "mixins.moneymod.json" );
    }

    public String[] getASMTransformerClass( ) {
        return new String[ 0 ];
    }

    public String getModContainerClass( ) {
        return null;
    }

    public String getSetupClass( ) {
        return null;
    }

    public void injectData( Map<String, Object> data ) {
        isObfuscatedEnvironment = ( Boolean ) data.get( "runtimeDeobfuscationEnabled" );
    }

    public String getAccessTransformerClass( ) {
        return null;
    }
}
