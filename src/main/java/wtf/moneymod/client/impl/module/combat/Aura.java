package wtf.moneymod.client.impl.module.combat;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.events.PacketEvent;
import wtf.moneymod.client.api.management.impl.FriendManagement;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.player.ItemUtil;
import wtf.moneymod.client.impl.utility.impl.render.JColor;
import wtf.moneymod.client.impl.utility.impl.render.Renderer3D;
import wtf.moneymod.client.mixin.mixins.ducks.AccessorCPacketPlayer;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

import java.util.Comparator;
import java.util.function.Predicate;

@Module.Register( label = "Knife-Bot", cat = Module.Category.COMBAT )
public class Aura extends Module {

    @Value( value = "Mode" ) public Mode mode = Mode.SWITCH;
    @Value( value = "Range" ) @Bounds( max = 6 ) public double range = 4.2;
    @Value( value = "Render" ) public boolean render = false;
    @Value( value = "Rotate" ) public boolean rotate = false;
    @Value( value = "Color" ) public JColor color = new JColor(255, 255, 255, 120, false);

    float yaw = 0f, pitch = 0f;
    boolean rotating;
    Entity target;

    @Override protected void onEnable() {
        target = null;
    }

    @Override public void onTick() {
        if (nullCheck()) return;
        target = findTarget(EntityPlayer.class::isInstance);
        if (target != null) {
            switch (mode) {
                case NONE:
                    break;
                case SWITCH:
                    ItemUtil.getHotbarItems().keySet()
                            .stream()
                            .filter(e -> e.getItem() instanceof ItemSword)
                            .max(Comparator.comparing(e -> (( ItemSword ) e.getItem()).getAttackDamage() + EnchantmentHelper.getModifierForCreature(e, EnumCreatureAttribute.UNDEFINED)))
                            .ifPresent(bestSword -> ItemUtil.swapToHotbarSlot(ItemUtil.getHotbarItems().get(bestSword),false));
                    break;
                case ONLY:
                    if (!(mc.player.getHeldItemMainhand().getItem() instanceof ItemSword)) return;
                    break;
            }
            attack();
        } else {
            rotating = false;
        }
    }

    @Override public void onRender3D(float partialTicks) {
        if (target != null && render) {
            if (mode == Mode.ONLY) {
                if (!(mc.player.getHeldItemMainhand().getItem() instanceof ItemSword)) return;
            }
            Renderer3D.drawBoxESP(new AxisAlignedBB(target.posX - 0.3f, target.posY, target.posZ - 0.3f, target.posX + 0.3f, target.posY + 1.9f, target.posZ + 0.3f), color.getColor(), 1f, true, true, color.getColor().getAlpha(), 255);
        }
    }

    private Entity findTarget(Predicate<Entity> predicate) {
        return mc.world.loadedEntityList.stream()
                .filter(e -> !FriendManagement.getInstance().is(e.getName()) && e != mc.player && mc.player.getPositionVector().add(0, mc.player.eyeHeight, 0).distanceTo(e.getPositionVector().add(0, e.height / 2d, 0)) <= range)
                .filter(predicate)
                .min(Comparator.comparing(e -> mc.player.getDistanceSq(e)))
                .orElse(null);
    }

    private void attack() {
        if (mc.player.getCooledAttackStrength(0) >= 1f) {

            if ( rotate ) {
                rotating = true;
                yaw = Main.getMain().getRotationManagement().look(target, false, false)[0];
                pitch = Main.getMain().getRotationManagement().look(target, false, false)[1];
            }
            mc.playerController.attackEntity(mc.player, target);
            mc.player.swingArm(EnumHand.MAIN_HAND);
            mc.player.resetCooldown();
        }
    }

    @Handler
    public Listener<PacketEvent.Send> packetEventSend = new Listener<>(PacketEvent.Send.class, e -> {
        if ( e.getPacket( ) instanceof CPacketPlayer && rotating && rotate) {
            ( ( AccessorCPacketPlayer ) e.getPacket( ) ).setYaw( yaw );
            ( ( AccessorCPacketPlayer ) e.getPacket( ) ).setPitch( pitch );
        }
    });

    public enum Mode {
        NONE,
        ONLY,
        SWITCH
    }

}
