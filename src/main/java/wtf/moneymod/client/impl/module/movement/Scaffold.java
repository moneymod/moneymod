package wtf.moneymod.client.impl.module.movement;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import wtf.moneymod.client.api.events.MotionUpdateEvent;
import wtf.moneymod.client.api.events.MoveEvent;
import wtf.moneymod.client.api.events.Render3DEvent;
import wtf.moneymod.client.api.events.RenderPlayerRotationsEvent;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.misc.Timer;
import wtf.moneymod.client.impl.utility.impl.render.Renderer3D;
import wtf.moneymod.client.impl.utility.impl.world.ChatUtil;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

import java.awt.*;

@Module.Register( label = "Scaffold", desc = "Automatically towers with blocks", cat = Module.Category.MOVEMENT )
public class Scaffold extends Module
{
    @Value( "Rotate" ) public boolean rotate = true;
    @Value( "AutoSwap" ) public boolean autoswap = true;
    @Value( "Tower" ) public boolean tower = true;
    @Value( "SafeWalk" ) public boolean safewalk = true;
    @Value( "EchestHolding" ) public boolean echestholding = false;
    @Value( "Render" ) public boolean render = true;
    @Value( "Color" ) public Color color = new Color( Color.CYAN.getRed( ), Color.CYAN.getGreen( ), Color.CYAN.getBlue( ), 50 );

    private Timer timer;
    private BlockPosWithFacing currentblock;

    public Scaffold( )
    {
        super( );
        timer = new Timer( );
    }

    private boolean isBlockValid( Block block )
    {
        /*switch( blocks.getType( ) )
        {
            case BLACKLIST:
                return !blocks.contains( block );
            case WHITELIST:
                return blocks.contains( block );
            default: break;
        }*/

        return block.getDefaultState( ).getMaterial( ).isSolid( );
    }

    private BlockPosWithFacing checkNearBlocks( BlockPos blockPos )
    {
        if( isBlockValid( mc.world.getBlockState( blockPos.add( 0, -1, 0 ) ).getBlock( ) ) )
            return new BlockPosWithFacing( blockPos.add( 0, -1, 0 ), EnumFacing.UP );
        else if( isBlockValid( mc.world.getBlockState( blockPos.add( -1, 0, 0 ) ).getBlock( ) ) )
            return new BlockPosWithFacing( blockPos.add( -1, 0, 0 ), EnumFacing.EAST );
        else if( isBlockValid( mc.world.getBlockState( blockPos.add( 1, 0, 0 ) ).getBlock( ) ) )
            return new BlockPosWithFacing( blockPos.add( 1, 0, 0 ), EnumFacing.WEST );
        else if( isBlockValid( mc.world.getBlockState( blockPos.add( 0, 0, 1 ) ).getBlock( ) ) )
            return new BlockPosWithFacing( blockPos.add( 0, 0, 1 ), EnumFacing.NORTH );
        else if( isBlockValid( mc.world.getBlockState( blockPos.add( 0, 0, -1 ) ).getBlock( ) ) )
            return new BlockPosWithFacing( blockPos.add( 0, 0, -1 ), EnumFacing.SOUTH );

        return null;
    }

    // Dayum Fuck Dis Shiet
    private BlockPosWithFacing checkNearBlocksExtended( BlockPos blockPos )
    {
        BlockPosWithFacing ret = null;

        ret = checkNearBlocks( blockPos );
        if( ret != null ) return ret;

        ret = checkNearBlocks( blockPos.add( -1, 0, 0 ) );
        if( ret != null ) return ret;

        ret = checkNearBlocks( blockPos.add( 1, 0, 0 ) );
        if( ret != null ) return ret;

        ret = checkNearBlocks( blockPos.add( 0, 0, 1 ) );
        if( ret != null ) return ret;

        ret = checkNearBlocks( blockPos.add( 0, 0, -1 ) );
        if( ret != null ) return ret;

        ret = checkNearBlocks( blockPos.add( -2, 0, 0 ) );
        if( ret != null ) return ret;

        ret = checkNearBlocks( blockPos.add( 2, 0, 0 ) );
        if( ret != null ) return ret;

        ret = checkNearBlocks( blockPos.add( 0, 0, 2 ) );
        if( ret != null ) return ret;

        ret = checkNearBlocks( blockPos.add( 0, 0, -2 ) );
        if( ret != null ) return ret;

        ret = checkNearBlocks( blockPos.add( 0, -1, 0 ) );
        BlockPos blockPos2 = blockPos.add( 0, -1, 0 );

        if( ret != null ) return ret;

        ret = checkNearBlocks( blockPos2.add( 1, 0, 0 ) );
        if( ret != null ) return ret;

        ret = checkNearBlocks( blockPos2.add( -1, 0, 0 ) );
        if( ret != null ) return ret;

        ret = checkNearBlocks( blockPos2.add( 0, 0, 1 ) );
        if( ret != null ) return ret;

        return checkNearBlocks( blockPos2.add( 0, 0, -1 ) );
    }

