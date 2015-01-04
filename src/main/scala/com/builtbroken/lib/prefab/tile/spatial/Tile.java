package com.builtbroken.lib.prefab.tile.spatial;

import com.builtbroken.lib.transform.vector.IVector3;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by robert on 1/4/2015.
 */
public class Tile extends TileEntity implements IVector3
{
    private BlockDummy  block = null;

    public CreativeTabs creativeTab = CreativeTabs.tabMisc;
    public Material material = Material.clay;
    public float hardness = 1;
    public float resistance = 1;

    public Tile(Material material)
    {
        this.material = material;
    }


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
}
