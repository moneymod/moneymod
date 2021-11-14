package wtf.moneymod.client.impl.module.movement;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.MovementInput;
import wtf.moneymod.client.api.events.PacketEvent;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.math.MathUtil;
import wtf.moneymod.client.impl.utility.impl.world.ChatUtil;
import wtf.moneymod.client.mixin.mixins.ducks.AccessorCPacketPlayer;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

@Module.Register( label = "ElytraFly", cat = Module.Category.MOVEMENT, desc = "GodLike elytrafly for oldfag.org (from plutonium)" )
public class ElytraFly extends Module {

    @Value( "Accelerate" ) public boolean accelerate = true;
    @Value( "Accel Speed" ) @Bounds( min = 0.1f, max = 1 ) public double speed = 0.1d;
    @Value( "Accel Min" ) @Bounds( min = 0.1f, max = 2 ) public double min = 0.1d;
    @Value( "Accel Max" ) @Bounds( min = 0.5f, max = 3.5f ) public double max = 2.3d;
    @Value( "Normal Speed" ) @Bounds( min = 0.5f, max = 3.5f ) public double normal = 2.3d;
    @Value( "LagBack" ) public boolean lagback = true;
    @Value( "LagBack Debug" ) public boolean debug = true;
    @Value( "AntiKick" ) public boolean antiKick = true;
    @Value( "OnGround" ) public boolean onGround = false;

    private double acceleratedSpeed;
    private double lastLagBack;
    private boolean allowup = true;

    public void onDisable() {
        if (mc.player != null)
            mc.player.capabilities.isFlying = false;
    }

    public double getSpeed() {
        return accelerate ? acceleratedSpeed : normal;
    }

    public boolean elytraCheck() {
        return mc.player != null && mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == Items.ELYTRA;
    }

    public void updateAcceleratedSpeed(boolean notMoving) {
        if (accelerate) {
            if (notMoving)
                acceleratedSpeed = min;

            if (acceleratedSpeed + speed >= max)
                acceleratedSpeed = max;
            else
                acceleratedSpeed += speed / 5;
        } else {
            acceleratedSpeed = calcSpeed();
        }
    }

    private double calcSpeed() {
        return MathUtil.INSTANCE.square(mc.player.motionX) + MathUtil.INSTANCE.square(mc.player.motionY) + MathUtil.INSTANCE.square(mc.player.motionZ);
    }

    public void onEnable() {
        if (!nullCheck())
            acceleratedSpeed = calcSpeed();
        else
            acceleratedSpeed = 0;
    }

    @Override public void onTick() {
        if (nullCheck() || !elytraCheck()) return;
        if (lagback && (System.currentTimeMillis() - lastLagBack) < 1200) return;

        updateAcceleratedSpeed(mc.player.movementInput.moveForward == 0.0F && mc.player.movementInput.moveStrafe == 0.0F);

        if (mc.player.onGround)
            allowup = true;

        if (!onGround && mc.player.onGround) return;

        if (allowup && !mc.player.onGround)
            allowup = false;
        else {
            mc.player.motionY = 0;
            if (antiKick && !mc.player.onGround && mc.player.ticksExisted % 4 == 0)
                mc.player.motionY = -0.000001f;
        }

        MovementInput input = mc.player.movementInput;
        if (input.moveForward != 0.f || input.moveStrafe != 0.f || input.jump || input.sneak) {
            float forward = input.moveForward;
            float strafe = input.moveStrafe;
            float yaw = mc.player.rotationYaw;

            if (forward != 0) {
                if (strafe > 0.0f)
                    yaw += ((forward > 0.0f) ? -45 : 45);
                else if (strafe < 0.0f)
                    yaw += ((forward > 0.0f) ? 45 : -45);

                strafe = 0.0f;
                if (forward > 0.0f)
                    forward = 1;
                else if (forward < 0.0f)
                    forward = -1;
            }
            if (strafe > 0.0f)
                strafe = 1;
            else if (strafe < 0)
                strafe = -1;

            if (input.sneak)
                mc.player.motionY = -(getSpeed() / 10);
            final double cos = Math.cos(Math.toRadians((yaw + 90.0f)));
            mc.player.motionX = forward * getSpeed() * cos + strafe * getSpeed() * Math.sin(Math.toRadians((yaw + 90)));
            mc.player.motionZ = forward * getSpeed() * Math.sin(Math.toRadians((yaw + 90.0f))) - strafe * getSpeed() * cos;
        } else {
            mc.player.motionX = 0;
            mc.player.motionZ = 0;
        }

        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
    }

    @Handler public Listener<PacketEvent.Receive> receiveListener = new Listener<>(PacketEvent.Receive.class, event -> {
        if (event.getPacket() instanceof SPacketPlayerPosLook && elytraCheck()) {
            allowup = true;
            if (lagback) {
                acceleratedSpeed = 0;
                if (debug) {
                    ChatUtil.INSTANCE.sendMessage("[" + ChatFormatting.YELLOW + getLabel() + ChatFormatting.GRAY +
                            "] Lagback detected", true);
                }
                lastLagBack = System.currentTimeMillis();
            }
        }
    });

    @Handler public Listener<PacketEvent.Send> sendListener = new Listener<>(PacketEvent.Send.class, event -> {
        if (event.getPacket() instanceof CPacketPlayer.Rotation || event.getPacket() instanceof CPacketPlayer.PositionRotation) {
            if (elytraCheck()) {
                CPacketPlayer packet = event.getPacket();
                (( AccessorCPacketPlayer ) packet).setPitch(0.5f);
            }
        }
    });

}
