package wtf.moneymod.client;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import org.apache.commons.codec.digest.DigestUtils;


public class DiscordMod {

    private static final DiscordRichPresence discordRichPresence = new DiscordRichPresence();
    private static final DiscordRPC discordRPC = DiscordRPC.INSTANCE;

    public static void start() {
        DiscordEventHandlers eventHandlers = new DiscordEventHandlers();
        eventHandlers.disconnected = ((var1, var2) -> System.out.println("Discord RPC disconnected, var1: " + var1 + ", var2: " + var2));
        String discordID = "913836268222312488";
        discordRPC.Discord_Initialize(discordID, eventHandlers, true, null);
        discordRichPresence.startTimestamp = System.currentTimeMillis() / 1000L;
        discordRichPresence.largeImageKey = "sonyakot";
        discordRichPresence.largeImageText = "fanboy club";
        discordRichPresence.details = "version: ++";
        discordRichPresence.state = "$gang";
        discordRichPresence.partyId = "ae488379-351d-4a4f-ad32-2b9b01c91657";
        discordRichPresence.joinSecret = "MTI4NzM0OjFpMmhuZToxMjMxMjM= ";

        discordRPC.Discord_UpdatePresence(discordRichPresence);
    }

    public static void stop() {
        discordRPC.Discord_Shutdown();
        discordRPC.Discord_ClearPresence();
    }
}