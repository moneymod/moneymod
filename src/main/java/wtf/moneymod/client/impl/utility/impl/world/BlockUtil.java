package wtf.moneymod.client.impl.utility.impl.world;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import wtf.moneymod.client.impl.utility.Globals;

public class BlockUtil implements Globals {

    private static boolean unshift = false;
    //yrodi fixite formating

    public static boolean placeBlock (BlockPos pos) {
        Block block = mc.world.getBlockState( pos ).getBlock( );
        EnumFacing direction = BlockUtil.calcSide( pos );
        if ( direction == null ) {
            return false;
        }
        boolean activated = block.onBlockActivated( mc.world, pos, mc.world.getBlockState( pos ), mc.player, EnumHand.MAIN_HAND, direction, 0.0f, 0.0f, 0.0f );
        if ( activated ) {
            mc.player.connection.sendPacket( new CPacketEntityAction( mc.player, CPacketEntityAction.Action.START_SNEAKING ) );
        }
        mc.player.connection.sendPacket( new CPacketPlayerTryUseItemOnBlock( pos.offset( direction ), direction.getOpposite( ), EnumHand.MAIN_HAND, 0.5f, 0.5f, 0.5f ) );
        mc.player.connection.sendPacket( new CPacketAnimation( EnumHand.MAIN_HAND ) );
        if ( activated || unshift ) {
            mc.player.connection.sendPacket( new CPacketEntityAction( mc.player, CPacketEntityAction.Action.STOP_SNEAKING ) );
            unshift = false;
        }
        mc.playerController.updateController( );
        return true;
    }
    public static EnumFacing calcSide ( BlockPos pos ) {
        for ( EnumFacing side : EnumFacing.values( ) ) {
            IBlockState offsetState = mc.world.getBlockState( pos.offset( side ) );
            boolean activated = offsetState.getBlock( ).onBlockActivated( mc.world, pos, offsetState, mc.player, EnumHand.MAIN_HAND, side, 0.0f, 0.0f, 0.0f );
            if ( activated ) {
                mc.getConnection( ).sendPacket( new CPacketEntityAction( mc.player, CPacketEntityAction.Action.START_SNEAKING ) );
                unshift = true;
            }
            if ( !offsetState.getBlock( ).canCollideCheck( offsetState, false ) || offsetState.getMaterial( ).isReplaceable( ) )
                continue;
            return side;
        }
        return null;
    }



}
