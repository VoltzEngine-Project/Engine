package com.builtbroken.mc.lib.mod;

import com.builtbroken.mc.api.tile.IGuiTile;
import com.builtbroken.mc.prefab.tile.Tile;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import com.builtbroken.mc.lib.mod.loadable.ILoadable;

/**
 * An abstract proxy that can be extended by any mod.
 */
public abstract class AbstractProxy implements IGuiHandler, ILoadable
{
	public void preInit()
	{

	}

	public void init()
	{

	}

	public void postInit()
	{

	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		return getClientGuiElement(ID, player, world.getTileEntity(x, y, z));
	}

    public Object getServerGuiElement(int ID, EntityPlayer player, TileEntity tile)
    {
        if(tile instanceof IGuiTile)
        {
            return ((IGuiTile) tile).getServerGuiElement(ID, player);
        }
        return null;
    }

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		return getClientGuiElement(ID, player, world.getTileEntity(x, y, z));
	}

    public Object getClientGuiElement(int ID, EntityPlayer player, TileEntity tile)
    {
        if(tile instanceof IGuiTile)
        {
            return ((IGuiTile) tile).getClientGuiElement(ID, player);
        }
        return null;
    }
}
