package wtf.moneymod.eventhandler;

import wtf.moneymod.eventhandler.event.Event;
import wtf.moneymod.eventhandler.event.enums.Era;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class EventBus implements IEventBus {

    private final Map<Class<? extends Event>, CopyOnWriteArrayList<Listener>> listeners = new ConcurrentHashMap<>();

    @Override public void register(Object obj) {
        Arrays.stream(obj.getClass().getFields())
                .filter(field -> field.isAnnotationPresent(Handler.class))
                .forEach(field -> {
                    if (!field.isAccessible()) field.setAccessible(true);

                    Listener<?> listener = null;

                    try {
                        listener = ( Listener<?> ) field.get(obj);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                    assert listener != null;
                    Class<? extends Event> event = listener.getEvent();

                    listener.setObject(obj);
                    listener.setField(field);

                    if (!listeners.containsKey(event)) listeners.put(event, new CopyOnWriteArrayList<>());

                    listeners.get(event).add(listener);
                    listeners.get(event).sort(Comparator.comparing(e -> -e.getHandler().priority().priority));
                });
    }

    @Override public void unregister(Object obj) {
        listeners.values().forEach(arrayList -> {
            arrayList.removeIf(listener -> listener.getObject().equals(obj));
        });
    }

    @Override public void dispatch(Event event) {
        List<Listener> listenersList = listeners.get(event.getClass());
        if (listenersList != null) for (Listener listener : listenersList) {
            if (event.isCancelled()) return;
            if (listener.getHandler().era() == Era.NONE || listener.getHandler().era() == event.getEra()) {
                listener.getConsumer().accept(event);
            }
        }
    }

}
