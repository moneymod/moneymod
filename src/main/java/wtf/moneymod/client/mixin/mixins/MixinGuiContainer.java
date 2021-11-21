package wtf.moneymod.client.mixin.mixins;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemShulkerBox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.impl.module.render.ShulkerPreview;
import wtf.moneymod.client.impl.utility.Globals;

@Mixin(GuiContainer.class)
public class MixinGuiContainer implements Globals {
    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    public void mouseClicked(int mouseX, int mouseY, int mouseButton, CallbackInfo info) {
        if (mc.player != null && mc.world != null && mouseButton == 2) {
            ShulkerPreview sp = (ShulkerPreview) Main.getMain().getModuleManager().get(ShulkerPreview.class);
            if (sp.isToggled()) {
                sp.setState(true);
                if (sp.getItem() instanceof ItemShulkerBox)
                    info.cancel();
            }
        }
    }
}