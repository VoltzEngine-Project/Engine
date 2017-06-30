package com.builtbroken.mc.core.content.debug;

import com.builtbroken.mc.core.References;
import com.builtbroken.mc.imp.transform.vector.Pos;
import com.builtbroken.mc.prefab.tile.Tile;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;

/**
 * Inventory tile that provides a single infinite stack for automation testing
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 11/15/2016.
 */
public class TileInfInv extends Tile implements IInventory
{
    public ItemStack slotStack = null;

    public TileInfInv()
    {
        super("tileInfInventory", Material.iron);
        this.hardness = -1;
        this.resistance = -1;
        this.setTextureName(References.PREFIX + "infiniteInventory");
    }

    @Override
    protected boolean onPlayerRightClick(EntityPlayer player, int side, Pos hit)
    {
        if (isServer())
        {
            if (player.getHeldItem() != null)
            {
                if (player.capabilities.isCreativeMode)
                {
                    if (player.isSneaking() && player.getHeldItem().getItem() == Items.stick)
                    {
                        slotStack = null;
                        player.addChatComponentMessage(new ChatComponentText("SlotStack = " + slotStack));
                    }
                    else
                    {
                        setInventorySlotContents(0, player.getHeldItem());
                    }
                }
            }
            else
            {
                player.addChatComponentMessage(new ChatComponentText("SlotStack = " + slotStack));
            }
        }
        return true;
    }

    @Override
    public Tile newTile()
    {
        return new TileInfInv();
    }

    @Override
    public int getSizeInventory()
    {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int slot)
    {
        return slot == 0 ? slotStack : null;
    }

    @Override
    public ItemStack decrStackSize(int slot, int a)
    {
        if (slot == 0 && slotStack != null)
        {
            ItemStack stack = slotStack.copy();
            stack.stackSize -= a;
            if (stack.stackSize <= 0)
            {
                return null;
            }
            return stack;
        }
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot)
    {
        return slot == 0 ? slotStack : null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack)
    {
        if (slot == 0 && stack != null)
        {
            slotStack = stack.copy();
            slotStack.stackSize = slotStack.getMaxStackSize();
        }
    }

    @Override
    public String getInventoryName()
    {
        return "Inf Inventory";
    }

    @Override
    public boolean hasCustomInventoryName()
    {
        return true;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return true;
    }

    @Override
    public void openInventory()
    {

    }

    @Override
    public void closeInventory()
    {

    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack)
    {
        return slot == 0 && stack != null;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        if (nbt.hasKey("slotStack"))
        {
            slotStack = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("slotStack"));
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        if (slotStack != null)
        {
            nbt.setTag("slotStack", slotStack.writeToNBT(new NBTTagCompound()));
        }
    }
}
