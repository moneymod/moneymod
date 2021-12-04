package wtf.moneymod.client.mixin.mixins;


import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.events.*;
import wtf.moneymod.client.api.management.impl.RotationHandler;
import wtf.moneymod.client.impl.module.movement.ElytraFly;
import wtf.moneymod.client.impl.module.movement.Sprint;
import wtf.moneymod.client.mixin.mixins.ducks.AccessorEntityPlayerSP;

@Mixin( value = EntityPlayerSP.class, priority = 9999 )
public class MixinEntityPlayerSP extends AbstractClientPlayer {

    @Shadow protected Minecraft mc;

    @Shadow @Final public NetHandlerPlayClient connection;

    @Shadow public MovementInput movementInput;

    public MixinEntityPlayerSP(World worldIn, GameProfile playerProfile) {
        super(worldIn, playerProfile);
    }

    @Redirect( method = "onLivingUpdate", at = @At( value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;setSprinting(Z)V", ordinal = 2 ) )
    public void sprint(EntityPlayerSP playerSP, boolean sprinting) {
        playerSP.setSprinting(Main.getMain().getModuleManager().get(Sprint.class).isToggled() && (mc.player.moveForward != 0 || mc.player.moveStrafing != 0));
    }

    @Redirect( method = "move", at = @At( value = "INVOKE", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;move(Lnet/minecraft/entity/MoverType;DDD)V" ) )
    public void move(AbstractClientPlayer player, MoverType type, double x, double y, double z) {
        MoveEvent event = new MoveEvent(x, y, z);
        Main.EVENT_BUS.dispatch(event);
        super.move(type, event.motionX, event.motionY, event.motionZ);
    }

    @Inject( method = "onUpdateWalkingPlayer", at = @At( "HEAD" ), cancellable = true )
    public void pre(CallbackInfo info) {
        UpdateWalkingPlayerEvent event = new UpdateWalkingPlayerEvent(0);
        Main.EVENT_BUS.dispatch(event);
        if(RotationHandler.getInstance().isSet()) {
            info.cancel();
            if (this.isRiding()) {
                this.connection.sendPacket(new CPacketPlayer.Rotation(RotationHandler.getInstance().getRotation().getYaw(), RotationHandler.getInstance().getRotation().getPitch(), this.onGround));
                this.connection.sendPacket(new CPacketInput(this.moveStrafing, this.moveForward, this.movementInput.jump, this.movementInput.sneak));
                this.rotationYawHead = RotationHandler.getInstance().getRotation().getYaw();
                Entity entity = this.getLowestRidingEntity();

                if (entity != this && entity.canPassengerSteer()) {
                    this.connection.sendPacket(new CPacketVehicleMove(entity));
                }
            } else {
                doRotationShit(RotationHandler.getInstance().getRotation().getYaw(), RotationHandler.getInstance().getRotation().getPitch());
                this.rotationYawHead = RotationHandler.getInstance().getRotation().getYaw();
            }
            UpdateWalkingPlayerEvent event1 = new UpdateWalkingPlayerEvent(1);
            Main.EVENT_BUS.dispatch(event1);
        }
        RotationHandler.getInstance().reset();
    }

    @Inject( method = "onUpdate",
            at = @At( value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;onUpdateWalkingPlayer()V", shift = At.Shift.BEFORE ) )
    public void onPreMotionUpdate(CallbackInfo info) {
        MotionUpdateEvent event = new MotionUpdateEvent(Minecraft.getMinecraft().player.rotationYaw, Minecraft.getMinecraft().player.rotationPitch, Minecraft.getMinecraft().player.posX, Minecraft.getMinecraft().player.posY, Minecraft.getMinecraft().player.posZ, Minecraft.getMinecraft().player.onGround, Minecraft.getMinecraft().player.noClip, 0);
        Main.EVENT_BUS.dispatch(event);
    }

    @Redirect( method = "onLivingUpdate", at = @At( value = "INVOKE", target = "Lnet/minecraft/util/MovementInput;updatePlayerMoveState()V" ) )
    public void updatePlayerMoveState(MovementInput input) {
        input.updatePlayerMoveState();
        UpdatePlayerMoveStateEvent event = new UpdatePlayerMoveStateEvent(input);
        Main.EVENT_BUS.dispatch(event);
    }

    @Inject( method = "onUpdate",
            at = @At( value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;onUpdateWalkingPlayer()V", shift = At.Shift.AFTER ) )
    public void onPostMotionUpdate(CallbackInfo info) {
        MotionUpdateEvent event = new MotionUpdateEvent(Minecraft.getMinecraft().player.rotationYaw, Minecraft.getMinecraft().player.rotationPitch,
                Minecraft.getMinecraft().player.posX, Minecraft.getMinecraft().player.posY, Minecraft.getMinecraft().player.posZ, Minecraft.getMinecraft().player.onGround, Minecraft.getMinecraft().player.noClip, 1);
        Main.EVENT_BUS.dispatch(event);
    }

    @Inject( method = "onUpdateWalkingPlayer", at = @At( "RETURN" ) )
    public void post(CallbackInfo info) {
        UpdateWalkingPlayerEvent event = new UpdateWalkingPlayerEvent(1);
        Main.EVENT_BUS.dispatch(event);
    }

    @Inject( method = "onUpdate", at = @At( "HEAD" ) )
    public void onUpdatePre(CallbackInfo info) {
        PreUpdateEvent event = new PreUpdateEvent();
        Main.EVENT_BUS.dispatch(event);
    }

    @Override public boolean isElytraFlying() {
        try {
            if (Main.getMain().getModuleManager().get(ElytraFly.class).isToggled()) return false;
            return super.isElytraFlying();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    void doRotationShit(float yaw, float pitch) {
        boolean flag = mc.player.isSprinting();

        if (flag != ((AccessorEntityPlayerSP) mc.player).getServerSprintState()) {
            if (flag) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SPRINTING));
            } else {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
            }

            ((AccessorEntityPlayerSP) mc.player).setServerSprintState(flag);
        }

        boolean flag1 = mc.player.isSneaking();

        if (flag1 != ((AccessorEntityPlayerSP) mc.player).getServerSneakState()) {
            if (flag1) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
            } else {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            }

            (( AccessorEntityPlayerSP ) mc.player).setServerSneakState(flag1);
        }

        if (mc.player == mc.getRenderViewEntity()) {
            AxisAlignedBB axisalignedbb = mc.player.getEntityBoundingBox();
            double dX = mc.player.posX - ((AccessorEntityPlayerSP) mc.player).getLastReportedPosX();
            double dY = axisalignedbb.minY - ((AccessorEntityPlayerSP) mc.player).getLastReportedPosY();
            double dZ = mc.player.posZ - ((AccessorEntityPlayerSP) mc.player).getLastReportedPosZ();
            double dYaw = (yaw - ((AccessorEntityPlayerSP) mc.player).getLastReportedYaw());
            double dPitch = (pitch - ((AccessorEntityPlayerSP) mc.player).getLastReportedPitch());
            ((AccessorEntityPlayerSP) mc.player).setPositionUpdateTicks(((AccessorEntityPlayerSP) mc.player).getPositionUpdateTicks() + 1);
            boolean positionChanged = dX * dX + dY * dY + dZ * dZ > 9.0E-4D || ((AccessorEntityPlayerSP) mc.player).getPositionUpdateTicks() >= 20;
            boolean rotationChanged = dYaw != 0.0D || dPitch != 0.0D;

            if (mc.player.isRiding()) {
                mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.motionX, -999.0D, mc.player.motionZ, yaw, pitch, mc.player.onGround));
                positionChanged = false;
            } else if (positionChanged && rotationChanged) {
                mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX, axisalignedbb.minY, mc.player.posZ, yaw, pitch, mc.player.onGround));
            } else if (positionChanged) {
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, axisalignedbb.minY, mc.player.posZ, mc.player.onGround));
            } else if (rotationChanged) {
                mc.player.connection.sendPacket(new CPacketPlayer.Rotation(yaw, pitch, mc.player.onGround));
            } else if (((AccessorEntityPlayerSP) mc.player).getPrevOnGround() != mc.player.onGround) {
                mc.player.connection.sendPacket(new CPacketPlayer(mc.player.onGround));
            }

            if (positionChanged) {
                ((AccessorEntityPlayerSP) mc.player).setLastReportedPosX(mc.player.posX);
                ((AccessorEntityPlayerSP) mc.player).setLastReportedPosY(axisalignedbb.minY);
                ((AccessorEntityPlayerSP) mc.player).setLastReportedPosZ(mc.player.posZ);
                ((AccessorEntityPlayerSP) mc.player).setPositionUpdateTicks(0);
            }

            if (rotationChanged) {
                ((AccessorEntityPlayerSP) mc.player).setLastReportedYaw(yaw);
                ((AccessorEntityPlayerSP) mc.player).setLastReportedPitch(pitch);
            }

            ((AccessorEntityPlayerSP) mc.player).setPrevOnGround(mc.player.onGround);
            ((AccessorEntityPlayerSP) mc.player).setAutoJumpEnabled(mc.gameSettings.autoJump);
        }
    }

}
