package net.minecraft.src.universalelectricity.components;

import java.util.ArrayList;

import net.minecraft.src.ItemStack;

public class ItemCircuit extends UCItem
{
    private String[] names = new String[] {"Basic Circuit", "Adavanced Circuit", "Elite Circuit"};

    public ItemCircuit(int id, int texture)
    {
        super("Circuit", id, texture);
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
        return names[itemstack.getItemDamage()];
    }

    @Override
	public void addCreativeItems(ArrayList itemList)
    {
        for (int i = 0; i < names.length; i++)
        {
            itemList.add(new ItemStack(this, 1, i));
        }
    }
}
