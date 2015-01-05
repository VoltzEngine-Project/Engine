package com.builtbroken.mc.lib.mod;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Prefab creative tab to either create a fast creative tab or reduce code
 * need to make a more complex tab
 * Created by robert on 11/25/2014.
 */
public class ModCreativeTab extends CreativeTabs
{
    public ItemStack itemStack = null;
    public Comparator itemSorter = null;

    public ModCreativeTab(String name)
    {
        super(name);
    }

    public ModCreativeTab(String name, Block block)
    {
        super(name);
        this.itemStack = new ItemStack(block);
    }

    public ModCreativeTab(String name, Item item)
    {
        super(name);
        this.itemStack = new ItemStack(item);
    }

    public ModCreativeTab(String name, ItemStack stack)
    {
        super(name);
        this.itemStack = stack;
    }

    @Override
    public void displayAllReleventItems(List list)
    {
        super.displayAllReleventItems(list);

        if(itemSorter != null && !list.isEmpty())
        {
            Collections.sort(list, itemSorter);
        }
    }

    @Override
    public ItemStack getIconItemStack()
    {
        if (itemStack == null)
        {
            itemStack = new ItemStack(Items.iron_door);
        }
        return itemStack;
    }

    @Override
    public Item getTabIconItem()
    {
        if (itemStack == null)
        {
            itemStack = new ItemStack(Items.iron_door);
        }
        return itemStack.getItem();
    }

    public static abstract class ItemSorter implements Comparator
    {
        @Override
        public int compare(Object o1, Object o2)
        {
            if(o1 instanceof ItemStack && o2 instanceof ItemStack)
            {
                return compareItem((ItemStack) o1, (ItemStack) o2);
            }
            return -1;
        }

        public abstract int compareItem(ItemStack o1, ItemStack o2);
    }

    public static class NameSorter extends ItemSorter
    {
        @Override
        public int compareItem(ItemStack a, ItemStack b)
        {
            return getLabel(a).compareToIgnoreCase(getLabel(b));
        }

        public String getLabel(ItemStack stack)
        {
            return stack.getDisplayName();
        }
    }
}
