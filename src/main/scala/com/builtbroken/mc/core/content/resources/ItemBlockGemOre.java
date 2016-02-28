package com.builtbroken.mc.core.content.resources;

import com.builtbroken.mc.core.References;
import com.builtbroken.mc.core.content.resources.gems.GemOres;
import com.builtbroken.mc.lib.helper.LanguageUtility;
import com.builtbroken.mc.prefab.tile.item.ItemBlockMetadata;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

/**
 * @author - Kolatra
 */
public class ItemBlockGemOre extends ItemBlockMetadata
{
    public ItemBlockGemOre(Block block)
    {
        super(block);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack)
    {
        if (itemstack.getItemDamage() < GemOres.values().length)
        {
            return "tile." + References.PREFIX + "ore"+ LanguageUtility.capitalizeFirst(GemOres.values()[itemstack.getItemDamage()].name().toLowerCase());
        }
        return super.getUnlocalizedName(itemstack);
    }
}
