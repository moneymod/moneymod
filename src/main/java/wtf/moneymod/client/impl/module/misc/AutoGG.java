package wtf.moneymod.client.impl.module.misc;

import net.minecraft.entity.player.EntityPlayer;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.module.combat.AutoCrystal;

import java.util.Random;

@Module.Register( label = "AutoGG", cat = Module.Category.MOVEMENT)
public class AutoGG extends Module {

    @Value(value = "Logic") public Mode mode = Mode.DEFAULT;

    private static EntityPlayer target;
    Random random = new Random();
    String message = "Good Game! My Dick is Stuck In Car Exhaust Pipe It Hurts. Thanks To MoneyMod+3";
    String[] randomMessage = new String[]{
            "is a noob hahaha fobus on tope",
            "Good fight! Konas owns me and all",
            "I guess konas ca is too fast for you",
            "you just got nae nae'd by konas",
            "so ez lmao",
            "you just got nae nae'd by wurst+1",
            "you just got nae nae'd by wurst+2",
            "you just got nae nae'd by wurst+3",
    };
    public static void target(EntityPlayer name){ target = name; }
    @Override
    public void onTick(){
        if (target != null && mc.player.getDistanceSq(target) < 150) {
            if (target.isDead || target.getHealth() <= 0) {
                switch (mode){
                    case RANDOM:
                        mc.player.sendChatMessage(randomMessage[random.nextInt(randomMessage.length)]);
                        break;
                    case DEFAULT:
                        mc.player.sendChatMessage(message);
                        break;
                }
            }
            target = null;
        }
    }
    public enum Mode{DEFAULT, RANDOM}

}
