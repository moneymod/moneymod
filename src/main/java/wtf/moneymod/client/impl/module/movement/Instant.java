package wtf.moneymod.client.impl.module.movement;

import net.minecraft.init.MobEffects;
import scala.reflect.internal.Phase;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.events.MoveEvent;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

import java.util.Objects;

@Module.Register(label = "Instant", cat = Module.Category.MOVEMENT)
public final class Instant extends Module {

    @Value("InLiquid") public boolean liquid = false;
    @Value("ShiftCheck") public boolean shift = false;

    @Handler public Listener<MoveEvent> moveListener = new Listener<>(MoveEvent.class, event -> {
        if ( nullCheck( ) ) return;
        if ( mc.player.isElytraFlying( ) ) return;
        if ( !liquid && ( mc.player.isInWater( ) || mc.player.isInLava( ) ) ) return;
        if (mc.player.isEntityInsideOpaqueBlock()) return;
        if (shift && mc.player.isSneaking()) return;
        PhaseWalk phase = ( PhaseWalk ) Main.getMain().getModuleManager().get(PhaseWalk.class);
        if (phase.isToggled()) {
            if (phase.movement == PhaseWalk.Movement.SHIFT) {
                if (mc.gameSettings.keyBindSneak.isKeyDown()) return;
            } else {
                return;
            }
        }
        if (mc.player.moveForward != 0.0 || mc.player.moveStrafing != 0.0) {
            float moveForward = mc.player.movementInput.moveForward;
            float moveStrafe  = mc.player.movementInput.moveStrafe;
            float rotationYaw = mc.player.prevRotationYaw
                    + (mc.player.rotationYaw - mc.player.prevRotationYaw)
                    * mc.getRenderPartialTicks();

            if (moveForward != 0.0f)
            {
                if (moveStrafe > 0.0f)
                {
                    rotationYaw += ((moveForward > 0.0f) ? -45 : 45);
                }
                else if (moveStrafe < 0.0f)
                {
                    rotationYaw += ((moveForward > 0.0f) ? 45 : -45);
                }
                moveStrafe = 0.0f;
                if (moveForward > 0.0f)
                {
                    moveForward = 1.0f;
                }
                else if (moveForward < 0.0f)
                {
                    moveForward = -1.0f;
                }
            }

            double speed = 0.2873;

            if (mc.player.isPotionActive(MobEffects.SPEED)) {
                int amplifier = Objects.requireNonNull(
                                mc.player.getActivePotionEffect(MobEffects.SPEED))
                        .getAmplifier();

                speed *= 1.0 + 0.2 * (amplifier + 1);
            }

            double posX =
                    moveForward * speed * -Math.sin(Math.toRadians(rotationYaw))
                            + moveStrafe * speed * Math.cos(Math.toRadians(rotationYaw));
            double posZ =
                    moveForward * speed * Math.cos(Math.toRadians(rotationYaw))
                            - moveStrafe * speed * -Math.sin(Math.toRadians(rotationYaw));

            event.motionX = posX;
            event.motionZ = posZ;

        } else {
            event.motionX = 0;
            event.motionZ = 0;
        }
    });

}
