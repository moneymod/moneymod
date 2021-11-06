package wtf.moneymod.client.impl.module.movement;

import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.events.MoveEvent;
import wtf.moneymod.client.api.events.PacketEvent;
import wtf.moneymod.client.api.events.UpdateWalkingPlayerEvent;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.mixin.mixins.ducks.AccessorCPacketPlayer;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

@Module.Register( label = "Speed", cat = Module.Category.MOVEMENT)
public class Speed extends Module {

    @Value(value = "Mode") public Mode mode = Mode.STRAFE;
    @Value(value = "Vanlla Speed") @Bounds(min = 1, max = 10) public float speed = 4;
    @Value(value = "Timer") public boolean timer = true;
    @Value(value = "AutoSprint") public boolean autoSprint = true;
    @Value(value = "Water") public boolean water = true;
    @Value(value = "OnGround Strict") public boolean onGroundStrict = true;

    private boolean flip = false;
    private int rhh = 0;
    private int stage = 0;
    private double moveSpeed = 0;
    private double distance = 0;

    public void onToggle ( ) {
        try {
            stage = 2;
            distance = 0;
            moveSpeed = getBaseMoveSpeed( );

            Main.TICK_TIMER = 1.0f;
            if ( autoSprint && mc.player != null )
                mc.player.setSprinting( false );
        } catch ( Exception e ) {

        }
    }

    @Handler public Listener<PacketEvent.Receive> receiveListener = new Listener<>(PacketEvent.Receive.class, e -> {
        if ( e.getPacket( ) instanceof SPacketPlayerPosLook) {
            if ( mode == Mode.STRAFE || mode == Mode.ONGROUND ) {
                rhh = 6;

                if ( mode == Mode.ONGROUND )
                    stage = 2;
                else {
                    stage = 1;
                    flip = false;
                }

                distance = 0;
                moveSpeed = getBaseMoveSpeed( );
            }
        }
    });

    @Handler public Listener<PacketEvent.Send> sendListener = new Listener<>(PacketEvent.Send.class, e -> {
        if ( e.getPacket( ) instanceof CPacketPlayer) {
            CPacketPlayer packet = ( CPacketPlayer ) e.getPacket( );

            if ( mode == Mode.ONGROUND ) {
                if ( stage == 3 )
                    ( ( AccessorCPacketPlayer ) packet ).setY( packet.getY( 0 ) + 0.4 );
            }
        }
    });

    @Handler public Listener<UpdateWalkingPlayerEvent> eventListener = new Listener<>(UpdateWalkingPlayerEvent.class, e -> {
        if ( e.getStage( ) == 0 ) {
            double d3 = mc.player.posX - mc.player.prevPosX;
            double d4 = mc.player.posZ - mc.player.prevPosZ;

            distance = Math.sqrt( d3 * d3 + d4 * d4 );
        }
    });

