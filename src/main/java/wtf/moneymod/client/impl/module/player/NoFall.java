package wtf.moneymod.client.impl.module.player;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import wtf.moneymod.client.api.events.PacketEvent;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.mixin.mixins.ducks.AccessorCPacketPlayer;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

@Module.Register( label = "NoFall", desc = "Removes fall damage", cat = Module.Category.PLAYER )
public class NoFall extends Module
{
    @Value( "Mode" ) public Mode mode = Mode.NORMAL;
    @Value( "Distance" ) @Bounds( min = 3, max = 30 ) public int distance = 4;

    public enum Mode
    {
        NORMAL, OLDFAG
    }

    @Override
    public void onTick( )
    {
        if( nullCheck( ) ) return;

        if( mode == Mode.OLDFAG )
        {
            Vec3d vec = new Vec3d(mc.player.lastTickPosX + (mc.player.posX - mc.player.lastTickPosX) * mc.getRenderPartialTicks(), mc.player.lastTickPosY + (mc.player.posY - mc.player.lastTickPosY) * mc.getRenderPartialTicks(), mc.player.lastTickPosZ + (mc.player.posZ - mc.player.lastTickPosZ) * mc.getRenderPartialTicks());
            BlockPos pos = new BlockPos(vec.x, vec.y - 1, vec.z);
            BlockPos[] posList = { pos.north(), pos.south(), pos.east(), pos.west(), pos.down() };
            for (BlockPos blockPos : posList)
            {
                Block block = mc.world.getBlockState(blockPos).getBlock();
                if (mc.player.fallDistance > 3 && block != Blocks.AIR) {
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, 0, mc.player.posZ, false));
                    mc.player.fallDistance = 0;
                }
            }
        }
    }

    @Handler
    public Listener< PacketEvent.Send > onPacketSend = new Listener< >( PacketEvent.Send.class, event ->
    {
        if( event.getPacket( ) instanceof CPacketPlayer )
        {
            if( mode == Mode.NORMAL )
            {
                if( mc.player.fallDistance > distance )
                    ( ( AccessorCPacketPlayer )event.getPacket( ) ).setOnGround( true );
            }
        }
    } );
}
