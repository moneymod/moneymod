package wtf.moneymod.client.impl.module.render;

import wtf.moneymod.client.api.events.CameraClipEvent;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

@Module.Register( label = "ViewClip", cat = Module.Category.RENDER )
public class ViewClip extends Module
{
    @Value( "Distance" ) @Bounds( min = 0.1f, max = 30 ) public float distance = 4f;

    @Handler
    public Listener< CameraClipEvent > onCameraClip = new Listener< >( CameraClipEvent.class, event ->
    {
        event.distance = this.distance;
    } );
}
