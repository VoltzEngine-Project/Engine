package com.builtbroken.mc.prefab.tile;

import net.minecraft.block.material.Material;

/**
 * Created by robert on 1/12/2015.
 */
public class TileEnt extends Tile
{
    public TileEnt(String name, Material material)
    {
        super(name, material);
    }

    @Override
    public Tile newTile()
    {
        try
        {
            return getClass().newInstance();
        }
        catch (InstantiationException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
