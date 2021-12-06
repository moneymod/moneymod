package wtf.moneymod.client.impl.module.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import wtf.moneymod.client.api.events.PacketEvent;
import wtf.moneymod.client.api.events.UpdateWalkingPlayerEvent;
import wtf.moneymod.client.api.management.impl.RotationHandler;
import wtf.moneymod.client.api.management.impl.RotationManagement;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.math.Rotation;
import wtf.moneymod.client.impl.utility.impl.misc.Timer;
import wtf.moneymod.client.impl.utility.impl.player.ItemUtil;
import wtf.moneymod.client.impl.utility.impl.render.JColor;
import wtf.moneymod.client.impl.utility.impl.render.Renderer3D;
import wtf.moneymod.client.impl.utility.impl.world.BlockUtil;
import wtf.moneymod.client.impl.utility.impl.world.EntityUtil;
import wtf.moneymod.client.mixin.mixins.ducks.AccessorCPacketUseEntity;
import wtf.moneymod.client.mixin.mixins.ducks.AccessorEntityPlayerSP;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Module.Register( label = "AutoCrystalRewrite", cat = Module.Category.COMBAT )
public class AutoCrystalRewrite extends Module {

    //TODO: AntiCity (aka antisurround)
    //TODO: Break Check
    //TODO: Пофиксить рендер в са, Рендерится даже когда не держись кристаллы в руках
    //TODO: Пофиксить плейсмент (СА Плейсит когда я даже не держу кристаллы в руках)


    //global
    @Value( "Place" ) public boolean place = true;
    @Value( "Break" ) public boolean hit = true;
    @Value( "Break Calc" ) public boolean hitCalc = true;
    @Value( "Break Mode" ) public BreakMode hitMode = BreakMode.ALWAYS;
    @Value( "Instant" ) public boolean instant = true;
    @Value( "Inhibit" ) public boolean inhibit = true;
    @Value( "Place Delay" ) @Bounds( max = 200 ) public int placeDelay = 20;
    @Value( "Break Delay" ) @Bounds( max = 200 ) public int breakDelay = 40;
    @Value( "Swap" ) public SwapMode swap = SwapMode.NONE;
    @Value( "Packet Swing" ) public boolean packetSwing = false;
    @Value( "Swing" ) public SwingMode swing = SwingMode.MAINHAND;
    @Value( "AntiWeakness" ) public AntiWeakness antiWeakness = AntiWeakness.NONE;
    //ranges
    @Value( "Target Range" ) @Bounds( min = 2, max = 15 ) public float targetRange = 12;
    @Value( "Place Range" ) @Bounds( min = 1, max = 6 ) public float placeRange = 5.5f;
    @Value( "Break Range" ) @Bounds( min = 1, max = 6 ) public float hitRange = 5.5f;
    @Value( "Wall Place Range" ) @Bounds( min = 1, max = 6 ) public float wallPlaceRange = 3.5f;
    @Value( "Wall Break Range" ) @Bounds( min = 1, max = 6 ) public float wallBreakRange = 3.5f;
    @Value( "Ray Trace" ) public RayTrace rayTrace = RayTrace.NONE;
    //place
    @Value( "1.13" ) public boolean ecme = false;
    @Value( "Break Check" ) public boolean hitCheck = false;
    @Value( "Terrain Ignore" ) public boolean terrainIgnore = true;
    @Value( "Multi Place" ) public boolean multiplace = false;
    @Value( "Synchronize" ) public boolean sync = true; //говно для стрикта
    @Value( "Clear Delay" ) @Bounds( min = 1, max = 60 ) public int clearDelay = 10;
    @Value( "Anti City" ) public boolean antiSurround = false;
    //checks
    @Value( "Min Damage" ) @Bounds( min = 0.1f, max = 36f ) public float minDamage = 6f;
    @Value( "Max Self Damage" ) @Bounds( min = 0.1f, max = 36f ) public float maxSelf = 12f;
    @Value( "FacePlace Damage" ) @Bounds( max = 36 ) public int faceplace = 8;
    @Value( "Armor Scale" ) @Bounds( max = 100 ) public int armor = 12;
    //pause
    @Value( "Pause Health" ) @Bounds( max = 36f ) public float pauseHealth = 4f;
    @Value( "Pause While Eating" ) public boolean pauseOnEat = false;
    //rotation
    @Value( "Rotate" ) public Rotations rotations = Rotations.NONE;
    @Value( "Client Side" ) public boolean clientSide = false;
    @Value( "Randomize" ) public boolean randomize = true;
    @Value( "Yaw Step" ) public AutoCrystal.YawStep yawStep = AutoCrystal.YawStep.NONE;
    @Value( "Steps" ) @Bounds( min = 0, max = 1f ) public float steps = 0.3f;
    //render
    @Value( "Render" ) public boolean render = true;
    @Value( "Box" ) public JColor box = new JColor(255, 255, 255, 120, false);
    @Value( "Line" ) public JColor line = new JColor(255, 255, 255, 255, false);
    @Value( "Line Width" ) @Bounds( max = 3f ) public float lineWidth = 0.6f;

