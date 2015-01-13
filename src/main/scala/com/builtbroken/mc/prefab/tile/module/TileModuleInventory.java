package com.builtbroken.mc.prefab.tile.module;

import com.builtbroken.mc.api.ISave;
import com.builtbroken.mc.api.tile.IInventoryProvider;
import com.builtbroken.mc.api.tile.ITileModuleProvider;
import com.builtbroken.mc.api.tile.node.IExternalInventory;
import com.builtbroken.mc.api.tile.node.ITileModule;
import com.builtbroken.mc.prefab.inventory.ExternalInventory;
import com.builtbroken.mc.prefab.inventory.InventoryUtility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by robert on 1/12/2015.
 */
public class TileModuleInventory extends ExternalInventory implements ITileModule
{
    public TileModuleInventory(IInventoryProvider inv, int slots)
    {
        super(inv, slots);
        if(!(inv instanceof ITileModuleProvider))
        {
            throw new IllegalArgumentException("TileModuleInventory requires that the parent is an instance of ITileModuleProvider");
        }
    }

    @Override
    public void onJoinWorld()
    {

    }

    @Override
    public void onParentChange()
    {

    }

    @Override
    public void onLeaveWorld()
    {

    }

    @Override
    public ITileModuleProvider getParent()
    {
        return (ITileModuleProvider) host;
    }
}
