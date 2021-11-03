package wtf.moneymod.eventhandler.listener;

import wtf.moneymod.eventhandler.event.enums.Era;
import wtf.moneymod.eventhandler.event.enums.Priority;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.FIELD )
public @interface Handler {

    Priority priority() default Priority.MEDIUM;

    Era era() default Era.NONE;

}
