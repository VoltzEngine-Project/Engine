package com.builtbroken.mc.framework.tile;

import com.builtbroken.mc.framework.tile.api.ITileHost;
import com.builtbroken.mc.prefab.tile.interfaces.tile.ITile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Logic object that takes over for the TileEntity class in Minecraft
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/15/2016.
 */
public class Tile implements ITile
{
    protected ITileHost host;

    @Override
    public void setTileHost(ITileHost host)
    {
        this.host = host;
    }

    @Override
    public ITileHost getTileHost()
    {
        return host;
    }

    @Override
    public void firstTick()
    {

    }

    @Override
    public void update(long tick, double delta)
    {

    }

    @Override
    public void onAdded()
    {

    }

    @Override
    public void onRemove()
    {

    }

    @Override
    public void invalidate()
    {

    }

    @Override
    public boolean removeByPlayer(EntityPlayer player, boolean willHarvest)
    {
        return host != null && host.isBlock() ? world().setBlockToAir(xi(), yi(), zi()) : true;
    }

    @Override
    public World world()
    {
        return host != null ? host.world() : null;
    }

    @Override
    public double x()
    {
        return host != null ? host.x() : 0;
    }

    @Override
    public double y()
    {
        return host != null ? host.y() : 0;
    }

    @Override
    public double z()
    {
        return host != null ? host.z() : 0;
    }

    public int xi()
    {
        return host != null ? (int) host.x() : 0;
    }

    public int yi()
    {
        return host != null ? (int) host.y() : 0;
    }

    public int zi()
    {
        return host != null ? (int) host.z() : 0;
    }
}
