package wtf.moneymod.client.impl.module.render;

import net.minecraft.network.play.server.SPacketTimeUpdate;
import wtf.moneymod.client.api.events.PacketEvent;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.render.JColor;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

@Module.Register( label = "Chams", cat = Module.Category.RENDER )
public class Chams extends Module {

    //CRYSTALS
    @Value(value = "Crystal") public boolean crystalChams = false;
    @Value(value = "Cancel X") public boolean cancelX = false;
    @Value(value = "Cancel Y") public boolean cancelY = false;
    @Value(value = "Glint") public boolean crystalGlint = false;
    @Value(value = "Model") public boolean crystalModel = false;
    @Value(value = "Line") public boolean crystalLine = false;
    @Value(value = "Scale") @Bounds(max = 1) public double crystalScale = 0.5D;
    @Value(value = "LineWidht") @Bounds(max = 3) public double crystalLineWidht = 0.5D;
    @Value(value = "Color" ) public JColor crystalColor = new JColor(255, 0, 0,180, false);
    @Value(value = "Glint Color" ) public JColor crystalColorGlint = new JColor(255, 0, 0,180, false);


    //PLAYERS
    @Value(value = "Players") public boolean playersChams = false;
    @Value(value = "Glint") public boolean playerGlint = false;
    @Value(value = "Model") public boolean playerModel = false;
    @Value(value = "Line") public boolean playerLine = false;
    @Value(value = "LineWidht") @Bounds(max = 3) public double playerLineWidht = 0.5D;
    @Value(value = "Color" ) public JColor playerColor = new JColor(255, 0, 0,180, false);
    @Value(value = "Glint Color" ) public JColor playerColorGlint = new JColor(255, 0, 0,180, false);

}
