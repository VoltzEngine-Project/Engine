package com.builtbroken.mc.framework.mod;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.prefab.inventory.InventoryUtility;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
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
        //TODO maybe cache?

        for (Object anItemRegistry : Item.itemRegistry)
        {
            Item item = (Item) anItemRegistry;

            if (item != null)
            {
                for (CreativeTabs tab : item.getCreativeTabs())
                {
                    if (tab == this)
                    {
                        List temp_list = new ArrayList();
                        item.getSubItems(item, this, temp_list);
                        for (Object o : temp_list)
                        {
                            if (o instanceof ItemStack)
                            {
                                if (((ItemStack) o).getItem() != null)
                                {
                                    list.add(o);
                                }
                                else
                                {
                                    Engine.logger().error("Item: " + item + "  attempted to add a stack with a null item to creative tab " + this);
                                }
                            }
                            else
                            {
                                Engine.logger().error("Item: " + item + "  attempted to add a non ItemStack to creative tab " + this);
                            }
                        }
                    }
                }
            }
        }

        if (this.func_111225_m() != null)
        {
            this.addEnchantmentBooksToList(list, this.func_111225_m());
        }

        if (itemSorter != null && !list.isEmpty())
        {
            Collections.sort(list, itemSorter);
        }
    }

    @Override
    public ItemStack getIconItemStack()
    {
        if (itemStack == null || itemStack.getItem() == null)
        {
            //Display error for devs to see
            if (itemStack == null)
            {
                Engine.logger().error("ItemStack used for creative tab " + this.getTabLabel() + " is null");
            }
            else
            {
                Engine.logger().error("ItemStack used for creative tab " + this.getTabLabel() + " contains a null Item reference");
            }

            //Attempt to use a random item in the tab
            List list = new ArrayList();
            displayAllReleventItems(list);
            for (Object object : list)
            {
                if (object instanceof ItemStack)
                {
                    itemStack = (ItemStack) object;
                    return itemStack;
                }
            }

            //If that fails set to redstone block as backup
            itemStack = new ItemStack(Blocks.redstone_block);
        }
        return itemStack;
    }

    @Override
    public Item getTabIconItem()
    {
        return getIconItemStack().getItem();
    }

    /**
     * Helper method to add the item and it's sub types to a list
     *
     * @param list
     * @param item
     */
    protected boolean add(List list, Item item)
    {
        if (item != null)
        {
            List list2 = new ArrayList();
            item.getSubItems(item, this, list2);
            list.addAll(list2);
            return true;
        }
        return false;
    }

    protected boolean add(List list, String name)
    {
        if (!addItem(list, name))
        {
            return addBlock(list, name);
        }
        return true;
    }

    protected boolean addItem(List list, String name)
    {
        Item item = InventoryUtility.getItem(name);
        if (item != null)
        {
            return add(list, item);
        }
        else if (Engine.runningAsDev)
        {
            Engine.logger().error("Failed to locate '" + name + "' to add to creative tab");
        }
        return false;
    }

    protected boolean addBlock(List list, String name)
    {
        Block block = InventoryUtility.getBlock(name);
        if (block != null)
        {
            return add(list, block);
        }
        else if (Engine.runningAsDev)
        {
            Engine.logger().error("Failed to locate '" + name + "' to add to creative tab");
        }
        return false;
    }

    /**
     * Helper method to add the item and it's sub types to a list
     *
     * @param list
     * @param block
     */
    protected boolean add(List list, Block block)
    {
        if (block != null)
        {
            Item item = Item.getItemFromBlock(block);
            if (item != null)
            {
                List list2 = new ArrayList();
                block.getSubBlocks(item, this, list2);
                list.addAll(list2);
                return true;
            }
            else if (Engine.runningAsDev)
            {
                Engine.logger().error("Block: " + block + " does not have an item so can not be displayed in creative tab");
            }
        }
        return false;
    }

    public static abstract class ItemSorter implements Comparator
    {
        @Override
        public int compare(Object o1, Object o2)
        {
            if (o1 instanceof ItemStack && o2 instanceof ItemStack)
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
