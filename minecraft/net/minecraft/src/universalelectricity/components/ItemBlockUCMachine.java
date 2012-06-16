package net.minecraft.src.universalelectricity.components;

import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;

public class ItemBlockUCMachine extends ItemBlock
{
	public String[] names = {"Battery Box", "Coal Generator", "Electric Furnace"};
	
    public ItemBlockUCMachine(int id)
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
                .append(names[itemstack.getItemDamage()])
                .toString();
    }
}
