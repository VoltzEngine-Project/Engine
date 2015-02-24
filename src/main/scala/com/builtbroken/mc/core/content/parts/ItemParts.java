package com.builtbroken.mc.core.content.parts;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.registry.implement.IRegistryInit;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Created by robert on 2/23/2015.
 */
public class ItemParts extends Item implements IRegistryInit
{
    public ItemParts()
    {
        this.setCreativeTab(CreativeTabs.tabMaterials);
    }

    @Override
    public void onRegistered()
    {
        for (EnumParts part : EnumParts.values())
        {
            OreDictionary.registerOre(part.oreName, new ItemStack(Engine.itemCraftingParts, 1, part.ordinal()));
        }
    }

    @Override
    public void onClientRegistered()
    {

    }

    public static enum EnumParts
    {
        BASIC_CIRCUIT("circuitBasic", "circuit.t1");

        public final String oreName;
        public final String name;

        private EnumParts(String oreName, String name)
        {
            this.oreName = oreName;
            this.name = name;
        }
    }
}
