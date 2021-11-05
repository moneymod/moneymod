package wtf.moneymod.client.mixin.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import wtf.moneymod.client.mixin.accessors.AccessorEntity;

@Mixin( Entity.class )
public class MixinEntity implements AccessorEntity
{
    @Shadow
    public boolean isInWeb;

    public boolean isInWeb( )
    {
        return this.isInWeb;
    }

    public void setInWeb( boolean state ) { this.isInWeb = state; }
}