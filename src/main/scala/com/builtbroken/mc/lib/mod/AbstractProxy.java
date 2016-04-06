package com.builtbroken.mc.lib.mod;

import com.builtbroken.mc.api.tile.IGuiTile;
import com.builtbroken.mc.lib.mod.loadable.ILoadable;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

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
		if(ID == 10001)
		{
			return getServerGuiElement(y, player, world.getEntityByID(x));
		}
		return getServerGuiElement(ID, player, world.getTileEntity(x, y, z));
	}

    public Object getServerGuiElement(int ID, EntityPlayer player, TileEntity tile)
    {
        if(tile instanceof IGuiTile)
        {
            return ((IGuiTile) tile).getServerGuiElement(ID, player);
        }
        return null;
    }

	public Object getServerGuiElement(int ID, EntityPlayer player, Entity entity)
	{
		if(entity instanceof IGuiTile)
		{
			return ((IGuiTile) entity).getServerGuiElement(ID, player);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if(ID == 10001)
		{
			return getClientGuiElement(y, player, world.getEntityByID(x));
		}
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

	public Object getClientGuiElement(int ID, EntityPlayer player, Entity entity)
	{
		if(entity instanceof IGuiTile)
		{
			return ((IGuiTile) entity).getClientGuiElement(ID, player);
		}
		return null;
	}
}