    private int findBlockToPlace( )
    {
        if( mc.player.getHeldItemMainhand( ).getItem( ) instanceof ItemBlock )
        {
            if( isBlockValid( ( ( ItemBlock )mc.player.getHeldItemMainhand( ).getItem( ) ).getBlock( ) ) )
                return mc.player.inventory.currentItem;
        }

        int n = 0;
        int n2 = 0;

        while( true )
        {
            if( n2 >= 9 ) break;

            if( mc.player.inventory.getStackInSlot( n ).getCount( ) != 0 )
            {
                if( mc.player.inventory.getStackInSlot( n ).getItem( ) instanceof ItemBlock )
                {
                    if( !echestholding ||
                            ( echestholding && !mc.player.inventory.getStackInSlot( n ).getItem( ).equals( Item.getItemFromBlock( Blocks.ENDER_CHEST ) ) ) )
                    {
                        if( isBlockValid( ( ( ItemBlock )mc.player.inventory.getStackInSlot( n ).getItem( ) ).getBlock( ) ) )
                            return n;
                    }
                }
            }

            n2 = ++n;
        }

        return -1;
    }

    private boolean someblockcheck( int itemnum )
    {
        Item item = mc.player.inventory.getStackInSlot( itemnum ).getItem( );

        if( item instanceof ItemBlock )
        {
            Vec3d vec3d = mc.player.getPositionVector( );
            Block block = ( ( ItemBlock )item ).getBlock( );

            return mc.world.rayTraceBlocks( vec3d, vec3d.add( 0.0, -block.getDefaultState( ).getSelectedBoundingBox( mc.world, BlockPos.ORIGIN ).maxY, 0.0 ), false, true, false ) == null;
        }

        return false;
    }

    private int countValidBlocks( )
    {
        int n = 36;
        int n2 = 0;

        while( true )
        {
            if( n >= 45 ) break;

            if( mc.player.inventoryContainer.getSlot( n ).getHasStack( ) )
            {
                ItemStack itemStack = mc.player.inventoryContainer.getSlot( n ).getStack( );
                if( itemStack.getItem( ) instanceof ItemBlock )
                {
                    if( isBlockValid( ( ( ItemBlock )itemStack.getItem( ) ).getBlock( ) ) )
                        n2 += itemStack.getCount( );
                }
            }

            n++;
        }

        return n2;
    }

    private Vec3d getEyePosition( )
    {
        return new Vec3d( mc.player.posX, mc.player.posY + mc.player.getEyeHeight( ), mc.player.posZ );
    }

