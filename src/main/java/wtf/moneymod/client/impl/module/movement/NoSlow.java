package wtf.moneymod.client.impl.module.movement;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.events.*;
import wtf.moneymod.client.api.management.impl.PacketManagement;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.mixin.accessors.AccessorEntity;
import wtf.moneymod.client.mixin.mixins.ducks.AccessorCPacketPlayer;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

import java.nio.file.Path;
import java.util.List;

@Module.Register( label = "NoSlow", desc = "Removes movement slow down", cat = Module.Category.MOVEMENT )
public class NoSlow extends Module {

    @Value( "Items" ) public boolean items = false;
    @Value( "Inventory" ) public boolean inventory = true;
    @Value( "Ice" ) public boolean ice = true;
    @Value( "Webs" ) public boolean webs = true;
    @Value( "SoulSand" ) public boolean soulsand = true;
    @Value( "SlimeBlocks" ) public boolean slimeblocks = true;
    @Value( "Strict" ) public boolean strict = false;
    @Value( "WebsFastFall" ) public boolean fastfall = false;
    @Value( "2b2t" ) public boolean strict2b2t = false;

    private boolean idk = false;
    private boolean fastfallstate = false;

    public void onEnable( )
    {
        idk = false;
        fastfallstate = false;
    }

    public void onDisable( )
    {
        idk = false;
        fastfallstate = false;

        Blocks.ICE.setDefaultSlipperiness( 0.98f );
        Blocks.FROSTED_ICE.setDefaultSlipperiness( 0.98f );
        Blocks.PACKED_ICE.setDefaultSlipperiness( 0.98f );
        Blocks.SLIME_BLOCK.setDefaultSlipperiness( 0.8f );
        Main.TICK_TIMER = 1.0f;
    }

    @Handler
    public Listener< MotionUpdateEvent > onMotionUpdate = new Listener< >( MotionUpdateEvent.class, event ->
    {
        if( nullCheck( ) ) return;

        if( ice )
        {
            if( mc.player.getRidingEntity( ) != null )
            {
                Blocks.ICE.setDefaultSlipperiness( 0.98f );
                Blocks.FROSTED_ICE.setDefaultSlipperiness( 0.98f );
                Blocks.PACKED_ICE.setDefaultSlipperiness( 0.98f );
            }
            else
            {
                Blocks.ICE.setDefaultSlipperiness( 0.6f );
                Blocks.FROSTED_ICE.setDefaultSlipperiness( 0.6f );
                Blocks.PACKED_ICE.setDefaultSlipperiness( 0.6f );
            }
        }

        if( slimeblocks )
        {
            if( mc.player.getRidingEntity( ) != null )
                Blocks.SLIME_BLOCK.setDefaultSlipperiness( 0.8f );
            else
                Blocks.SLIME_BLOCK.setDefaultSlipperiness( 0.6f );
        }

        if( items && mc.player.isHandActive( ) && !mc.player.isRiding( ) )
        {
            if( strict2b2t && !idk && !mc.player.onGround )
            {
                idk = true;
                mc.player.connection.sendPacket( new CPacketEntityAction( mc.player, CPacketEntityAction.Action.START_SNEAKING ) );
                return;
            }
        }

        if( !idk || !mc.player.onGround ) return;

        if( !mc.player.isHandActive( ) )
        {
            idk = false;
            mc.player.connection.sendPacket( new CPacketEntityAction( mc.player, CPacketEntityAction.Action.STOP_SNEAKING ) );
        }
    } );

