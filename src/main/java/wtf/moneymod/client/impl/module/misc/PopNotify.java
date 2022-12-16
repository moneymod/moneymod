package wtf.moneymod.client.impl.module.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import wtf.moneymod.client.api.events.DisconnectEvent;
import wtf.moneymod.client.api.events.EntityDeathEvent;
import wtf.moneymod.client.api.events.TotemPopEvent;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.world.ChatUtil;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

import java.util.HashMap;
import java.util.Map;

@Module.Register( label = "PopNotify", desc = "Notifies you when somebody pops a totem", cat = Module.Category.MISC )
public class PopNotify extends Module
{
    @Value( "LocalPlayer" ) public boolean localplayer = true;

    private static final Map< String, Integer > list = new HashMap< >( );

    @Handler public Listener< TotemPopEvent > onTotemPop = new Listener< >( TotemPopEvent.class, event ->
    {
        if( !localplayer )
        {
            if (mc.getSession().getUsername().equalsIgnoreCase(event.getEntityPlayerSP().getName()))
                return;
        }

        if( !list.containsKey( event.getEntityPlayerSP( ).getName( ) ) )
            list.put( event.getEntityPlayerSP( ).getName( ), 1 );
        else
            list.put( event.getEntityPlayerSP( ).getName( ), list.get( event.getEntityPlayerSP( ).getName( ) ) + 1 );

        notifyPop( event.getEntityPlayerSP( ) );
    } );

    @Handler
    public Listener< EntityDeathEvent > onEntityDeath = new Listener< >( EntityDeathEvent.class, event ->
    {
        if( !( event.getEntity( ) instanceof EntityPlayer ) ) return;
        if( event.getEntity( ).equals( mc.player ) && !localplayer ) return;

        notifyDeath( ( EntityPlayer )event.getEntity( ) );
    } );

    @Handler
    public Listener< DisconnectEvent > onDisconnect = new Listener< >( DisconnectEvent.class, event ->
    {
        list.clear( );
    } );

    private void notifyPop( EntityPlayer player )
    {
        if( !list.containsKey( player.getName( ) ) ) return;

        int popcount = list.get( player.getName( ) );

        String str = ChatFormatting.DARK_GRAY + player.getName( ) +
                ChatFormatting.WHITE + " popped " + ChatFormatting.DARK_GRAY + popcount + ChatFormatting.WHITE + " totem(s)";

        if( player.getName( ).equals( mc.player.getName( ) ) )
            str += " (" + ChatFormatting.DARK_GRAY + getTotemsLeft( player ) + ChatFormatting.WHITE + " left)";

        ChatUtil.INSTANCE.sendMessageId( str, true, 9236 );
    }

    private void notifyDeath( EntityPlayer player )
    {
        int count = list.get( player.getName( ) );

        list.remove( player.getName( ) );

        ChatUtil.INSTANCE.sendMessageId( ChatFormatting.DARK_GRAY + player.getName( ) +
                ChatFormatting.WHITE + " died after popping " + ChatFormatting.DARK_GRAY + count + ChatFormatting.WHITE + " totem(s)", true, 9235 );
    }

    private int getTotemsLeft( EntityPlayer player )
    {
        int ret =
                player.inventory.mainInventory
                        .stream( )
                        .filter( item -> item.getItem( ).equals( Items.TOTEM_OF_UNDYING ) )
                        .mapToInt( ItemStack::getCount )
                        .sum( ) - 1;

        if( player.getHeldItemOffhand( ).getItem( ).equals( Items.TOTEM_OF_UNDYING ) )
            ret++;

        return Math.max( 0, ret );
    }
}