    Random random = new Random();
    Set<BlockPos> placedCrystals = new HashSet<>();
    EntityPlayer target;
    Entity lastHitEntity;
    BlockPos current;
    Rotation rotation;

    private final Timer breakTimer = new Timer();
    private final Timer placeTimer = new Timer();
    private final Timer clearTimer = new Timer();

    @Handler public Listener<PacketEvent.Send> sendListener = new Listener<>(PacketEvent.Send.class, e -> {
        if (e.getPacket() instanceof CPacketPlayerTryUseItemOnBlock && mc.player.getHeldItem((( CPacketPlayerTryUseItemOnBlock ) e.getPacket()).getHand()).getItem() == Items.END_CRYSTAL) {
            placedCrystals.add((( CPacketPlayerTryUseItemOnBlock ) e.getPacket()).getPos());
        }
    });

    @Handler
    public Listener<UpdateWalkingPlayerEvent> walkingPlayerEventListener = new Listener<>(UpdateWalkingPlayerEvent.class, e -> {
        if (e.getStage() == 0 && rotation != null && rotations != Rotations.NONE) {
            if (yawStep != AutoCrystal.YawStep.NONE && steps < 1) {
                float packetYaw = (( AccessorEntityPlayerSP ) mc.player).getLastReportedYaw();
                float diff = MathHelper.wrapDegrees(rotation.getYaw() - packetYaw);
                if (Math.abs(diff) > 180 * steps) {
                    rotation.setYaw(packetYaw + (diff * ((180 * steps) / Math.abs(diff))));
                }
            }
            if (randomize) {
                rotation.setYaw(rotation.getYaw() + (random.nextInt(4) - 2) / 100f);
            }

            if (clientSide) {
                mc.player.rotationYaw = rotation.getYaw();
                mc.player.rotationPitch = rotation.getPitch();
            } else {
                RotationHandler.getInstance().setRotation(rotation);
            }

        }
    });

    @Handler public Listener<PacketEvent.Receive> packetEventReceive = new Listener<>(PacketEvent.Receive.class, e -> {
        if (e.getPacket() instanceof SPacketSpawnObject && instant) {
            SPacketSpawnObject packet = e.getPacket();
            if (packet.getType() == 51) {
                BlockPos toRemove = null;
                for (BlockPos pos : placedCrystals) {
                    boolean canSee = EntityUtil.INSTANCE.canSee(pos);
                    if (!canSee && (rayTrace == RayTrace.FULL || rayTrace == RayTrace.HIT)) break;

                    if (mc.player.getDistance(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5) >= (canSee ? hitRange : wallBreakRange))
                        break;

                    if (hitMode == BreakMode.OWN && Math.sqrt(getDistance(pos.getX(), pos.getY(), pos.getZ(), packet.getX(), packet.getY(), packet.getZ())) > 1.5)
                        continue;

                    if (hitMode == BreakMode.SMART && EntityUtil.getHealth(mc.player) - EntityUtil.calculate(packet.getX(), packet.getY(), packet.getZ(), mc.player, terrainIgnore) < 0)
                        break;

                    toRemove = pos;
                    if (inhibit) {
                        try {
                            lastHitEntity = mc.world.getEntityByID(packet.getEntityID());
                        } catch (Exception ignored) { }
                    }

                    if (rotations == Rotations.FULL) {
                        rotation = RotationManagement.calcRotation(new BlockPos(packet.getX(), packet.getY(), packet.getZ()));
                    }

                    AccessorCPacketUseEntity hitPacket = ( AccessorCPacketUseEntity ) new CPacketUseEntity();
                    hitPacket.setEntityId(packet.getEntityID());
                    hitPacket.setAction(CPacketUseEntity.Action.ATTACK);
                    mc.player.connection.sendPacket(( CPacketUseEntity ) hitPacket);
                    swing();
                    break;
                }
                if (toRemove != null) {
                    placedCrystals.remove(toRemove);
                }
            }
        }
        if (e.getPacket() instanceof SPacketSoundEffect && inhibit && lastHitEntity != null) {
            SPacketSoundEffect packet = e.getPacket();
            if (packet.getCategory() == SoundCategory.BLOCKS && packet.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                if (lastHitEntity.getDistance(packet.getX(), packet.getY(), packet.getZ()) <= 6.0f) {
                    lastHitEntity.setDead();
                }
            }
        }
    });

