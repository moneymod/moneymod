package wtf.moneymod.client.impl.command.impl;

import wtf.moneymod.client.impl.command.Command;
import wtf.moneymod.client.impl.utility.impl.world.ChatUtil;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;


public class CoordsCommand extends Command {

    public CoordsCommand() {
        super("coords", "coords");
    }

    @Override public void execute(String[] args) {
        if (nullCheck()) return;
        //shit code momentXYZ: 14 7 9
        ChatUtil.INSTANCE.sendMessage("coordinates has copied [" + String.format("XYZ: %s %s %s", (int)mc.player.posX, (int)mc.player.posY, (int)mc.player.posZ) + "]");
        StringSelection selection = new StringSelection(String.format("XYZ: %s %s %s", (int)mc.player.posX, (int)mc.player.posY, (int)mc.player.posZ));
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }
}