    private float[ ] getRotations( BlockPos blockPos, EnumFacing enumFacing )
    {
        Vec3d vec3d = new Vec3d( ( double )blockPos.getX( ) + 0.5, mc.world.getBlockState( blockPos ).getSelectedBoundingBox( mc.world, blockPos ).maxY - 0.01, ( double )blockPos.getZ( ) + 0.5 );
        vec3d = vec3d.add( new Vec3d( enumFacing.getDirectionVec( ) ).scale( 0.5 ) );

        Vec3d vec3d2 = getEyePosition( );

        double d = vec3d.x - vec3d2.x;
        double d2 = vec3d.y - vec3d2.y;
        double d3 = vec3d.z - vec3d2.z;
        double d4 = d;
        double d5 = d3;
        double d6 = Math.sqrt( d4 * d4 + d5 * d5 );

        float f = ( float )( Math.toDegrees( Math.atan2( d3, d ) ) - 90.0f );
        float f2 = ( float )( -Math.toDegrees( Math.atan2( d2, d6 ) ) );

        float[ ] ret = new float[ 2 ];
        ret[ 0 ] = mc.player.rotationYaw + MathHelper.wrapDegrees( ( float )( f - mc.player.rotationYaw ) );
        ret[ 1 ] = mc.player.rotationPitch + MathHelper.wrapDegrees( ( float )( f2 - mc.player.rotationPitch ) );

        return ret;
    }

    @Handler
    public Listener< Render3DEvent > onRender3D = new Listener< >( Render3DEvent.class, event ->
    {
        if( render && currentblock != null )
        {
            GlStateManager.pushMatrix( );

            Renderer3D.rhK( currentblock.blockPos, true, true, color );

            GlStateManager.popMatrix( );
        }
    } );

    @Handler
    public Listener< RenderPlayerRotationsEvent > onRotationsRender = new Listener< >( RenderPlayerRotationsEvent.class, event ->
    {
        if( rotate && currentblock != null && countValidBlocks( ) > 0 )
        {
            float[ ] rotations = getRotations( currentblock.blockPos, currentblock.enumFacing );
            event.setYaw( rotations[ 0 ] );
            event.setPitch( rotations[ 1 ] );
        }
    } );

