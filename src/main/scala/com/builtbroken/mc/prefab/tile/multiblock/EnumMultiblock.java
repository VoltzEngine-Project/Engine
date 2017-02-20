package com.builtbroken.mc.prefab.tile.multiblock;

import com.builtbroken.mc.prefab.tile.multiblock.types.*;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.HashMap;

/**
 * Enum of different mutli-block tiles that can be used to control functionality of the structure.
 * <p>
 * As well improve handling by not using an et. al. approach to interfaces.
 * <p>
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
    /* Energy Types */
    /* 3 */ENERGY_RF("rfTileMulti"), //RF code is setup in a proxy class
    /* 4 */ENERGY_EU("euTileMulti"),//TODO implement
    /* 5 */ENERGY("veTileMultiEnergy", TileMultiEnergy.class, new ITileEntityProvider()
    {
        @Override
        public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
        {
            return new TileMultiEnergy();
        }
    }),  //TODO implement universal energy version
    /* Combinations */
    /* 6 */TANK_INV("veTileMultiTankInv", TileMultiTankInv.class, new ITileEntityProvider()
    {
        @Override
        public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
        {
            return new TileMultiTankInv();
        }
    }),
    /* 7 */TANK_ENERGY("veTileMultiTankEnergy", TileMultiTankEnergy.class, new ITileEntityProvider()
    {
        @Override
        public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
        {
            return new TileMultiTankEnergy();
        }
    }),//TODO implement universal energy version
    /* 8 */INV_ENERGY("veTileMultiInvEnergy", TileMultiInvEnergy.class, new ITileEntityProvider()
    {
        @Override
        public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
        {
            return new TileMultiInvEnergy();
        }
    }),//TODO implement universal energy version
    /* 9 */TANK_INV_ENERGY("veTileMultiTankInvEnergy", TileMultiTankInvEnergy.class, new ITileEntityProvider()
    {
        @Override
        public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
        {
            return new TileMultiTankInvEnergy();
        }
    });  //TODO implement universal energy version

    /** Registered name of the TileEntity.class and the reference name of the multi-block */
    public final String name;
    /** Class to register for this multi-block, not all entries will use this */
    public Class<? extends TileMulti> clazz;
    /** Provider for the multi-block tile type, defaults to {@link #TILE if missing} */
    public ITileEntityProvider provider;

    //Has the cached been initialized
    private static boolean init = false;
    //Quick reference cached to improve speed of multi-block builder
    private static final HashMap<String, EnumMultiblock> cache = new HashMap();

    EnumMultiblock(String name)
    {
        this.name = name;
    }

    EnumMultiblock(String name, Class<? extends TileMulti> clazz)
    {
        this.name = name;
        this.clazz = clazz;
    }

    EnumMultiblock(String name, Class<? extends TileMulti> clazz, ITileEntityProvider provider)
    {
        this.name = name;
        this.clazz = clazz;
        this.provider = provider;
    }

    public String getTileName()
    {
        return name;
    }

    public TileMulti newTile(World world, int meta)
    {
        if (provider != null)
        {
            final TileEntity tile = provider.createNewTileEntity(world, meta);
            if (tile instanceof TileMulti)
            {
                return (TileMulti) tile;
            }
        }
        return new TileMulti();
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
            {
                cache.put(e.name.toLowerCase(), e);
            }
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
        {
            init();
        }

        if (cache.containsKey(name.toLowerCase()))
        {
            return cache.get(name.toLowerCase());
        }
        return null;
    }
}
