package wtf.moneymod.client.impl.utility.impl.cape;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.impl.module.global.Global;
import wtf.moneymod.client.impl.utility.Globals;
//import wtf.cattyn.moneymod.Main;
//import wtf.cattyn.moneymod.client.module.client.Capes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

//import static wtf.cattyn.moneymod.util.Globals.mc;

public class CapeThread implements Runnable, Globals {
    private boolean print = true;
    private int usercount = 1;
    private String online = "unknown";
    private List<CapeUser> users = new ArrayList<>();
    private Socket socket = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;
    private String code = null;

    public CapeThread() {
        super();

        try {
            // eat shit if youre not using windows
            File file = new File(System.getenv("LOCALAPPDATA") + "\\moneymod+3\\chatcode.txt");
            if (file.exists())
                code = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        } catch (Exception e) {

        }
    }

    @Override
    public void run() {
        Thread hsthread = null;

        while (!Thread.currentThread().isInterrupted()) {
            hsthread = null;
            socket = null;
            in = null;
            out = null;

            try {
                socket = new Socket("185.240.103.107", 1631);
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());

                out.writeUTF("ktidrjtdmm+3/capes/hello/jtejgd09i45fjg0ht");
                out.flush();

                hsthread = new Thread(() -> requestDataThread(out));
                hsthread.start();

                while (!socket.isClosed()) {
                    if (print) {
                        print = false;
                        System.out.println("[CapeThread] Connected");
                    }

                    String str = in.readUTF();

                    if (str.equals("mm+3/capes/update")) {
                        StringBuilder send = new StringBuilder("mm+3/capes/post/");
                        send.append(mc.getSession().getProfile().getId().toString());
                        send.append("/");

                        Global mod = (Global) Main.getMain().getModuleManager().get(Global.class);
                        if (!mod.isToggled())
                            send.append("none");
                        else {
                            CapeEnum cape = mod.getCape();
                            if (cape == null) {
                                System.out.println(String.format("Unknown cape: %s", mod.mode));
                                send.append("none");
                            } else
                                send.append(cape.getCapeName());
                        }

                        send.append("/");
                        send.append(mc.getSession().getUsername());
                        send.append("/");
                        if (code != null && code.length() > 2)
                            send.append(code);
                        else
                            send.append("nocode");

                        out.writeUTF(send.toString());
                    } else if (str.startsWith("mm+3/capes/reply/")) {
                        String[] split = str.split("/");
                        if (split.length != 5) {
                            // size mismatch
                        } else {
                            int users = Integer.parseInt(split[3]);
                            this.usercount = users;

                            String m = String.join("/", Arrays.copyOfRange(split, 4, split.length));

                            String b64decode = new String(
                                    Base64.getDecoder().decode(m.getBytes(StandardCharsets.UTF_8)),
                                    StandardCharsets.UTF_8);

                            String[] newsplit = b64decode.split("\n");
                            if (users != newsplit.length) {
                                // user count mismatch wtf ??
                            }

                            this.users.clear();
                            for (String capeuser : newsplit) {
                                String[] capesplit = capeuser.split("/");

                                CapeUser user = new CapeUser(capesplit[0], capesplit[1]);

                                this.users.add(user);
                            }
                        }
                    } else if (str.startsWith("mm+3/chat/receive/")) {
                        try {
                            String[] split = str.split("/");

                            String name = split[3];
                            String msg = String.join("/", Arrays.copyOfRange(split, 4, split.length));

                            msg = new String(Base64.getDecoder().decode(
                                    msg.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);

                            if (msg.charAt(0) == '>')
                                msg = ChatFormatting.GREEN + msg;

                            if (msg.charAt(0) == '<')
                                msg = ChatFormatting.BLUE + msg;

                            mc.ingameGUI.getChatGUI().printChatMessage(
                                    new TextComponentString(ChatFormatting.DARK_GREEN + "[" + ChatFormatting.GREEN + "$IRC" +
                                            ChatFormatting.DARK_GREEN + "] " + ChatFormatting.RESET + "<" + name + "> " + msg));

                            Main.getMain().getIrcScreen().getMessages().add(0, new TextComponentString("<" + name + "> " + msg));
                        } catch (Exception e2) {

                        }
                    } else if (str.startsWith("mm+3/chat/online/")) {
                        try {
                            String[] split = str.split("/");
                            String info = String.join("/", Arrays.copyOfRange(split, 3, split.length));
                            info = new String(Base64.getDecoder().decode(
                                    info.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);

                            String[] newsplit = info.split("\n");
                            int count = newsplit.length;

                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < count; i++) {
                                sb.append(newsplit[i]);
                                if ((i + 1) != count)
                                    sb.append(", ");
                            }

                            online = sb.toString();
                        } catch (Exception e2) {

                        }
                    }
                }

                if (hsthread != null)
                    hsthread.interrupt();
            } catch (Exception e) {
                //if( !( e instanceof ConnectException ) && !( e instanceof EOFException ) )
                //    e.printStackTrace( );

                if (hsthread != null)
                    hsthread.interrupt();

                if (socket != null && !socket.isClosed()) {
                    try {
                        socket.close();
                    } catch (Exception e2) {

                    }
                }
            }

            // reconnect in 10 seconds
            try {
                Thread.sleep(10000);
            } catch (Exception e) {

            }
        }
    }

    public static void requestDataThread(DataOutputStream out) {
        try {
            Thread.sleep(4000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (!Thread.currentThread().isInterrupted()) {
            try {
                out.writeUTF("mm+3/capes/get");
                out.writeUTF("mm+3/chat/requestonline");
                Thread.sleep(5000);
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public int getUserCount() {
        return usercount;
    }

    public List<CapeUser> getUsers() {
        return users;
    }

    public boolean isCapePresent(String uuid) {
        Global mod = (Global) Main.getMain().getModuleManager().get(Global.class);
        if (!mod.isToggled()) return false;

        for (CapeUser capeuser : users) {
            if (capeuser.getUUID().equalsIgnoreCase(uuid)) {
                if (!capeuser.getCape().equals("none"))
                    return true;

                break;
            }
        }

        return false;
    }

    public ResourceLocation getCapeLocation(String uuid) {
        for (CapeUser capeuser : users) {
            if (capeuser.getUUID().equalsIgnoreCase(uuid)) {
                for (CapeEnum c : CapeEnum.values()) {
                    if (c.getCapeName().equalsIgnoreCase(capeuser.getCape()))
                        return c.getResourceLocation();
                }

                break;
            }
        }

        return null;
    }

    public void sendChatMessage(String msg) {
        try {
            if (out != null) {
                String name = mc.getSession().getUsername();
                String b64 = Base64.getEncoder().encodeToString(msg.getBytes(StandardCharsets.UTF_8));

                StringBuilder sb = new StringBuilder("mm+3/chat/send/");
                sb.append(name);
                sb.append("/");
                sb.append(b64);

                out.writeUTF(sb.toString());
                out.flush();
            }
        } catch (Exception e) {

        }
    }

    public void sendOnlineRequest() {
        try {
            if (out != null) {
                out.writeUTF("mm+3/chat/requestonline");
                out.flush();
            }
        } catch (Exception e) {

        }
    }

    public String getOnline() {
        return online;
    }
}
