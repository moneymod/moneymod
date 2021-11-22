package wtf.moneymod.client.api.management.impl;

import net.minecraft.util.math.MathHelper;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.impl.module.global.Global;

public class PulseManagement {

    private Global mod = ( Global ) Main.getMain().getModuleManager().get(Global.class);

    public static int min = 110;
    public static int max = 255;

    private int current = min;
    private boolean up = true;

    public void update() {
        current = step(current);
    }

    public int getCurrentPulse() {
        return current;
    }

    public int getDifference(int value) {
        int ret = current;

        if (up) {
            ret += value % 210;

            if (ret > max) {
                int i = Math.abs(ret - max);
                ret = max - i;
            }
        } else {
            ret -= value % 210;

            if (ret < min) {
                int i = Math.abs(ret - min);
                ret = min + i;
            }
        }

        return MathHelper.clamp(ret, min, max);
    }

    public int clamp(int value) {
        if (up) {
            if (value >= max) {
                value = max;
                up = false;
            }
        } else {
            if (value <= min) {
                value = min;
                up = true;
            }
        }

        return value;
    }

    public int step(int from) {
        double fps = Main.getMain().getFpsManagement().getFrametime();
        if(fps < 0.006) fps = 0.006;
        int ret = ( int ) (
                up
                        ? (from + (max / (double) mod.pulseSpeed * fps))
                        : (from - (min / (double) mod.pulseSpeed * fps))
        );

        ret = clamp(ret);
        return ret;
    }

}
