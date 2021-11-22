package wtf.moneymod.client.impl.module.global;

import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.cape.CapeEnum;

@Module.Register(label = "Globals", cat = Module.Category.GLOBAL)
public class Global extends Module {

    @Value(value = "Override cape") public boolean override;
    @Value(value = "Cape") public Mode mode = Mode.PIG;
    @Value("Pulse Speed") @Bounds(min = 0.1f, max = 3) public float pulseSpeed = 1.5f;

    public enum Mode {
        PIG, SQUIDGAME
    }

    public CapeEnum getCape() {
        for (CapeEnum c : CapeEnum.values()) {
            if (c.getCapeName().equalsIgnoreCase(String.valueOf(mode)))
                return c;
        }
        return null;
    }

    private static Global INSTANCE;

    public Global() {
        INSTANCE = this;
    }

    public static Global getInstance() {
        return INSTANCE;
    }

}
