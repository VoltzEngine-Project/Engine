package com.builtbroken.mc.prefab.items;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by robert on 1/10/2015.
 */
public class ItemStackList extends ArrayList<ItemStackWrapper>
{
    public void add(ItemStack stack)
    {
        super.add(new ItemStackWrapper(stack));
    }

    public void add(Collection<ItemStack> stacks)
    {
        for(ItemStack stack : stacks)
        {
            add(stack);
        }
    }

    public List<ItemStack> toItemStack()
    {
        List<ItemStack> list = new ArrayList();
        for(ItemStackWrapper wrapper : this)
        {
            list.add(wrapper.itemStack);
        }
        return list;
    }
}
