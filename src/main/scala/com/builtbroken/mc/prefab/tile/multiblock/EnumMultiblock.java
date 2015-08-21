package com.builtbroken.mc.prefab.tile.multiblock;

import com.builtbroken.mc.prefab.tile.multiblock.types.TileMultiInv;
import com.builtbroken.mc.prefab.tile.multiblock.types.TileMultiTank;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.HashMap;

/**
 * Enum of different mutliblock tiles that can be used to restrict connections
 * Created by Dark on 7/4/2015.
 */
public enum EnumMultiblock
{
    /* Basic */
    /* 0 */TILE("veTileMulti", TileMulti.class, new ITileEntityProvider()
    {
        @Override
        public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
        {
            return new TileMulti();
        }
    }),
    /* 1 */TANK("veTileMultiTank", TileMultiTank.class, new ITileEntityProvider()
    {
        @Override
        public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
        {
            return new TileMultiTank();
        }
    }),
    /* 2 */INVENTORY("veTileMultiInv", TileMultiInv.class, new ITileEntityProvider()
    {
        @Override
        public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
        {
            return new TileMultiInv();
        }
    }),
    /* 3 */ENERGY_RF,
    /* 4 */ENERGY_EU,
    /* 5 */ENERGY,
    /* Combinations */
    /* 6 */TANK_INV,
    /* 7 */TANK_ENERGY,
    /* 8 */INV_ENERGY,
    /* 9 */TANK_INV_ENERGY;

    private String name;
    public Class<? extends TileMulti> clazz;
    public ITileEntityProvider provider;

    private static boolean init = false;
    private static final HashMap<String, EnumMultiblock> cache = new HashMap();

    EnumMultiblock() {}

    EnumMultiblock(String name, Class<? extends TileMulti> clazz)
    {
        this(name, clazz, null);
    }

    EnumMultiblock(String name, Class<? extends TileMulti> clazz, ITileEntityProvider provider)
    {
        this.name = name;
        this.clazz = clazz;
        this.provider = provider;
    }

    public String getName()
    {
        return name;
    }

    public TileMulti newTile(World world, int meta)
    {
        if (provider != null)
        {
            TileEntity tile = provider.createNewTileEntity(world, meta);
            if (tile instanceof TileMulti)
                return (TileMulti) tile;
        }
        return null;
    }

    public static TileMulti provideTile(World world, int meta)
    {
        if (meta >= 0 && meta < values().length)
        {
            return values()[meta].newTile(world, meta);
        }
        return null;
    }

    private static void init()
    {
        //Cache so look up is a little faster
        for (EnumMultiblock e : values())
        {
            if (e.name != null && !e.name.isEmpty())
                cache.put(e.name.toLowerCase(), e);
        }
        init = true;
    }

    public static void register()
    {
        for (EnumMultiblock e : values())
        {
            if (e.clazz != null && e.name != null)
            {
                GameRegistry.registerTileEntity(e.clazz, e.name);
            }
        }
    }

    public static EnumMultiblock get(String name)
    {
        if (!init)
            init();

        if (cache.containsKey(name.toLowerCase()))
        {
            return cache.get(name.toLowerCase());
        }
        return null;
    }
}
