package com.builtbroken.mc.core.content.resources;

import com.builtbroken.mc.lib.helper.LanguageUtility;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import com.builtbroken.mc.prefab.tile.item.ItemBlockMetadata;
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
        if(itemstack.getItemDamage() < MetallicOres.values().length)
        {
            return "tile." + References.PREFIX + "ore"+ LanguageUtility.capitalizeFirst(MetallicOres.values()[itemstack.getItemDamage()].name().toLowerCase());
        }
        return super.getUnlocalizedName(itemstack);
    }
}
