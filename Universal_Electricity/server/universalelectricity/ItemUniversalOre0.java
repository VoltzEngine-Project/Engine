package net.minecraft.src.universalelectricity;

import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;

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
        return (new StringBuilder())
                .append(super.getItemName())
                .append(".")
                .append(UniversalElectricity.BlockOre[0].ores[itemstack.getItemDamage()].name)
                .toString();
    }
	
}
