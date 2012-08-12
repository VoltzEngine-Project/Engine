package net.minecraft.src.basiccomponents;

import java.util.List;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.ItemStack;

public class ItemCircuit extends BCItem
{
    private String[] names = new String[] {"Basic Circuit", "Adavanced Circuit", "Elite Circuit"};

    public ItemCircuit(int id, int texture)
    {
        super("Circuit", id, texture);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.setTabToDisplayOn(CreativeTabs.tabRedstone);
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
    public int getIconFromDamage(int i)
    {
        return this.iconIndex + i;
    }

    @Override
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
    	for(int i = 0; i < names.length; i++)
        {
    		par3List.add(new ItemStack(this, 1, i));
        }
    }
}
