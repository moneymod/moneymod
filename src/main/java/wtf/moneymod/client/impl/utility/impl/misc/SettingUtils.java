package wtf.moneymod.client.impl.utility.impl.misc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public enum  SettingUtils {
    INSTANCE;

    public ArrayList<String> enumValues(Enum clazz) {
        return Arrays.stream(clazz.getClass().getEnumConstants()).map(Enum::name).collect(Collectors.toCollection(ArrayList::new));
    }

    public int currentEnum(Enum clazz) {
        for (int i = 0; i < clazz.getClass().getEnumConstants().length; ++i) {
            Enum e = clazz.getClass().getEnumConstants()[i];
            if (!e.name().equalsIgnoreCase(clazz.name())) continue;
            return i;
        }
        return -1;
    }

    public Enum increaseEnum(Enum clazz) {
        int index = currentEnum(clazz);
        for (int i = 0; i < clazz.getClass().getEnumConstants().length; ++i) {
            Enum e = clazz.getClass().getEnumConstants()[i];
            if (i != index + 1) continue;
            return e;
        }
        return clazz.getClass().getEnumConstants()[0];
    }

    public String getProperName(Enum clazz) {
        return Character.toUpperCase(clazz.name().charAt(0)) + clazz.name().toLowerCase().substring(1);
    }

    public Enum getProperEnum(Enum clazz, String name) {
        for (int i = 0; i < clazz.getClass().getEnumConstants().length; ++i) {
            Enum e = clazz.getClass().getEnumConstants()[i];
            if(getProperName(e).equalsIgnoreCase(name)) return e;
        }
        return clazz.getClass().getEnumConstants()[0];
    }

}
