package com.builtbroken.mc.prefab.tile.multiblock;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by Dark on 7/4/2015.
 */
public class BlockMultiblock extends BlockContainer
{
    protected BlockMultiblock()
    {
        super(Material.circuits);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return null;
    }
}
