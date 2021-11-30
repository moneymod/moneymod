package wtf.moneymod.client.mixin.mixins.ducks;

import net.minecraft.network.play.client.CPacketChatMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CPacketChatMessage.class)
public interface AccessorCPacketChatMessage {

    @Accessor("message")
    void setMessage(String message);
}