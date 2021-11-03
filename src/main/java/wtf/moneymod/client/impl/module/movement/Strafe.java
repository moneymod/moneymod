package wtf.moneymod.client.impl.module.movement;

import club.cafedevelopment.reflectionsettings.annotation.Clamp;
import club.cafedevelopment.reflectionsettings.annotation.Setting;
import wtf.moneymod.client.impl.module.Module;

@Module.Register( label = "Strafe", cat = Module.Category.MOVEMENT)
public class Strafe extends Module {

    @Setting(id = "Timer") public boolean timer = true;
    @Setting(id = "AutoSprint") public boolean autoSprint = true;
    @Setting(id = "Water") public boolean water = true;


    //ya ebal ne bydy perenosit strafe s rh ibo eto pizdec cod

}
