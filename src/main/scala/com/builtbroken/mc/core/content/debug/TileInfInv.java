package com.builtbroken.mc.core.content.debug;

import com.builtbroken.mc.api.tile.provider.IInventoryProvider;
import com.builtbroken.mc.codegen.annotations.TileWrapped;
import com.builtbroken.mc.core.References;
import com.builtbroken.mc.framework.block.imp.IActivationListener;
import com.builtbroken.mc.framework.logic.TileNode;
import com.builtbroken.mc.prefab.inventory.BasicInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Inventory tile that provides a single infinite stack for automation testing
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 11/15/2016.
 */
@TileWrapped(className = "TileEntityWrappedInfInv", wrappers = "ExternalInventory")
public class TileInfInv extends TileNode implements IInventoryProvider, IActivationListener //TODO port as an SBM mod for greater reuse
{
    public ItemStack slotStack = null;

    public IInventory inv;

    public TileInfInv()
    {
        super("tileInfInventory", References.DOMAIN);
    }

    @Override
    public boolean onPlayerActivated(EntityPlayer player, int side, float hitX, float hitY, float hitZ)
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
                        slotStack = player.getHeldItem().copy();
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
    public boolean canStore(ItemStack stack, ForgeDirection side)
    {
        return false; //can not insert items
    }

    @Override
    public boolean canRemove(ItemStack stack, ForgeDirection side) //TODO maybe implement side configs for testing
    {
        return true; //can always remove
    }

    @Override
    public void load(NBTTagCompound nbt)
    {
        super.load(nbt);
        if (nbt.hasKey("slotStack"))
        {
            slotStack = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("slotStack"));
        }
    }

    @Override
    public NBTTagCompound save(NBTTagCompound nbt)
    {
        super.save(nbt);
        if (slotStack != null)
        {
            nbt.setTag("slotStack", slotStack.writeToNBT(new NBTTagCompound()));
        }
        return nbt;
    }

    @Override
    public IInventory getInventory()
    {
        if (inv == null)
        {
            inv = new BasicInventory(1)
            {
                public ItemStack getStackInSlot(int slot)
                {
                    return slotStack;
                }
            };
        }
        return inv;
    }
}
