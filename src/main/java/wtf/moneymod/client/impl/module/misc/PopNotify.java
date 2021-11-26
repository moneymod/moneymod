package wtf.moneymod.client.impl.module.misc;

import wtf.moneymod.client.api.events.TotemPopEvent;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.world.ChatUtil;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

@Module.Register( label = "PopNotify", desc = "Notifies you when somebody pops a totem", cat = Module.Category.MISC )
public class PopNotify extends Module
{
    @Value( "LocalPlayer" ) public boolean localplayer = true;

    @Handler
    public Listener< TotemPopEvent > onTotemPop = new Listener< >( TotemPopEvent.class, event ->
    {
        if( !localplayer )
        {
            if (mc.getSession().getUsername().equalsIgnoreCase(event.getEntityPlayerSP().getName()))
                return;
        }

        ChatUtil.INSTANCE.sendMessageId( String.format( "%s has popped!", event.getEntityPlayerSP( ).getName( ) ),true,843 );
    } );
}
