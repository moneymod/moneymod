package wtf.moneymod.client.impl.module;

import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.impl.utility.Globals;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author cattyn
 * @since 11/02/21
 */

public class Module implements Globals {

    private final String label, desc;
    private final Category category;
    private boolean toggled;
    private final boolean configException;
    private int key;

    public Module() {
        Register register = getClass().getAnnotation(Register.class);
        label = register.label();
        desc = register.desc();
        category = register.cat();
        configException = register.exception();
        key = register.key();
    }

    public String getLabel() {
        return label;
    }

    public String getDesc() {
        return desc;
    }

    public Category getCategory() {
        return category;
    }

    public int getKey() {
        return key;
    }

    public boolean isConfigException() {
        return configException;
    }

    public boolean isToggled() {
        return toggled;
    }

    protected void onEnable() {}

    protected void onDisable() {}

    protected void onToggle() {}

    public void onTick() {}

    public void enable() {
        toggled = true;
        onToggle();
        onEnable();
        Main.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void disable() {
        toggled = false;
        onToggle();
        onDisable();
        Main.EVENT_BUS.unregister(this);
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    public void setKey(int key) {
        this.key = key;
    }

    public void setToggled(boolean toggled) {
        if (toggled) enable();
        else disable();
    }

    public void toggle() {
        setToggled(!toggled);
    }

    @Target( ElementType.TYPE )
    @Retention( RetentionPolicy.RUNTIME )
    public @interface Register {

        String label();

        Category cat();

        String desc() default "";

        boolean exception() default false;

        int key() default Keyboard.KEY_NONE;

    }

    public enum Category {
        COMBAT("Combat"),
        PLAYER("Player"),
        MOVEMENT("Movement"),
        MISC("Misc"),
        RENDER("Render"),
        GLOBAL("Global");

        private final String name;

        Category(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

}