    @Handler public Listener<MoveEvent> moveListener = new Listener<>(MoveEvent.class, event -> {
        if ( nullCheck( ) ) return;
        if ( mc.player.isElytraFlying( ) || mc.player.fallDistance >= 4.0f ) return;
        if ( !water && ( mc.player.isInWater( ) || mc.player.isInLava( ) ) ) return;

        if ( rhh > 0 )
            rhh--;

        if ( autoSprint )
            mc.player.setSprinting( true );

        String mode = this.mode.name().toLowerCase();
        if ( mode.equals( "strafe" ) ) {
            if ( timer )
                Main.TICK_TIMER = 1.08f;

            if ( round( mc.player.posY - ( double ) ( ( int ) mc.player.posY ), 3 ) == round( 0.138, 3 ) ) {
                mc.player.motionY -= 1.0;
                event.motionY -= 0.09316090325960147;
            }

            if ( stage == 2 && isMoving( ) ) {
                if ( mc.player.collidedVertically ) {
                    event.motionY = 0.4;
                    mc.player.motionY = 0.3995;
                    flip = !flip;

                    Main.TICK_TIMER = 1.0f;

                    if ( flip )
                        moveSpeed *= 1.55f;
                    else
                        moveSpeed *= 1.395f;
                }

            } else if ( stage == 3 && isMoving( ) ) {
                double var = 0.66 * ( distance - getBaseMoveSpeed( ) );
                moveSpeed = distance - var;

                if ( timer ) {
                    if ( flip ) {
                        Main.TICK_TIMER = 1.125f;
                    } else {
                        Main.TICK_TIMER = 1.0088f;
                    }
                }
            } else {
                if ( mc.world.getCollisionBoxes( mc.player, mc.player.getEntityBoundingBox( ).offset( 0.0, mc.player.motionY, 0.0 ) ).size( ) > 0 || mc.player.collidedVertically )
                    stage = 1;

                moveSpeed = distance - distance / 159.0;
            }

            float val = 1.0f;

            val *= getBaseMoveSpeed( );
            moveSpeed = Math.max( moveSpeed, val );

            float[] dir = rhc( moveSpeed );
            event.motionX = dir[ 0 ];
            event.motionZ = dir[ 1 ];

            stage++;
        } else if ( mode.equals( "onground" ) ) {
            if ( mc.player.collidedHorizontally || !checkMove( ) )
                Main.TICK_TIMER = 1.0f;
            else {
                if ( !onGroundStrict ) {
                    if ( !mc.player.onGround )
                        Main.TICK_TIMER = 1.0f;
                    else {
                        if ( stage == 2 ) {
                            Main.TICK_TIMER = 1.0f;
                            if ( rhh > 0 )
                                moveSpeed = getBaseMoveSpeed( );
                            moveSpeed *= 2.149;
                            stage = 3;
                        } else if ( stage == 3 ) {
                            if ( timer )
                                Main.TICK_TIMER = Math.max( 1.0f + new Random( ).nextFloat( ), 1.2f );
                            else
                                Main.TICK_TIMER = 1.0f;

                            stage = 2;

                            double var = 0.66 * ( distance - getBaseMoveSpeed( ) );
                            moveSpeed = distance - var;
                        }
                    }
                }

                moveSpeed = Math.max( moveSpeed, getBaseMoveSpeed( ) );
                rhQ_rhP( event, moveSpeed );
            }
        } else if ( mode.equals( "groundstrafe" ) ) {
            if ( mc.player.collidedHorizontally || mc.player.movementInput.sneak ) return;

            if ( mc.player.isHandActive( ) ) {
                if ( mc.player.getHeldItemMainhand( ).getItem( ) instanceof ItemFood)
                    return;
            }

            if ( !checkMove( ) ) return;

            if ( mc.player.onGround ) {
                if ( mc.player.ticksExisted % 2 == 0 ) {
                    Main.TICK_TIMER = 1.0f;
                    stage = 2;
                } else {
                    if ( timer )
                        Main.TICK_TIMER = 1.2f;
                    else
                        Main.TICK_TIMER = 1.0f;

                    stage = 3;
                }

                moveSpeed = getBaseMoveSpeed( );
            } else {
                Main.TICK_TIMER = 1.0f;
                stage = 0;
            }

            moveSpeed = Math.max( moveSpeed, getBaseMoveSpeed( ) );
            rhQ_rhP( event, moveSpeed );
        } else if ( mode.equals( "vanilla" ) ) {
            double speedval = ( double ) speed / 5.0f;
            rhQ_rhP( event, speedval );
        }
    });

    public double getBaseMoveSpeed ( ) {
        double d = 0.2873;

        if ( mc.player != null ) {
            if ( mc.player.isPotionActive( MobEffects.SPEED ) ) {
                int n = mc.player.getActivePotionEffect( MobEffects.SPEED ).getAmplifier( );
                d *= 1.0 + 0.2 * ( double ) ( n + 1 );
            }
        }

        return d;
    }

