package com.builtbroken.mc.core.resources.content;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import com.builtbroken.mc.prefab.tile.ItemBlockMetadata;
import com.builtbroken.mc.core.References;

/**
 * Created by robert on 11/25/2014.
 */
public class ItemBlockOre extends ItemBlockMetadata
{
    public ItemBlockOre(Block block)
    {
        super(block);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack)
    {
        String s = OreDictionary.getOreName(OreDictionary.getOreIDs(new ItemStack(this, 1, itemstack.getItemDamage()))[0]);
        if(s != null && !s.isEmpty())
        {
            return "tile." + References.PREFIX + s;
        }
        return super.getUnlocalizedName(itemstack);
    }
}
