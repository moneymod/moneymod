package wtf.moneymod.client.api.management.impl;

import wtf.moneymod.client.api.management.IManager;

import java.util.ArrayList;

public final class FriendManagement extends ArrayList<String> implements IManager<FriendManagement> {

    private static final FriendManagement INSTANCE = new FriendManagement();

    @Override public FriendManagement register() {
        return this;
    }

    public static FriendManagement getInstance() {
        return INSTANCE;
    }

    public boolean is(String name) {
        return stream().anyMatch(friend -> friend.equalsIgnoreCase(name));
    }

}
