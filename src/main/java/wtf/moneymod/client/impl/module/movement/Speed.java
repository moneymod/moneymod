package wtf.moneymod.client.impl.module.movement;

import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;

@Module.Register( label = "Speed", cat = Module.Category.MOVEMENT)
public class Speed extends Module {

    @Value(value = "Timer") public boolean timer = true;
    @Value(value = "AutoSprint") public boolean autoSprint = true;
    @Value(value = "Water") public boolean water = true;


}
