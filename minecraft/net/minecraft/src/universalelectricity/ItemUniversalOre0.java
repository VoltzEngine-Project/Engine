package net.minecraft.src.universalelectricity;

import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.universalelectricity.ore.UEOreManager;

public class ItemUniversalOre0 extends ItemBlock
{
    public ItemUniversalOre0(int id)
    {
        super(id);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int damage)
    {
        return damage;
    }

    @Override
    public String getItemNameIS(ItemStack itemstack)
    {
    	if(UEOreManager.BlockOre[0].ores[itemstack.getItemDamage()] != null)
    	{
	        return (new StringBuilder())
	                .append(super.getItemName())
	                .append(".")
	                .append(UEOreManager.BlockOre[0].ores[itemstack.getItemDamage()].name)
	                .toString();
    	}
    	
    	return "";
    }
}
