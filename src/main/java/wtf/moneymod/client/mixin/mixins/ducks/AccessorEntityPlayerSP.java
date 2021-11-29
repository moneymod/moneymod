package wtf.moneymod.client.mixin.mixins.ducks;

import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin( EntityPlayerSP.class )
public interface AccessorEntityPlayerSP {

    @Accessor( "handActive" )
    void mm_setHandActive(boolean value);

    @Accessor( "lastReportedYaw" )
    float getLastReportedYaw();

}
