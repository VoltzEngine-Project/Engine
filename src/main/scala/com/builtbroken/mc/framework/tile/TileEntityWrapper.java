package com.builtbroken.mc.framework.tile;

import com.builtbroken.mc.api.IWorldPosition;
import com.builtbroken.mc.prefab.tile.entity.TileEntityBase;
import net.minecraft.block.Block;
import net.minecraft.world.World;

/**
 * Wrapper object for redirecting calls to the {@Tile} object
 * <p>
 * Created by Robert(DarkGuardsman) on 4/11/2016
 */
public final class TileEntityWrapper extends TileEntityBase implements IWorldPosition
{
    /** Number of ticks that have passed since this object was created*/
    public long ticks = 0L;

    /** Tile object this is wrappered around */
    public final Tile tile;

    /** Default method for forge to use when loading this tile */
    public TileEntityWrapper()
    {

    }

    /** Constructor to use when creating new instances of this object */
    public TileEntityWrapper(Tile tile)
    {
        this.tile = tile;
    }

    /**
     * TILE, Called by the world to update the tile. Never
     * call this from your owner code. Use Update() method
     * as this is set final to ensure base functionality.
     */
    @Override
    public final void updateEntity()
    {
        if(tile != null)
        {
            if (ticks == 0)
            {
                tile.firstTick();
            }
            else
            {
                tile.update(ticks);
            }

            //Increase tick
            if (ticks >= Long.MAX_VALUE - 5)
            {
                ticks = 1;
            }
            ticks += 1;
        }
    }

    //==============================
    //==== Location ================
    //==============================
    @Override
    public double z()
    {
        return zCoord;
    }

    public int zi()
    {
        return zCoord;
    }

    @Override
    public double x()
    {
        return xCoord;
    }

    public int xi()
    {
        return xCoord;
    }

    @Override
    public double y()
    {
        return yCoord;
    }

    public int yi()
    {
        return yCoord;
    }

    @Override
    public World world()
    {
        return getWorldObj();
    }
}
