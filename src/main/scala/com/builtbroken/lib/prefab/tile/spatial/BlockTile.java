package com.builtbroken.lib.prefab.tile.spatial;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

/**
 * Created by robert on 1/4/2015.
 */
public class BlockTile extends Block
{
    private Tile tile = null;

    protected BlockTile(Tile tile)
    {
        super(tile.material);
        this.tile = tile;
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return tile.creativeTab;
    }
}
