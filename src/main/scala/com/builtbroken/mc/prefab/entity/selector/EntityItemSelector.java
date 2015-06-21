package com.builtbroken.mc.prefab.entity.selector;

import com.builtbroken.mc.prefab.items.ItemStackWrapper;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

/**
 * Created by robert on 2/10/2015.
 */
public class EntityItemSelector extends EntitySelector
{
    public ItemStackWrapper wrapper;
    public ArrayList<ItemStackWrapper> items;

    public EntityItemSelector()
    {

    }

    public EntityItemSelector(ItemStackWrapper wrapper)
    {
        this.wrapper = wrapper;
    }

    public EntityItemSelector(Object... array)
    {
        for (Object obj : array)
        {
            if (obj instanceof ItemStackWrapper)
            {
                items.add((ItemStackWrapper) obj);
            }
            else if (obj instanceof ItemStack)
            {
                items.add(new ItemStackWrapper((ItemStack) obj));
            }
            else if (obj instanceof Item)
            {
                items.add(new ItemStackWrapper((Item) obj));
            }
            else if (obj instanceof Block)
            {
                items.add(new ItemStackWrapper((Block) obj));
            }
            else
            {
                throw new IllegalArgumentException("EntityItemSelector: " + obj + " is an invalid sorting type");
            }
        }
    }

    @Override
    public boolean isEntityApplicable(Entity entity)
    {
        if (entity instanceof EntityItem && ((EntityItem) entity).getEntityItem() != null)
        {
            if (wrapper != null)
            {
                return wrapper.equals(((EntityItem) entity).getEntityItem());
            }
            return items == null || items.contains(new ItemStackWrapper(((EntityItem) entity).getEntityItem()));
        }
        return false;
    }
}
