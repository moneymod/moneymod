package wtf.moneymod.client.impl.module.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.math.MathUtil;
import wtf.moneymod.client.impl.utility.impl.player.ItemUtil;
import wtf.moneymod.client.impl.utility.impl.render.JColor;
import wtf.moneymod.client.impl.utility.impl.world.BlockUtil;
import wtf.moneymod.client.impl.utility.impl.world.EntityUtil;

import java.util.ArrayList;
import java.util.Comparator;

@Module.Register( label = "AutoCrystal", cat = Module.Category.COMBAT )
public class AutoCrystal extends Module {

    @Value( value = "Place" ) public boolean place = true;
    @Value( value = "Break" ) public boolean hit = true;
    @Value( value = "Break Mode" ) public Break hitMode = Break.PACKET;

    @Value( value = "Logic" ) public Logic logic = Logic.BREAKPLACE;
    @Value( value = "1.13" ) public boolean ecme = false;

    @Value( value = "Place Range " ) @Bounds( max = 6 ) public float placeRange = 4f;
    @Value( value = "Break Range " ) @Bounds( max = 6 ) public float breakRange = 4f;
    @Value( value = "Wall Range" ) @Bounds( max = 6 ) public float wallRange = 3.5f;
    @Value( value = "Target Range" ) @Bounds( max = 16 ) public float targetRange = 12f;

    @Value( value = "Place Delay" ) @Bounds( max = 15 ) public int placeDelay = 0;
    @Value( value = "Break Delay" ) @Bounds( max = 15 ) public int breakDelay = 0;

    @Value( value = "Min Damage" ) @Bounds( max = 36 ) public float minDmg = 4f;
    @Value( value = "Self Damage" ) @Bounds( max = 36 ) public float selfDmg = 16f;

    @Value( value = "Faceplace" ) public boolean faceplace = true;
    @Value( value = "FacePlace Health" ) public float faceplaceHealth = 8f;
    @Value( value = "Armor Breaker" ) public int armorBreaker = 15;

    @Value( value = "Swap" ) public Swap swap = Swap.NONE;
    @Value( value = "Optimize" ) public boolean optimize = true;

    @Value( value = "Render" ) public boolean render = true;
    @Value( value = "RenderText" ) public boolean textRender = true;
    @Value( value = "Fade" ) public boolean fade = false;
    @Value( value = "Color" ) public JColor color = new JColor(255, 255, 255, 120, false);

    public enum Swap {
        NONE,
        NORMAL,
        SILENT
    }

    public enum Break {
        INSTANT,
        PACKET,
        NORMAL
    }

    public enum Logic {
        BREAKPLACE,
        PLACEBREAK;
    }

}
