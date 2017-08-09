package com.builtbroken.mc.framework.mod;

import com.builtbroken.mc.api.tile.access.IGuiTile;
import com.builtbroken.mc.api.tile.node.ITileNodeHost;
import com.builtbroken.mc.framework.mod.loadable.AbstractLoadable;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

/**
 * An abstract proxy that can be extended by any mod.
 */
public abstract class AbstractProxy extends AbstractLoadable implements IGuiHandler
{
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if (ID == 10002)
        {
            return getServerGuiElement(y, player, x);
        }
        else if (ID == 10001)
        {
            return getServerGuiElement(y, player, world.getEntityByID(x));
        }
        return getServerGuiElement(ID, player, world.getTileEntity(x, y, z));
    }

    public Object getServerGuiElement(int ID, EntityPlayer player, int slot)
    {
        ItemStack stack = player.inventory.getStackInSlot(slot);
        if (stack != null && stack.getItem() instanceof IGuiTile)
        {
            return ((IGuiTile) stack.getItem()).getServerGuiElement(ID, player);
        }
        return null;
    }

    public Object getServerGuiElement(int ID, EntityPlayer player, TileEntity tile)
    {
        if (tile instanceof IGuiTile)
        {
            return ((IGuiTile) tile).getServerGuiElement(ID, player);
        }
        else if (tile instanceof ITileNodeHost && ((ITileNodeHost) tile).getTileNode() instanceof IGuiTile)
        {
            return ((IGuiTile) ((ITileNodeHost) tile).getTileNode()).getServerGuiElement(ID, player);
        }
        return null;
    }

    public Object getServerGuiElement(int ID, EntityPlayer player, Entity entity)
    {
        if (entity instanceof IGuiTile)
        {
            return ((IGuiTile) entity).getServerGuiElement(ID, player);
        }
        else if (entity instanceof ITileNodeHost && ((ITileNodeHost) entity).getTileNode() instanceof IGuiTile)
        {
            return ((IGuiTile) ((ITileNodeHost) entity).getTileNode()).getServerGuiElement(ID, player);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if (ID == 10002)
        {
            return getServerGuiElement(y, player, world.getEntityByID(x));
        }
        else if (ID == 10001)
        {
            return getClientGuiElement(y, player, world.getEntityByID(x));
        }
        return getClientGuiElement(ID, player, world.getTileEntity(x, y, z));
    }

    public Object getClientGuiElement(int ID, EntityPlayer player, int slot)
    {
        ItemStack stack = player.inventory.getStackInSlot(slot);
        if (stack != null && stack.getItem() instanceof IGuiTile)
        {
            return ((IGuiTile) stack.getItem()).getClientGuiElement(ID, player);
        }
        return null;
    }

    public Object getClientGuiElement(int ID, EntityPlayer player, TileEntity tile)
    {
        if (tile instanceof IGuiTile)
        {
            return ((IGuiTile) tile).getClientGuiElement(ID, player);
        }
        else if (tile instanceof ITileNodeHost && ((ITileNodeHost) tile).getTileNode() instanceof IGuiTile)
        {
            return ((IGuiTile) ((ITileNodeHost) tile).getTileNode()).getClientGuiElement(ID, player);
        }
        return null;
    }

    public Object getClientGuiElement(int ID, EntityPlayer player, Entity entity)
    {
        if (entity instanceof IGuiTile)
        {
            return ((IGuiTile) entity).getClientGuiElement(ID, player);
        }
        else if (entity instanceof ITileNodeHost && ((ITileNodeHost) entity).getTileNode() instanceof IGuiTile)
        {
            return ((IGuiTile) ((ITileNodeHost) entity).getTileNode()).getClientGuiElement(ID, player);
        }
        return null;
    }

    @SideOnly(Side.CLIENT)
    public boolean isShiftHeld()
    {
        return Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
    }

    /**
     * Called to register {@link com.builtbroken.mc.api.tile.listeners.ITileEventListenerBuilder}s
     */
    public void loadJsonContentHandlers()
    {

    }
}