    @Override protected void onToggle() {
        current = null;
        rotation = null;
        breakTimer.reset();
        placeTimer.reset();
    }

    @Override public void onTick() {
        if (nullCheck()) return;
        if (clearTimer.passed(clearDelay * 1000)) {
            placedCrystals.clear();
            clearTimer.reset();
        }
        target = EntityUtil.getTarget(targetRange);
        if (target == null) {
            current = null;
            return;
        }

        if ((mc.player.isHandActive() && pauseOnEat) || (EntityUtil.getHealth(mc.player) <= pauseHealth)) return;

        placer();
        breaker();

    }

    void placer() {
        if (!place || !placeTimer.passed(placeDelay)) return;
        EnumHand hand = null;
        double max = 0.5;

        for (BlockPos bp : BlockUtil.INSTANCE.getSphere(placeRange, true)) {

            if (!(isPosValid(bp) && BlockUtil.canPlaceCrystal(bp, ecme, true, multiplace))) continue;

            double damage = EntityUtil.calculate(( double ) bp.getX() + 0.5, ( double ) bp.getY() + 1.0, ( double ) bp.getZ() + 0.5, target, terrainIgnore),
                    localMinDamage = EntityUtil.getHealth(target) < faceplace || ItemUtil.isArmorLow(target, armor) ? 0.6 : minDamage,
                    self = EntityUtil.calculate(( double ) bp.getX() + 0.5, ( double ) bp.getY() + 1.0, ( double ) bp.getZ() + 0.5, mc.player, terrainIgnore) + 2.0;

            if (damage <= localMinDamage || self >= maxSelf || EntityUtil.getHealth(mc.player) - maxSelf < 0 || damage < 0.5) {
                continue;
            }

            if (damage > max) {
                max = damage;
                current = bp;
            }

        }

        if (current == null || max == 0.5) {
            current = null;
            return;
        }

        if (sync && placedCrystals.contains(current)) {
            return;
        }

        boolean isOffhand = mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL;
        int old = -1;

        if (mc.player.isHandActive()) hand = mc.player.getActiveHand();

        if (swap != SwapMode.NONE && !isOffhand && mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL) {
            old = mc.player.inventory.currentItem;
            if (ItemUtil.swapToHotbarSlot(ItemUtil.findItem(ItemEndCrystal.class), false) == -1) return;
        }

        if (rotations != Rotations.NONE) {
            rotation = RotationManagement.calcRotation(current);
        }

        placeCrystal(current, isOffhand);

        if (hand != null) mc.player.setActiveHand(hand);

        if (old != -1 && swap == SwapMode.SILENT) {
            ItemUtil.swapToHotbarSlot(old, false);
        }

    }

