package wtf.moneymod.client.mixin.mixins;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import wtf.moneymod.client.impl.utility.impl.render.fonts.FontRender;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Mixin( GuiMainMenu.class )
public class MixinGuiMainMenu extends GuiScreen {
    @Inject( method = { "drawScreen" }, at = { @At( "TAIL" ) }, cancellable = true )
    public void drawText( int mouseX, int mouseY, float partialTicks, CallbackInfo ci ) {
        FontRender.drawStringWithShadow( "moneymod", 1,1,-1);
    }
}