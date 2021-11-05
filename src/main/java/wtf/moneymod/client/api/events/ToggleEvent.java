package wtf.moneymod.client.api.events;

import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.eventhandler.event.Event;

public class ToggleEvent extends Event {

    private final Action action;
    private final Module module;

    public ToggleEvent(Action action, Module module) {
        this.action = action;
        this.module = module;
    }

    public Module getModule() {
        return module;
    }

    public Action getAction() {
        return action;
    }

    public enum Action {
        ENABLE, DISABLE
    }

}