    // pasted from seppuku
    private boolean isOffsetBBEmpty(double x, double y, double z) {
        return mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(x, y, z)).isEmpty();
    }

    private void doSafeWalk( MoveEvent event )
    {
        double x = event.motionX;
        double y = event.motionY;
        double z = event.motionZ;

        if (mc.player.onGround && !mc.player.noClip) {
            double increment;
            for (increment = 0.05D; x != 0.0D && isOffsetBBEmpty(x, -2, 0.0D); ) {
                if (x < increment && x >= -increment) {
                    x = 0.0D;
                } else if (x > 0.0D) {
                    x -= increment;
                } else {
                    x += increment;
                }
            }
            for (; z != 0.0D && isOffsetBBEmpty(0.0D, -2, z); ) {
                if (z < increment && z >= -increment) {
                    z = 0.0D;
                } else if (z > 0.0D) {
                    z -= increment;
                } else {
                    z += increment;
                }
            }
            for (; x != 0.0D && z != 0.0D && isOffsetBBEmpty(x, -2, z); ) {
                if (x < increment && x >= -increment) {
                    x = 0.0D;
                } else if (x > 0.0D) {
                    x -= increment;
                } else {
                    x += increment;
                }
                if (z < increment && z >= -increment) {
                    z = 0.0D;
                } else if (z > 0.0D) {
                    z -= increment;
                } else {
                    z += increment;
                }
            }
        }

        event.motionX = x;
        event.motionY = y;
        event.motionZ = z;
    }

    @Handler
    public Listener< MoveEvent > onMove = new Listener< >( MoveEvent.class, event ->
    {
        if( nullCheck( ) ) return;

        if( safewalk )
            doSafeWalk( event );
    } );

    @Handler
    public Listener< MotionUpdateEvent > onMotionUpdate = new Listener< >( MotionUpdateEvent.class, event ->
    {
        if( nullCheck( ) ) return;

        block31: {
            BlockPos blockPos;
            Scaffold scaffold;
            int n;
            block37: {
                block36: {
                    block35: {
                        block34: {
                            block33: {
                                block30: {
                                    BlockPos blockPos2;
                                    block32: {
                                        block29: {
                                            block28: {
                                                block27: {
                                                    block26: {
                                                        if (this.countValidBlocks() <= 0) break block26;
                                                        if (Double.compare(mc.player.posY, 257.0) <= 0) break block27;
                                                    }
                                                    this.currentblock = null;
                                                    return;
                                                }
                                                if (this.countValidBlocks() <= 0) break block28;
                                                if (autoswap) break block29;
                                                if (mc.player.getHeldItemMainhand().getItem() instanceof ItemBlock) break block29;
                                            }
                                            return;
                                        }
                                        if( event.stage != 0 ) break block30;
                                        this.currentblock = null;
                                        if (mc.player.isSneaking()) break block31;
                                        int n2 = this.findBlockToPlace();
                                        if (n2 == -1) break block31;
                                        Item item = mc.player.inventory.getStackInSlot(n2).getItem();
                                        if (!(item instanceof ItemBlock)) break block31;
                                        Block block = ((ItemBlock)item).getBlock();
                                        boolean bl = block.getDefaultState().isFullBlock();
                                        double d = bl ? 1.0 : 0.01;
                                        blockPos2 = new BlockPos(mc.player.posX, mc.player.posY - d, mc.player.posZ);
                                        if (!mc.world.getBlockState(blockPos2).getMaterial().isReplaceable()) break block31;
                                        if (bl) break block32;
                                        if (!someblockcheck(n2)) break block31;
                                    }
                                    Scaffold scaffold2 = this;
                                    scaffold2.currentblock = this.checkNearBlocksExtended(blockPos2);
                                    if (scaffold2.currentblock != null) {
                                        if (this.rotate) {
                                            float[ ] rotations = getRotations( currentblock.blockPos, currentblock.enumFacing );
                                            event.rotationYaw = rotations[ 0 ];
                                            event.rotationPitch = rotations[ 1 ];
                                            return;
                                        }
                                    }
                                    break block31;
                                }
                                if (this.currentblock == null) break block31;
                                n = mc.player.inventory.currentItem;
                                if (!(mc.player.getHeldItemMainhand().getItem() instanceof ItemBlock)) break block33;
                                if (this.isBlockValid(((ItemBlock)mc.player.getHeldItemMainhand().getItem()).getBlock())) break block34;
                            }
                            if (autoswap) {
                                int n3 = this.findBlockToPlace();
                                if (n3 != -1) {
                                    mc.player.inventory.currentItem = n3;
                                    mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(mc.player.inventory.currentItem));
                                }
                            }
                        }
                        if (!mc.player.movementInput.jump) break block35;
                        if (mc.player.moveForward != 0.0f) break block35;
                        if (mc.player.moveStrafing != 0.0f) break block35;
                        if (!tower) break block35;
                        mc.player.setVelocity(0.0, 0.42, 0.0);
                        Object[] objectArray = new Object[1];
                        if (!timer.passed(1500)) break block36;
                        mc.player.motionY = -0.28;
                        Scaffold scaffold3 = this;
                        scaffold = scaffold3;
                        timer.reset();
                        break block37;
                    }
                    timer.reset();
                }
                scaffold = this;
            }
            BlockPos blockPos3 = blockPos = scaffold.currentblock.blockPos;
            boolean bl = mc.world.getBlockState(blockPos).getBlock().onBlockActivated((World)mc.world, blockPos3, mc.world.getBlockState(blockPos3), (EntityPlayer)mc.player, EnumHand.MAIN_HAND, EnumFacing.DOWN, 0.0f, 0.0f, 0.0f);
            if (bl) {
                mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)mc.player, CPacketEntityAction.Action.START_SNEAKING));
            }
            mc.playerController.processRightClickBlock(mc.player, mc.world, blockPos, this.currentblock.enumFacing, new Vec3d((double)blockPos.getX() + Math.random(), mc.world.getBlockState((BlockPos)blockPos).getSelectedBoundingBox((World)mc.world, (BlockPos)blockPos).maxY - 0.01, (double)blockPos.getZ() + Math.random()), EnumHand.MAIN_HAND);
            mc.player.swingArm(EnumHand.MAIN_HAND);
            if (bl) {
                mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            }
            mc.player.inventory.currentItem = n;
            mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(mc.player.inventory.currentItem));
        }
    } );

    public class BlockPosWithFacing
    {
        public BlockPos blockPos;
        public EnumFacing enumFacing;

        public BlockPosWithFacing( BlockPos blockPos, EnumFacing enumFacing )
        {
            this.blockPos = blockPos;
            this.enumFacing = enumFacing;
        }
    }
}
