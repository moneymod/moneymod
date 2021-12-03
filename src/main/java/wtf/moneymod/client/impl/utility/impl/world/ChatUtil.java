package wtf.moneymod.client.impl.utility.impl.world;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.impl.utility.Globals;

/**
 * @author cattyn
 * @since 11/02/21
 */

public enum ChatUtil implements Globals {
    INSTANCE;

    public String staticName = ChatFormatting.DARK_GREEN + "[" + ChatFormatting.GREEN + "$" + ChatFormatting.DARK_GREEN + "]";

    public void sendMessage(String text) {
        sendMsgEvent(staticName, text, false, 1);
    }

    public void sendInfoMessage(String text) {
        sendMsgEvent(staticName, ChatFormatting.GOLD + "[INFO] " + ChatFormatting.YELLOW + text, false, 1);
    }

    public void sendErrorMessage(String text) {
        sendMsgEvent(staticName, ChatFormatting.DARK_RED + "[ERROR] " + ChatFormatting.RED + text, false, 1);
    }

    public void sendMessage(String text, Boolean silent) {
        sendMsgEvent(staticName, text, silent, 1);
    }

    public void sendMessageId(String text, Boolean silent, int id) {
        sendMsgEvent(staticName, text, silent, id);
    }

    public void sendMsgEvent(String prefix, String text, boolean silent, int id) {
        if (mc.player == null) return;
        if (!silent) {
            mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(prefix + TextFormatting.GRAY + " " + text));
        } else {
            mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new TextComponentString(prefix + TextFormatting.GRAY + " " + text), id);
        }
    }

}
