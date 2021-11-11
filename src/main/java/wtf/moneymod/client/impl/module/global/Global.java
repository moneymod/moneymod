package wtf.moneymod.client.impl.module.global;

import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;

@Module.Register( label = "Globals", cat = Module.Category.GLOBAL )
public class Global extends Module {

    @Value( value = "Chat Animation" ) public boolean chatAnim = true;
    private static Global INSTANCE;

    public Global() {
        INSTANCE = this;
    }

    public static Global getInstance() {
        return INSTANCE;
    }

}
