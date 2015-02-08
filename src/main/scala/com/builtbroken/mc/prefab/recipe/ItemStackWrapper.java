package com.builtbroken.mc.prefab.recipe;

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
        if(itemStack != null)
        {
            this.itemStack = itemStack;
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
            item = itemStack.isItemEqual(i);
            if (meta_compare)
                meta = itemStack.getItemDamage() == i.getItemDamage();
            if (stack_size)
                size = itemStack.stackSize == i.stackSize;
            if (nbt_compare)
            {
                if (itemStack.getTagCompound() == null && i.getTagCompound() == null)
                {
                    nbt = true;
                }
                else if (itemStack.getTagCompound() != null && i.getTagCompound() == null)
                {
                    nbt = false;
                }
                else if (itemStack.getTagCompound() == null && i.getTagCompound() != null)
                {
                    nbt = false;
                } //TODO this may need to be manually done
                else if (itemStack.getTagCompound().equals(i.getTagCompound()))
                {
                    nbt = true;
                }
            }
        }
        else if (meta_compare == false && nbt_compare == false && stack_size == false)
        {
            if (object instanceof Item)
            {
                item = itemStack.getItem() == (Item) object;
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
        int hashCode = 1;
        if(itemStack != null)
        {
            //http://stackoverflow.com/questions/9648305/creating-a-hashcode-method-java
            hashCode = 31 * hashCode + itemStack.getItem().hashCode();
            hashCode = 31 * hashCode + itemStack.getItemDamage();
            hashCode = 31 * hashCode + itemStack.stackSize;
            hashCode = 31 * hashCode + (itemStack.getTagCompound() != null ? itemStack.getTagCompound().hashCode() : 0);
        }
        else
        {
            return super.hashCode();
        }
        return hashCode;
    }
}
