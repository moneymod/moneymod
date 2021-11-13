package wtf.moneymod.client.impl.module.combat;

import net.minecraft.block.BlockObsidian;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.input.Keyboard;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.misc.Timer;
import wtf.moneymod.client.impl.utility.impl.player.ItemUtil;
import wtf.moneymod.client.impl.utility.impl.world.BlockUtil;
import wtf.moneymod.client.impl.utility.impl.world.ChatUtil;
import wtf.moneymod.client.impl.utility.impl.world.EntityUtil;
import wtf.moneymod.client.mixin.mixins.ducks.AccessorKeyBinding;

@Module.Register( label = "BurrowBypass", cat = Module.Category.COMBAT)
public class SelfFillBypass extends Module {

    @Value(value = "Mode") public Mode mode = Mode.SECOND;
    @Value(value = "Ticks") @Bounds( min = 10, max = 100) public int ticks = 50;
    @Value(value = "Toggle Delay") @Bounds( min = 10, max = 60) public int toggleDelays = 20;
    @Value(value = "One Delay") @Bounds( min = 10, max = 60) public int oneDelays = 42;
    @Value(value = "Second Delay") @Bounds( min = 10, max = 60) public int placeDelay = 30;

    BlockPos position;

    int time;
    BlockPos pos;
    int stages;
    int delay, pdelay,stage,jumpdelay,toggledelay;
    boolean jump;
    Timer timer = new Timer();

    @Override public void onEnable(){
        position = new BlockPos(mc.player.getPositionVector());
    }
    @Override public void onToggle(){

        time = 0;
        pos = null;
        stages = 0;

        pdelay = 0;
        stage = 1;
        toggledelay = 0;
        jumpdelay = 0;
        timer.reset();
        jump = false;
        Main.TICK_TIMER = 1;
        position = null;
        delay = 0;
    }

    @Override public void onTick() {

        if (position != null) {
            if(mode == Mode.FIRST){
                firstmethod();
            } else { secondMethod(); }
        }
    }

    public void secondMethod(){
        BlockPos pos = new BlockPos(mc.player.getPositionVector());
        if (stages == 0){
            time++;
            Main.TICK_TIMER = 30;
            ((AccessorKeyBinding) mc.gameSettings.keyBindJump).setPressed(true);
            if (time > 35){
                Main.TICK_TIMER = 1;
                time = 0;
                ((AccessorKeyBinding) mc.gameSettings.keyBindJump).setPressed(false);
                stages = 1;
            }
        }
        if (stages == 1){
            time++;
            Main.TICK_TIMER = 1;
            ((AccessorKeyBinding) mc.gameSettings.keyBindJump).setPressed(true);
            BlockUtil.INSTANCE.placeBlock(pos);

            if (time > 35){
                time = 0;
                ((AccessorKeyBinding) mc.gameSettings.keyBindJump).setPressed(false);
                Main.TICK_TIMER = 1;
                if (pos.getY() != mc.player.posY){
                    stages = 2;
                } else {
                    ChatUtil.INSTANCE.sendMessage("[Burrow Bypass] - lucky!");
                    setToggled(false);
                }
            }
        }
        if (stages == 2){
            time++;
            ((AccessorKeyBinding) mc.gameSettings.keyBindJump).setPressed(true);
            Main.TICK_TIMER = 30;
            if (time > 25){
                Main.TICK_TIMER = 1;
                time = 0;
                if (pos.getY() != mc.player.posY){
                    ChatUtil.INSTANCE.sendMessage("[Burrow Bypass] - unlucky");
                    setToggled(false);
                } else {
                    ChatUtil.INSTANCE.sendMessage("[Burrow Bypass] - lucky!");
                }
            }

        }
    }

    public void firstmethod(){
        if (stage == 1) {
            delay++;
            ((AccessorKeyBinding) mc.gameSettings.keyBindJump).setPressed(true);
            Main.TICK_TIMER = ticks;
            if (delay >= oneDelays) {
                stage = 2;
                delay = 0;
                Main.TICK_TIMER = 1;
                ((AccessorKeyBinding) mc.gameSettings.keyBindJump).setPressed(false);
            }
        }
        if (stage == 2){
            Main.TICK_TIMER = 1;
            if (mc.player.onGround) ((AccessorKeyBinding) mc.gameSettings.keyBindJump).setPressed(true);;
            BlockUtil.INSTANCE.placeBlock(position);
            pdelay++;
            if (pdelay >= placeDelay){
                stage = 3;
                pdelay = 0;
                ((AccessorKeyBinding) mc.gameSettings.keyBindJump).setPressed(false);
                Main.TICK_TIMER = 1;

            }
        }
        if (stage == 3){
            toggledelay++;
            Main.TICK_TIMER = ticks;
            ((AccessorKeyBinding) mc.gameSettings.keyBindJump).setPressed(true);
            if (toggledelay >= toggleDelays) {
                mc.player.motionY -= 0.4;
                Main.TICK_TIMER = 1;
                ((AccessorKeyBinding) mc.gameSettings.keyBindJump).setPressed(false);
                setToggled(false);
            }
        }
    }
    public enum Mode{ FIRST, SECOND }
}