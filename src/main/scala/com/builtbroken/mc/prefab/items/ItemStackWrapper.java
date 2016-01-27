package com.builtbroken.mc.prefab.items;

import com.builtbroken.mc.prefab.inventory.InventoryUtility;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Created by robert on 1/10/2015.
 */
public class ItemStackWrapper
{
    public final ItemStack itemStack;
    public boolean meta_compare = true;
    public boolean nbt_compare = true;
    public boolean stack_size = false;

    public ItemStackWrapper(ItemStack itemStack)
    {
        if (itemStack != null)
        {
            this.itemStack = itemStack.copy();
            if (!itemStack.getHasSubtypes())
                meta_compare = false;
            if (itemStack.getTagCompound() == null)
                nbt_compare = false;
        }
        else
        {
            this.itemStack = null;
            meta_compare = false;
            nbt_compare = false;
        }
    }

    public ItemStackWrapper(Item item)
    {
        this.itemStack = new ItemStack(item);
        this.meta_compare = false;
        this.nbt_compare = false;
    }

    public ItemStackWrapper(Block block)
    {
        this.itemStack = new ItemStack(block);
        this.meta_compare = false;
        this.nbt_compare = false;
    }

    public ItemStackWrapper setMetaCompare(boolean b)
    {
        this.meta_compare = b;
        return this;
    }

    public ItemStackWrapper setNBTCompare(boolean b)
    {
        this.nbt_compare = b;
        return this;
    }

    public ItemStackWrapper setStackCompare(boolean b)
    {
        this.stack_size = b;
        return this;
    }

    @Override
    public boolean equals(Object object)
    {
        boolean item = false;
        boolean meta = !meta_compare;
        boolean nbt = !nbt_compare;
        boolean size = !stack_size;
        if (itemStack == null || object == null)
        {
            return false;
        }
        else if (object instanceof ItemStack || object instanceof ItemStackWrapper)
        {
            ItemStack i = object instanceof ItemStack ? (ItemStack) object : ((ItemStackWrapper) object).itemStack;
            item = itemStack.getItem() == i.getItem();
            if (meta_compare)
                meta = itemStack.getItemDamage() == i.getItemDamage();
            if (stack_size)
                size = itemStack.stackSize == i.stackSize;
            if (nbt_compare)
            {
                nbt = InventoryUtility.doesStackNBTMatch(itemStack, i);
            }
        }
        else if (!meta_compare && !nbt_compare && !stack_size)
        {
            if (object instanceof Item)
            {
                item = itemStack.getItem() == object;
            }
            else if (object instanceof Block)
            {
                item = itemStack.getItem() == Item.getItemFromBlock((Block) object);
            }
        }

        return item && meta && nbt && size;
    }

    @Override
    public int hashCode()
    {

        if (itemStack != null)
        {
            int hashCode = 1;
            //http://stackoverflow.com/questions/9648305/creating-a-hashcode-method-java
            hashCode = 31 * hashCode + itemStack.getItem().hashCode();
            if (meta_compare)
                hashCode = 31 * hashCode + itemStack.getItemDamage();
            if (stack_size)
                hashCode = 31 * hashCode + itemStack.stackSize;
            if (nbt_compare)
                hashCode = 31 * hashCode + (itemStack.getTagCompound() != null ? itemStack.getTagCompound().hashCode() : 0);
            return hashCode;
        }
        else
        {
            return super.hashCode();
        }
    }

    @Override
    public String toString()
    {
        return "ItemStackWrapper[" + itemStack + "]";
    }
}
