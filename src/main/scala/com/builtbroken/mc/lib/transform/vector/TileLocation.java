package com.builtbroken.mc.lib.transform.vector;

import com.builtbroken.mc.api.IWorldPosition;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/** Used to position something near a tile without using the tile itself
 * Created by Dark on 6/15/2015.
 */
public class TileLocation implements IWorldPosition
{
    TileEntity tile;
    double x_offest = 0;
    double y_offset = 0;
    double z_offset = 0;

    public TileLocation(TileEntity tile, double x, double y, double z)
    {
        this.tile = tile;
        this.x_offest = x;
        this.y_offset = y;
        this.z_offset = z;
    }

    @Override
    public World world()
    {
        return tile.getWorldObj();
    }

    @Override
    public double x()
    {
        return tile.xCoord + x_offest;
    }

    @Override
    public double y()
    {
        return tile.yCoord + y_offset;
    }

    @Override
    public double z()
    {
        return tile.zCoord + z_offset;
    }
}
