package wtf.moneymod.client.impl.module.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketChat;
import wtf.moneymod.client.api.events.PacketEvent;
import wtf.moneymod.client.api.management.impl.FriendManagement;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.misc.Timer;
import wtf.moneymod.client.impl.utility.impl.world.ChatUtil;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

import java.util.Random;

@Module.Register( label = "AutoEz", desc = "Types ez in the chat after kill", cat = Module.Category.MISC )
public class AutoEz extends Module
{
    private static EntityPlayer target;
    String[] message = new String[]{ "Wow he died so fast lmfao", "rekt", "LOL AHAHHA NICE IQ", "THE STATE", "MONEYMODLESS AHAHAHAHAH", "Iq issue retard" };
    String ezMessage = "Good Game! My Dick is Stuck In Car Exhaust Pipe It Hurts. Thanks To MoneyMod+3";
    int kilStreak;
    public static void setTarget( EntityPlayer player ) { target = player; }
    @Value( "Helping!!!" ) public boolean prikol = true;
    @Value( "KillStreak" ) public boolean killStreak = true;
    @Value( "RandomHack" ) public boolean randomHack = false;
    @Value( "Helping Delay" ) @Bounds( min = 0.1f, max = 10f ) public float pDelay = 1.f;
    private Timer timer = new Timer( );

    String[] randomHackMessage = new String[]{
            "is a noob hahaha fobus on tope",
            "Good fight! Konas owns me and all",
            "I guess konas ca is too fast for you",
            "you just got nae nae'd by konas",
            "I was AFK!",
            "you just got nae nae'd by wurst+",
    };


    @Override protected void onEnable( ) {
        timer.reset( );
        kilStreak = 0;
    }
    @Override protected void onDisable( ) {
        timer.reset( );
        kilStreak = 0;
    }

    @Override public void onTick( ) {
        if (target != null && mc.player.getDistanceSq(target) < 150) {
            if (target.isDead || target.getHealth() <= 0) {
                mc.player.sendChatMessage((!randomHack ? "> " + target.getName() + " " + ezMessage + " [$]" :  randomHackMessage[ new Random( ).nextInt( randomHackMessage.length ) ] ));
                kilStreak+=1;
                if (killStreak)
                    ChatUtil.INSTANCE.sendMessage(String.format("%s[%sKillStreak%s] %s kills!", ChatFormatting.YELLOW, ChatFormatting.GOLD,ChatFormatting.YELLOW, kilStreak));
            }
            target = null;
        }
    }

    @Handler
    public Listener< PacketEvent.Receive > onReceivePacket = new Listener< >( PacketEvent.Receive.class, event ->
    {
        if ( event.getPacket( ) instanceof SPacketChat ) {
            String text;
            final SPacketChat packet = (SPacketChat) event.getPacket( );
            text = packet.getChatComponent( ).getFormattedText( );
            if ( prikol ) {
                for ( String name : FriendManagement.getInstance( ) ) {
                    if ( text.contains( String.format( "<%s>", name ) ) && text.toLowerCase( ).contains( "[$]" ) ) {
                        if ( timer.passed( ( long ) ( pDelay * 1000L ) ) ) {
                            mc.player.sendChatMessage( "> " + message[ new Random( ).nextInt( message.length ) ] );
                            timer.reset( );
                            break;
                        }
                    }
                }
            }
        }
    } );
}
