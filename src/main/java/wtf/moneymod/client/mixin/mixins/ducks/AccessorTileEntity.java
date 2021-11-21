package wtf.moneymod.client.mixin.mixins.ducks;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TileEntity.class)
public interface AccessorTileEntity {
    @Accessor(value = "blockType")
    public void setBlockType(Block blockType);
}