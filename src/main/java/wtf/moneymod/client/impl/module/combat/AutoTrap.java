package wtf.moneymod.client.impl.module.combat;

import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.module.misc.AutoGG;
import wtf.moneymod.client.impl.utility.impl.misc.Timer;
import wtf.moneymod.client.impl.utility.impl.player.ItemUtil;
import wtf.moneymod.client.impl.utility.impl.render.Renderer3D;
import wtf.moneymod.client.impl.utility.impl.world.BlockUtil;
import wtf.moneymod.client.impl.utility.impl.world.EntityUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Module.Register(label = "AutoTrap", cat = Module.Category.COMBAT)
public class AutoTrap extends Module {

    @Value(value = "Mode") public Mode mode = Mode.FULL;
    @Value(value = "BPT") @Bounds(min = 1, max = 20) public int bpt = 8;
    @Value(value = "Delay") @Bounds(min = 1, max = 250) public int delay = 26;
    @Value(value = "Range") @Bounds(min = 1, max = 8) public float range = 5;
    @Value(value = "Disable Range") @Bounds(min = 1, max = 12) public float disableRange = 6;
    @Value( value = "Retry" ) public boolean retry = true;
    @Value( value = "Help" ) public boolean help = true;
    @Value( value = "Disable" ) public boolean disable = false;
    @Value( value = "Anti Step" ) public boolean antiStep = true;
    @Value( value = "Render" ) public boolean render = true;

    final Timer timer = new Timer( );
    boolean didPlace, rotating;
    int placed;
    Entity target;

    @SubscribeEvent public void onConnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        setToggled(false);
    }

    @SubscribeEvent public void onRender3D ( RenderWorldLastEvent event ) {
        if ( render && target != null ) {
            for ( BlockPos bp : mode == Mode.FULL ? getFull( target ) : getSimple( target ) ) {
                Renderer3D.drawBoxESP( bp, Color.WHITE, 1f, true, true, 60, 255, 1 );
            }
        }
    }

    @Override protected void onEnable ( ) {
        placed = 0;
        didPlace = false;
        timer.reset( );
        target = null;
        rotating = false;
    }

    @Override public void onTick ( ) {
        if ( nullCheck( ) ) return;
        target = EntityUtil.getTarget( range );
        if (target != null) AutoGG.target((EntityPlayer)target);

        /* CODED BY PIGHAX  23.10.21-22:30*/

        if (disableRange >= 1 && target != null && !disable) {
            if (mc.player.getDistanceSq(target) >= disableRange * disableRange) {
                setToggled(false);
                return;
            }
        }

        /* CODED BY PIGHAX  23.10.21-22:30*/


        if ( !timer.passed( delay ) && didPlace || target == null ) return;
        if ( mode == Mode.SIMPLE ) {
            if ( getSimple( target ).size( ) == 0 ) {
                if ( disable ) setToggled( false );
                rotating = false;
                return;
            }
            placeBlocks( getSimple( target ) );
        } else {
            if ( getFull( target ).size( ) == 0 ) {
                if ( disable ) setToggled( false );
                rotating = false;
                return;
            }
            placeBlocks( getFull( target ) );
        }
        placed = 0;
        timer.reset( );
    }


    List<BlockPos> getSimple ( Entity target ) {
        AtomicBoolean add = new AtomicBoolean( false );
        List<BlockPos> blocks = Stream.of( new BlockPos( target.posX, target.posY + 2.0, target.posZ ) ).filter(blockPos -> {
            switch ( BlockUtil.INSTANCE.isPlaceable( blockPos ) ) {
                case 0: {
                    if ( BlockUtil.INSTANCE.calcSide( blockPos ) == null ) {
                        add.set( true );
                    }
                    return true;
                }
                case -1: {
                    return retry;
                }
                case 1: {
                    return false;
                }
            }
            return false;
        } ).collect( Collectors.toList( ) );
        if ( add.get( ) ) {
            if ( help )
                blocks.add( new BlockPos( new BlockPos( target.posX + 1f, target.posY - 1, target.posZ ) ) );
            for ( int j = 0; j < 3; j++ ) blocks.add( new BlockPos( target.posX + 1f, target.posY + j, target.posZ ) );
        }
        return blocks;
    }


    List<BlockPos> getFull ( Entity target ) {
        List<Vec3d> vec3d = new ArrayList<>( Arrays.asList( new Vec3d( 0.0, 0.0, -1.0 ), new Vec3d( 1.0, 0.0, 0.0 ), new Vec3d( 0.0, 0.0, 1.0 ), new Vec3d( -1.0, 0.0, 0.0 ), new Vec3d( 0.0, 1.0, -1.0 ), new Vec3d( 1.0, 1.0, 0.0 ), new Vec3d( 0.0, 1.0, 1.0 ), new Vec3d( -1.0, 1.0, 0.0 ), new Vec3d( 1.0, 2.0, 0.0 ), new Vec3d( 0.0, 2.0, 0.0 ) ) );
        if ( help )
            vec3d.addAll( 0, Arrays.asList( new Vec3d( 0.0, -1.0, -1.0 ), new Vec3d( 1.0, -1.0, 0.0 ), new Vec3d( 0.0, -1.0, 1.0 ), new Vec3d( -1.0, -1.0, 0.0 ) ) );
        if ( antiStep ) vec3d.add( new Vec3d( 0.0, 3.0, 0.0 ) );
        return vec3d.stream( ).map( vec -> new BlockPos( target.getPositionVector( ) ).add( vec.x, vec.y, vec.z ) ).filter( bp -> {
            switch ( BlockUtil.INSTANCE.isPlaceable( bp ) ) {
                case 0: {
                    return true;
                }
                case -1: {
                    return retry;
                }
                case 1: {
                    return false;
                }
            }
            return false;
        } ).collect( Collectors.toList( ) );
    }

    void placeBlocks ( List<BlockPos> blockPos ) {
        rotating = true;
        for ( BlockPos bp : blockPos ) {
            if ( placed >= bpt ) return;
            int old = mc.player.inventory.currentItem;
            if ( ItemUtil.switchToHotbarSlot( ItemUtil.findHotbarBlock( BlockObsidian.class ), false ) == -1 )
                return;
            switch ( BlockUtil.INSTANCE.isPlaceable( bp ) ) {
                case 0: {
                    BlockUtil.INSTANCE.placeBlock( bp );
                    didPlace = true;
                    placed++;
                    break;
                }
                case -1: {
                    if ( retry ) {
                        BlockUtil.INSTANCE.placeBlock( bp );
                        placed++;
                    }
                    break;
                }
                case 1: {
                    break;
                }
            }
            ItemUtil.switchToHotbarSlot( old, false );
        }
    }

    public enum Mode {
        SIMPLE, FULL
    }

}
