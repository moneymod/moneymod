package wtf.moneymod.client.impl.utility.impl.cape;

import net.minecraft.util.ResourceLocation;

public enum CapeEnum
{
    SQUIDGAME( new ResourceLocation( "minecraft:moneymod/capes/squidgame.png" ), "squidgame"),
    PIGCAPE( new ResourceLocation( "minecraft:moneymod/capes/pigcape.png" ), "pigcape");

    private ResourceLocation loc;
    private String name;

    CapeEnum( ResourceLocation loc, String name )
    {
        this.loc = loc;
        this.name = name;
    }

    public ResourceLocation getResourceLocation( )
    {
        return loc;
    }

    public String getCapeName( )
    {
        return name;
    }
    }