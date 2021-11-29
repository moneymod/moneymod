package wtf.moneymod.client.mixin.mixins;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import wtf.moneymod.client.mixin.accessors.IEntity;

@Mixin( Entity.class )
public class MixinEntity implements IEntity
{
    @Shadow
    public boolean isInWeb;

    public boolean isInWeb( )
    {
        return this.isInWeb;
    }

    public void setInWeb( boolean state ) { this.isInWeb = state; }
}