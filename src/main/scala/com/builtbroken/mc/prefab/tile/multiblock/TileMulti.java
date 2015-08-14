package com.builtbroken.mc.prefab.tile.multiblock;

import com.builtbroken.mc.api.tile.multiblock.IMultiTile;
import com.builtbroken.mc.api.tile.multiblock.IMultiTileHost;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by Dark on 8/9/2015.
 */
public class TileMulti extends TileEntity implements IMultiTile
{
    IMultiTileHost host;

    @Override
    public IMultiTileHost getHost()
    {
        return host;
    }

    @Override
    public void setHost(IMultiTileHost host)
    {
        this.host = host;
    }

    @Override
    public void invalidate()
    {
        if(host != null)
        {
            host.onTileInvalidate(this);
        }
        super.invalidate();
    }

    @Override
    public boolean canUpdate()
    {
        return false;
    }
}
