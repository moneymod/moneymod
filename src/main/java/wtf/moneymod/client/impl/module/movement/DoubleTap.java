package wtf.moneymod.client.impl.module.movement;

import net.minecraft.entity.MoverType;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import wtf.moneymod.client.api.events.PacketEvent;
import wtf.moneymod.client.api.events.UpdateWalkingPlayerEvent;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.world.EntityUtil;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

@Module.Register( label = "DoubleTap", cat = Module.Category.MOVEMENT)
public class DoubleTap extends Module {

    @Value(value = "Power") @Bounds(min = 0.1f, max = 8) public float speed = 0.2F;


    @Override
    public void onTick() {
        if (nullCheck() || !EntityUtil.INSTANCE.isMoving(mc.player)){
            setToggled(false);
            return;
        }
        double power[] = EntityUtil.forward(speed / 2);
        mc.player.move(MoverType.PLAYER, power[0], 0, power[1]);
        mc.player.connection.sendPacket((Packet) new CPacketPlayer.Position(mc.player.posX,mc.player.posY,mc.player.posZ, true));
        setToggled(false);
    }
}

