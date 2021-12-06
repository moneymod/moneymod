package wtf.moneymod.client.impl.utility.impl.render;

import net.minecraft.util.math.Vec3d;
import wtf.moneymod.client.impl.utility.impl.misc.Timer;

public class CrumbsUtil {
    Vec3d vector;
    Timer timer;

    public CrumbsUtil(Vec3d vector) {
        timer = new Timer();
        this.vector = vector;
    }

    public Timer getTimer() {
        return timer;
    }

    public Vec3d getVector() {
        return vector;
    }

    public class TimeHelper {
        long ms;

        public TimeHelper() {
            reset();
        }

        public boolean hasReached(long mils) {
            return System.currentTimeMillis() - ms >= mils;
        }

        public long getMS() {
            return ms;
        }

        public void reset() {
            ms = System.currentTimeMillis();
        }
    }

}