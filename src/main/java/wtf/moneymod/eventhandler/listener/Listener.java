package wtf.moneymod.eventhandler.listener;

import wtf.moneymod.eventhandler.event.Event;
import wtf.moneymod.eventhandler.event.enums.Priority;

import java.lang.reflect.Field;
import java.util.function.Consumer;

public class Listener<T extends Event> {

    private Consumer<T> consumer;
    private final Class<? extends Event> event;
    private Object object;
    private Field field;

    public Listener(Class<? extends Event> event, Consumer<T> consumer) {
        this.consumer = consumer;
        this.event = event;
    }

    public Consumer<T> getConsumer() { return consumer; }

    public Class<? extends Event> getEvent() { return event; }

    public Object getObject() { return object; }

    public void setObject(Object object) { this.object = object; }

    public Field getField() { return field; }

    public void setField(Field field) { this.field = field; }

    public Handler getHandler() {
        return field.getAnnotation(Handler.class);
    }

}