    public void rhQ_rhP (MoveEvent event, double speed ) {
        float moveForward = mc.player.movementInput.moveForward;
        float moveStrafe = mc.player.movementInput.moveStrafe;
        float rotationYaw = mc.player.rotationYaw;

        if ( moveForward == 0.0f && moveStrafe == 0.0f ) {
            event.motionX = 0;
            event.motionZ = 0;

            return;
        } else if ( moveForward != 0.0f ) {
            if ( moveStrafe >= 1.0f ) {
                rotationYaw += moveForward > 0.0f ? -45.0f : 45.0f;
                moveStrafe = 0.0f;
            } else if ( moveStrafe <= -1.0f ) {
                rotationYaw += moveForward > 0.0f ? 45.0f : -45.0f;
                moveStrafe = 0.0f;
            }

            if ( moveForward > 0.0f )
                moveForward = 1.0f;
            else if ( moveForward < 0.0f )
                moveForward = -1.0f;
        }

        double motionX = Math.cos( Math.toRadians( rotationYaw + 90.0f ) );
        double motionZ = Math.sin( Math.toRadians( rotationYaw + 90.0f ) );

        double newX = moveForward * speed * motionX + moveStrafe * speed * motionZ;
        double newZ = moveForward * speed * motionZ - moveStrafe * speed * motionX;

        event.motionX = newX;
        event.motionZ = newZ;
    }

    // rh_v.rhf
    // я просто спастил код выше поэтому это не оно
    public float[] rhf ( float yaw, double niggers ) {
        float moveForward = mc.player.movementInput.moveForward;
        float moveStrafe = mc.player.movementInput.moveStrafe;
        float rotationYaw = yaw;

        if ( moveForward == 0.0f && moveStrafe == 0.0f ) {
            float[] ret = new float[ 2 ];
            ret[ 0 ] = 0.0f;
            ret[ 1 ] = 0.0f;
            return ret;
        } else if ( moveForward != 0.0f ) {
            if ( moveStrafe >= 1.0f ) {
                rotationYaw += moveForward > 0.0f ? -45.0f : 45.0f;
                moveStrafe = 0.0f;
            } else if ( moveStrafe <= -1.0f ) {
                rotationYaw += moveForward > 0.0f ? 45.0f : -45.0f;
                moveStrafe = 0.0f;
            }

            if ( moveForward > 0.0f )
                moveForward = 1.0f;
            else if ( moveForward < 0.0f )
                moveForward = -1.0f;
        }

        double motionX = Math.cos( Math.toRadians( rotationYaw + 90.0f ) );
        double motionZ = Math.sin( Math.toRadians( rotationYaw + 90.0f ) );

        double newX = moveForward * niggers * motionX + moveStrafe * niggers * motionZ;
        double newZ = moveForward * niggers * motionZ - moveStrafe * niggers * motionX;

        float[] ret = new float[ 2 ];
        ret[ 0 ] = ( float ) newX;
        ret[ 1 ] = ( float ) newZ;
        return ret;
    }

    // rh_v.rhc
    public float[] rhc ( double niggers ) {
        float yaw = mc.player.prevRotationYaw + ( mc.player.rotationYaw - mc.player.prevRotationYaw ) * mc.getRenderPartialTicks( );
        return rhf( yaw, niggers );
    }

    public boolean isMoving ( ) {
        return mc.player.movementInput.moveForward != 0.0f || mc.player.movementInput.moveStrafe != 0.0f;
    }

    public double round ( double value, int places ) {
        BigDecimal b = new BigDecimal( value ).setScale( places, RoundingMode.HALF_UP );
        return b.doubleValue( );
    }

    // rhQ.rhV
    public boolean checkMove ( ) {
        return mc.player.moveForward != 0.0F || mc.player.moveStrafing != 0.0F;
    }

    public enum Mode {
        STRAFE, ONGROUND, GROUNDSTRAGE, VANILLA
    }

}