    @Handler
    public Listener< UpdatePlayerMoveStateEvent > onPlayerMoveStateUpdate = new Listener< >( UpdatePlayerMoveStateEvent.class, event ->
    {
        if( nullCheck( ) ) return;

        if( webs && ( ( AccessorEntity )mc.player ).isInWeb( ) )
        {
            fastfallstate = ( !mc.player.onGround && fastfall );

            // ээээ блядь я не уверен нахуй
            if( !strict && mc.player.onGround )
            {
                ( ( AccessorEntity )mc.player ).setInWeb( false );
                mc.player.motionX *= 0.05000000074505806;
                mc.player.motionZ *= 0.05000000074505806;

                if( mc.player.getRidingEntity( ) != null )
                    ( ( AccessorEntity )mc.player.getRidingEntity( ) ).setInWeb( false );
            }
        }
        else
            fastfallstate = false;

        if( items && mc.player.isHandActive( ) && !mc.player.isRiding( ) )
        {
            if( strict )
                mc.player.connection.sendPacket( new CPacketPlayerDigging( CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, mc.player.getPosition( ), EnumFacing.DOWN ) );

            mc.player.movementInput.moveForward /= 0.2f;
            mc.player.movementInput.moveStrafe /= 0.2f;
        }
    } );


    @Handler Listener<InputUpdateEvent> eventListener = new Listener<>(InputUpdateEvent.class, event -> {
        if(nullCheck())return;
        if(items && mc.player.isHandActive()) {
            if(strict) {
                PacketManagement.getInstance().add(new CPacketHeldItemChange(mc.player.inventory.currentItem));
            }
            event.getMovementInput().moveForward *= 5;
            event.getMovementInput().moveStrafe *= 5;
        }
    });

    @Handler
    public Listener< PacketEvent.Send > onSendPacket = new Listener< >( PacketEvent.Send.class, event ->
    {
        if( nullCheck( ) ) return;

        if( event.getPacket( ) instanceof CPacketPlayer )
        {
            CPacketPlayer packet = ( CPacketPlayer )event.getPacket( );
            AccessorCPacketPlayer ac = ( AccessorCPacketPlayer )packet;
            if( fastfallstate )
            {
                if( event.getPacket( ) instanceof CPacketPlayer.PositionRotation )
                {
                    mc.player.connection.sendPacket( new CPacketPlayer.Position( packet.getX( 0 ), packet.getY( 0 ), packet.getZ( 0 ), packet.isOnGround( ) ) );
                    event.cancel( );
                    return;
                }

                if( event.getPacket( ) instanceof CPacketPlayer.Rotation )
                {
                    event.cancel( );
                    return;
                }
            }

            if( items && mc.player.isHandActive( ) && !mc.player.isRiding( ) && strict2b2t )
            {
                if( mc.player.onGround && !Main.getMain( ).getModuleManager( ).get( Speed.class ).isToggled( ) )
                {
                    if( mc.player.ticksExisted % 2 == 0 )
                    {
                        if( packet.isOnGround( ) && !mc.player.isElytraFlying( ) )
                        {
                            ac.setY( packet.getY( 0 ) + 0.05 );
                        }
                    }

                    ac.setOnGround( false );
                }
            }
        }
    } );

    @Handler
    public Listener< SoulSandCollisionEvent > onSoulSandCollision = new Listener< >( SoulSandCollisionEvent.class, event ->
    {
        if( nullCheck( ) ) return;

        if( soulsand )
            event.cancel( );
    } );

    @Handler
    public Listener< MoveEvent > onMove = new Listener< >( MoveEvent.class, event ->
    {
        if( nullCheck( ) ) return;

        if( fastfallstate )
        {
            event.motionY *= checkMove( ) ? 80 : 40;
            mc.player.motionY *= checkMove( ) ? 80 : 40;
            // в рх используются числа 8 и 4, но для нас они чет мелкие прям пиздец
            // еще в рх не модифицируется mc.player.motionY поэтому если вас будет
            // флагать нцп из за этого мне как то похуй да =DDD
            // обновление 20 дней спустя: вроде не флагает
        }
    } );

    public boolean checkMove( )
    {
        return mc.player.movementInput.moveStrafe != 0.0f || mc.player.movementInput.moveForward != 0.0f;
    }
}
