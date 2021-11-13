package wtf.moneymod.client.impl.module.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.player.EntityPlayer;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.module.combat.AutoCrystal;
import wtf.moneymod.client.impl.utility.impl.world.ChatUtil;

import java.util.Random;

@Module.Register( label = "AutoGG", cat = Module.Category.MISC)
public class AutoGG extends Module {

    @Value(value = "Logic") public Mode mode = Mode.DEFAULT;
    @Value(value = "KillStreak") public boolean killStreakss = false;
    int killStreak;
    private static EntityPlayer target;
    Random random = new Random();
    String message = "Good Game! My Dick is Stuck In Car Exhaust Pipe It Hurts. Thanks To MoneyMod";
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
        if (this.target != null && mc.player.getDistanceSq(this.target) < 150) {
            if (this.target.isDead || this.target.getHealth() <= 0) {
                switch (mode){
                    case RANDOM:
                        mc.player.sendChatMessage(randomMessage[random.nextInt(randomMessage.length)]);
                        break;
                    case DEFAULT:
                        mc.player.sendChatMessage(message);
                        break;
                    case NONE:
                        break;

                }
                if (killStreakss){
                    killStreak+=1;
                    ChatUtil.INSTANCE.sendMessage(String.format("%s[KillStreak] %s%d %skills!", ChatFormatting.GOLD,ChatFormatting.YELLOW,killStreak,ChatFormatting.YELLOW));
                }
                this.target = null;
            }
        }
    }
    public enum Mode{DEFAULT, RANDOM, NONE}

    @Override
    public void onToggle(){
        this.target = null;
        killStreak = 0;
    }

}
