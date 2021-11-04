package wtf.moneymod.client.api.setting;

import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Parent;
import wtf.moneymod.client.api.setting.annotatable.Value;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

//reap i love you so much
//made by reap :)
public class Option<T> {

    private final Field field;
    private final String name;
    private T currentValue;
    private final Object host;
    private final Option<Boolean> parent;
    private final List<Option<?>> children = new ArrayList<>();
    private final float min, max;

    private Option(Field field, T currentValue, Object host, Option<Boolean> parent) {
        Value includeOp = field.getDeclaredAnnotation(Value.class);
        Bounds bounds = field.getAnnotation(Bounds.class);

        (this.field = field).setAccessible(true);
        this.name = includeOp.value().equals("") ? field.getName() : includeOp.value();
        this.currentValue = currentValue;
        this.host = host;
        this.parent = parent;
        this.min = bounds == null ? 0 : bounds.min();
        this.max = bounds == null ? 0 : bounds.max();

        if (parent != null) parent.children.add(this);
    }

    public T getValue() {
        return currentValue;
    }

    public void setValue(T value) {
        try {
            field.set(host, currentValue = value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public Option<Boolean> getParent() {
        return parent;
    }

    public List<Option<?>> getChildren() {
        return children;
    }

    public float getMin() {
        return min;
    }

    public float getMax() {
        return max;
    }

    public static List<Option<?>> getContainersForObject(Object obj) {
        List<Option<?>> containers = new ArrayList<>();

        for (Field field : obj.getClass().getDeclaredFields()) {
            if (!field.isAnnotationPresent(Value.class)) continue;

            //            if (!Modifier.isPrivate(field.getModifiers())) throw new IllegalStateException("Fields annotated with IncludeOp must not be accessible!");
            field.setAccessible(true);

            if (Modifier.isFinal(field.getModifiers()))
                throw new IllegalStateException("Fields annotated with IncludeOp must not be final!");

            AtomicReference<Option<Boolean>> parent = new AtomicReference<>();

            Parent annotation = field.getAnnotation(Parent.class);

            if (annotation != null) {
                for (Option container : containers) {
                    if (container.getName().equals(annotation.value())) {
                        parent.set(( Option<Boolean> ) container);

                        break;
                    }
                }
            }

            try {
                containers.add(new Option<>(field, field.get(obj), obj, parent.get()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return containers;
    }

    public static Optional<Option<?>> getByTargetAndId(Object target, String id) {
        return getContainersForObject(target).stream().filter(s -> s.name.equalsIgnoreCase(id)).findAny();
    }

    public String getDebugInfo() { return "ID: " + getName() + ", Value: " + getValue(); }

    public Object getHost() {
        return host;
    }

}