    void placeCrystal(BlockPos pos, boolean offhand) {
        if (pos == null) return;
        System.out.println("PLACE");
        RayTraceResult result = mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + ( double ) mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(( double ) pos.getX() + 0.5, ( double ) pos.getY() - 0.5, ( double ) pos.getZ() + 0.5));
        EnumFacing facing = result == null || result.sideHit == null ? EnumFacing.UP : result.sideHit;
        mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, facing, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0f, 0f, 0f));
        mc.player.connection.sendPacket(new CPacketAnimation(swing == SwingMode.MAINHAND ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND));
        mc.playerController.updateController();
        placeTimer.reset();
    }

    void swing() {
        if (swing == SwingMode.NONE) return;
        if (packetSwing) {
            mc.player.connection.sendPacket(new CPacketAnimation(swing == SwingMode.MAINHAND ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND));
        } else {
            mc.player.swingArm(swing == SwingMode.MAINHAND ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND);
        }
    }

    void breaker() {
        if (!hit || !breakTimer.passed(breakDelay)) return;

        Entity crystal = null;
        double max = -1337;

        for (Entity e : mc.world.loadedEntityList) {
            if (e instanceof EntityEnderCrystal && isCrystalValid(( EntityEnderCrystal ) e)) {
                if (hitCalc) {
                    double dmg = EntityUtil.calculate(e.posX, e.posY, e.posZ, target, terrainIgnore);

                    if (dmg > max) {
                        max = dmg;
                        crystal = e;
                    }

                } else {
                    double distance = -mc.player.getDistance(e);

                    if (distance > max) {
                        max = distance;
                        crystal = e;
                    }

                }
            }
        }

        if (crystal == null) return;

        int curr = -1;

        if (antiWeakness != AntiWeakness.NONE && mc.player.isPotionActive(MobEffects.WEAKNESS)) {
            for (int j = 0; j < 9; j++) {
                Item item = mc.player.inventory.getStackInSlot(j).getItem();
                if (item instanceof ItemSword || item instanceof ItemPickaxe) {
                    curr = mc.player.inventory.currentItem;
                    ItemUtil.swapToHotbarSlot(j, false);
                    break;
                }
            }
        }

        if (rotations == Rotations.FULL) {
            rotation = RotationManagement.calcRotation(crystal);
        }

        hit(( EntityEnderCrystal ) crystal);

        if (antiWeakness == AntiWeakness.SILENT && curr != -1) {
            ItemUtil.swapToHotbarSlot(curr, false);
        }

    }

    void hit(EntityEnderCrystal entityEnderCrystal) {
        System.out.println("HIT");
        lastHitEntity = entityEnderCrystal;
        mc.player.connection.sendPacket(new CPacketUseEntity(entityEnderCrystal));
        swing();
        breakTimer.reset();
        BlockPos toRemove = null;
        if (sync) {
            for (BlockPos blockPos : placedCrystals) {
                if (entityEnderCrystal.getDistance(blockPos.getX(), blockPos.getY(), blockPos.getZ()) <= 3)
                    toRemove = blockPos;
            }
        }
        if (toRemove != null) placedCrystals.remove(toRemove);
    }

    @Override public void onRender3D(float partialTicks) {
        if (current == null) return;
        Renderer3D.drawBoxESP(current, box.getColor(), line.getColor(), lineWidth, true, true, box.getColor().getAlpha(), line.getColor().getAlpha(), 1f);
    }

    boolean isPosValid(BlockPos pos) {
        boolean canSee = EntityUtil.INSTANCE.canSee(pos);
        if (!canSee && (rayTrace == RayTrace.FULL || rayTrace == RayTrace.PLACE)) return false;
        return mc.player.getDistance(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5) <= (canSee ? placeRange : wallPlaceRange);
    }

    boolean isCrystalValid(EntityEnderCrystal e) {
        boolean canSee = mc.player.canEntityBeSeen(e);
        if (!canSee && (rayTrace == RayTrace.FULL || rayTrace == RayTrace.HIT)) return false;

        if (hitMode == BreakMode.OWN) {
            for (BlockPos blockPos : placedCrystals) {
                if (e.getDistance(blockPos.getX(), blockPos.getY(), blockPos.getZ()) <= 3) continue;
                return false;
            }
        } else if (hitMode == BreakMode.SMART && EntityUtil.getHealth(mc.player) - EntityUtil.calculate(e.posX, e.posY, e.posZ, mc.player, terrainIgnore) < 0) {
            return false;
        }
        return mc.player.getDistance(e) <= (canSee ? hitRange : wallBreakRange);
    }

    double getDistance(double x, double y, double z, double x1, double y1, double z1) {
        double d0 = x - x1;
        double d1 = y - y1;
        double d2 = z - z1;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    public enum RayTrace {
        NONE,
        HIT,
        PLACE,
        FULL
    }

    public enum BreakMode {
        ALWAYS,
        OWN,
        SMART
    }

    public enum AntiWeakness {
        NONE,
        SILENT,
        NORMAL
    }

    public enum Rotations {
        NONE,
        SEMI,
        FULL
    }

    public enum SwapMode {
        NONE,
        AUTO,
        SILENT
    }

    public enum SwingMode {
        OFFHAND,
        MAINHAND,
        NONE
    }

}
