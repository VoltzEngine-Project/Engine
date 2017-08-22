package com.builtbroken.mc.framework.multiblock;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Created by Dark on 8/18/2015.
 */
public class ItemBlockMulti extends ItemBlock
{
    public ItemBlockMulti(Block block)
    {
        super(block);
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean inUse)
    {
        if (stack.getItem() == this)
        {
            stack.setCount(0);
            if (entity instanceof EntityPlayer)
            {
                ((EntityPlayer) entity).inventory.setInventorySlotContents(slot, null);
                ((EntityPlayer) entity).inventoryContainer.detectAndSendChanges();
            }
        }
    }
}
