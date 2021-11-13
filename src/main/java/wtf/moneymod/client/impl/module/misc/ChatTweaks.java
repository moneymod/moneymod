package wtf.moneymod.client.impl.module.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.text.TextComponentString;
import wtf.moneymod.client.api.events.PacketEvent;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.module.global.Global;
import wtf.moneymod.client.mixin.mixins.ducks.AccessorSPacketChat;
import wtf.moneymod.client.mixin.mixins.ducks.ICPacketChatMessage;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

import java.util.Date;

@Module.Register( label = "ChatTweaks", cat = Module.Category.MISC, exception = true )
public class ChatTweaks extends Module {

    private static ChatTweaks INSTANCE;

    public ChatTweaks() {
        INSTANCE = this;
    }

    public static ChatTweaks getInstance() {
        return INSTANCE;
    }

    @Value(value = "Prefix") public boolean prefix = false;
    @Value( value = "Chat Animation" ) public boolean chatAnim = true;
    @Value(value = "Time") public boolean time = false;
    @Value(value = "Mode") public Mode mode = Mode.NORMAL;
    @Handler
    public Listener<PacketEvent.Receive> packetEventReceive = new Listener<>(PacketEvent.Receive.class, e -> {
        if ( this.time && e.getPacket( ) instanceof SPacketChat) {
            SPacketChat packet = e.getPacket( );
            Date date = new Date( );
            AccessorSPacketChat chatPacket = ( AccessorSPacketChat ) e.getPacket( );
            boolean add = false;
            if ( date.getMinutes( ) <= 9 ) add = true;
            String time = "<" + date.getHours( ) + ":" + ( add ? "0" : "" ) + date.getMinutes( ) + "> ";
            chatPacket.setChatComponent( new TextComponentString( ChatFormatting.DARK_PURPLE + time + ChatFormatting.RESET + packet.getChatComponent( ).getFormattedText( ) ) );
        }
    });

    @Handler
    public Listener<PacketEvent.Send> packetEventSend = new Listener<>(PacketEvent.Send.class, e -> {
        if (e.getPacket() instanceof CPacketChatMessage) {
            CPacketChatMessage packet = (CPacketChatMessage) e.getPacket();
            String prefix = "";
            switch (mode){
                case NORMAL:
                    prefix = "moneymod";
                    break;
                case FONT:
                    prefix = "\u1d0d\u1d0f\u0274\u1d07\u028f\u1d0d\u1d0f\u1d05";
                    break;
                case CARTI:
                    prefix = "m0n3ym0d";
                    break;
                case DENGIMOD:
                    prefix = "dengimodification.cc";
                    break;
            }
            if ( packet.getMessage().startsWith("/") || packet.getMessage().startsWith("$")) return;
            ((ICPacketChatMessage) e.getPacket()).setMessage( packet.getMessage() + " | " + prefix);
        }
    });

    private enum Mode{NORMAL, FONT, CARTI, DENGIMOD}

}
