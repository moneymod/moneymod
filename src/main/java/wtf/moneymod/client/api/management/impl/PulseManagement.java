package wtf.moneymod.client.api.management.impl;

import net.minecraft.util.math.MathHelper;
import wtf.moneymod.client.Main;

public class PulseManagement {

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
        int ret = ( int ) (
                up
                        ? (from + (max / 1.5D * Main.getMain().getFpsManagement().getFrametime()))
                        : (from - (min / 1.5D * Main.getMain().getFpsManagement().getFrametime()))
        );

        ret = clamp(ret);
        return ret;
    }

}
