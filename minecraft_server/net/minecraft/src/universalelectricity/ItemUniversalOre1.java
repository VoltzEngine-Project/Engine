package net.minecraft.src.universalelectricity;

import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.universalelectricity.ore.UEOreManager;

public class ItemUniversalOre1 extends ItemBlock
{
    public ItemUniversalOre1(int id)
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
                .append(UEOreManager.BlockOre[1].ores[itemstack.getItemDamage()].name)
                .toString();
    }
}
