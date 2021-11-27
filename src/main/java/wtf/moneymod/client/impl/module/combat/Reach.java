package wtf.moneymod.client.impl.module.combat;

import wtf.moneymod.client.api.events.BlockReachEvent;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

@Module.Register( label = "Reach", cat = Module.Category.COMBAT )
public class Reach extends Module
{
    @Value( "Range" ) @Bounds( min = 0.1f, max = 10.f ) public float range = 1.f;

    @Handler
    public Listener< BlockReachEvent > onBlockReach = new Listener< >( BlockReachEvent.class, event ->
    {
        event.setDistance( range );
    } );
}
