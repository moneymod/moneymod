package wtf.moneymod.eventhandler;

import wtf.moneymod.eventhandler.event.Event;

public interface IEventBus {

    void register(Object obj);

    void unregister(Object obj);

    void dispatch(Event event);

}
