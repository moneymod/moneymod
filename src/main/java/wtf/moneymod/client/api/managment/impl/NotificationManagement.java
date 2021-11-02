package wtf.moneymod.client.api.managment.impl;

import wtf.moneymod.client.api.managment.IManager;
import wtf.moneymod.client.impl.notifications.Notification;
import wtf.moneymod.client.impl.utility.Globals;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yoursleep
 * @since 02 November 2021
 */
public class NotificationManagement implements IManager<NotificationManagement>, Globals {
    @Override public NotificationManagement register() {
        return this;
    }
}