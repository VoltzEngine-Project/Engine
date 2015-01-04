package com.builtbroken.lib.prefab.tile.spatial;

import com.builtbroken.lib.transform.vector.Vector2;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;

import java.util.Map;

/**
 * Created by robert on 1/4/2015.
 */
public class BlockTile extends Block
{
    public Tile staticTile = null;

    protected BlockTile(Tile tile)
    {
        super(tile.material);
        this.staticTile = tile;
    }

    public static Vector2 getClickedFace(Byte hitSide, float hitX, float hitY, float hitZ)
    {
        switch (hitSide)
        {
            case 0:
                return new Vector2(1 - hitX, hitZ);
            case 1:
                return new Vector2(hitX, hitZ);
            case 2:
                return new Vector2(1 - hitX, 1 - hitY);
            case 3:
                return new Vector2(hitX, 1 - hitY);
            case 4:
                return new Vector2(hitZ, 1 - hitY);
            case 5:
                return new Vector2(1 - hitZ, 1 - hitY);
            default:
                return new Vector2(0.5, 0.5);
        }
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return staticTile.creativeTab;
    }
}
