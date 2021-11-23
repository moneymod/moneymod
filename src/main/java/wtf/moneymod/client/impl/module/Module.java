package wtf.moneymod.client.impl.module;

import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.events.ToggleEvent;
import wtf.moneymod.client.api.setting.Option;
import wtf.moneymod.client.api.setting.annotatable.Value;
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
    private boolean toggled, hold = false;
    private final boolean configException;
    private int key;
    public boolean drawn;

    public Module() {
        Register register = getClass().getAnnotation(Register.class);
        label = register.label();
        desc = register.desc();
        category = register.cat();
        configException = register.exception();
        key = register.key();
        drawn = true;
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

    public boolean isHold() {
        return hold;
    }

    public void setHold(boolean hold) {
        this.hold = hold;
    }

    protected void onEnable() {}

    protected void onDisable() {}

    protected void onToggle() {}

    public void onTick() {}

    public void onRender3D(float partialTicks) {}

    public void onRender2D() {}

    public void enable() {
        toggled = true;
        onToggle();
        onEnable();
        Main.EVENT_BUS.register(this);
        Main.EVENT_BUS.dispatch(new ToggleEvent(ToggleEvent.Action.ENABLE, this));
    }

    public void disable() {
        toggled = false;
        onToggle();
        onDisable();
        Main.EVENT_BUS.unregister(this);
        Main.EVENT_BUS.dispatch(new ToggleEvent(ToggleEvent.Action.DISABLE, this));
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
