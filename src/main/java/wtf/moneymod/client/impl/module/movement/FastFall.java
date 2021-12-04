package wtf.moneymod.client.impl.module.movement;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.events.MoveEvent;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.mixin.accessors.IEntity;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

import java.util.HashMap;
import java.util.Map;

@Module.Register( label = "FastFall", cat = Module.Category.MOVEMENT)
public class FastFall extends Module {

    @Value(value = "Speed") @Bounds(max = 15) public float speed = 10;
    @Value(value = "Web") public boolean web = false;
    @Value(value = "Mode") public Mode mode = Mode.MOTION;
    @Value( "Stop" ) public boolean stop = true;
    @Value(value = "Timer Speed") @Bounds(max = 8) public float timerSpeed = 2;
    @Value(value = "Motion Speed") @Bounds(max = 4) public float motionSpeed = 0.4f;
    public enum Mode{
        TIMER, MOTION, SKIP
    }

    @Override
    public void onToggle(){
        Main.TICK_TIMER = 1;
    }

    @Override
    public void onTick() {
        if (mc.player.isInWater() || mc.player.isInLava()) return;
        if (mc.player.onGround) mc.player.motionY -= speed / 10;

        if ((( IEntity ) mc.player).isInWeb() && !mc.player.onGround && mc.gameSettings.keyBindSneak.isKeyDown()) {
            switch (mode){
                case MOTION:
                    mc.player.motionY = -motionSpeed;
                    break;
                case TIMER:
                    Main.TICK_TIMER = timerSpeed * 4;
                    break;
                case SKIP:
                    mc.player.motionY = -55;
                    break;

            }
        } else
            Main.TICK_TIMER = 1;
    }

    @Handler
    public Listener< MoveEvent > onMove = new Listener< >( MoveEvent.class, event ->
    {
        if (mc.player.isInWater() || mc.player.isInLava()) return;

        if( stop && trace( ) && mc.player.onGround )
        {
            event.motionX *= 0.05;
            event.motionY -= speed / 10;
            event.motionZ *= 0.05;
        }
    } );

    private boolean trace( )
    {
        AxisAlignedBB bbox = mc.player.getEntityBoundingBox( );
        Vec3d basepos = bbox.getCenter( );

        double minX = bbox.minX;
        double minZ = bbox.minZ;
        double maxX = bbox.maxX;
        double maxZ = bbox.maxZ;

        Map< Vec3d, Vec3d > positions = new HashMap< >( );

        positions.put(
                basepos,
                    new Vec3d( basepos.x, basepos.y - 1, basepos.z ) );

        positions.put(
                new Vec3d( minX, basepos.y, minZ ),
                new Vec3d( minX, basepos.y - 1, minZ ) );

        positions.put(
                new Vec3d( maxX, basepos.y, minZ ),
                new Vec3d( maxX, basepos.y - 1, minZ ) );

        positions.put(
                new Vec3d( minX, basepos.y, maxZ ),
                new Vec3d( minX, basepos.y - 1, maxZ ) );

        positions.put(
                new Vec3d( maxX, basepos.y, maxZ ),
                new Vec3d( maxX, basepos.y - 1, maxZ ) );

        for( Vec3d key : positions.keySet( ) )
        {
            RayTraceResult result = mc.world.rayTraceBlocks( key, positions.get( key ), true );
            if( result != null && result.typeOfHit == RayTraceResult.Type.BLOCK )
                return false;
        }

        // one final trace
        RayTraceResult result = mc.world.rayTraceBlocks(
                mc.player.getPositionVector( ),
                new Vec3d( mc.player.getPositionVector( ).x, mc.player.getPositionVector( ).y - 1, mc.player.getPositionVector( ).z ) );

        if( result == null ) return true;

        return result.typeOfHit != RayTraceResult.Type.BLOCK;
    }
}
