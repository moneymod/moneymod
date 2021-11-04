package wtf.moneymod.client.api.setting.annotatable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention( RetentionPolicy.RUNTIME)
@Target( ElementType.FIELD)
public @interface Value {
    String value() default "";
}
