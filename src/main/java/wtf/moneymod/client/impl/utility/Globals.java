package wtf.moneymod.client.impl.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.Minecraft;

/**
 * @author cattyn
 * @since 11/02/21
 */

public interface Globals {

    Minecraft mc = Minecraft.getMinecraft();
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    default boolean nullCheck() {
        return mc.player == null || mc.world == null;
    }

}
