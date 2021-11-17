package wtf.moneymod.client.impl.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.impl.command.Command;
import wtf.moneymod.client.impl.utility.impl.world.ChatUtil;

import java.awt.*;
import java.util.Arrays;

public class FontCommand extends Command {

    public FontCommand() {
        super("font <FontName>", "font");
    }

    @Override public void execute(String[] args) {
        if(args.length < 2) return;
        String fontName = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        try {
            Main.getMain().getFontRenderer().setFont(new Font(fontName, Font.PLAIN, 18));
            ChatUtil.INSTANCE.sendMessage(ChatFormatting.YELLOW + fontName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
