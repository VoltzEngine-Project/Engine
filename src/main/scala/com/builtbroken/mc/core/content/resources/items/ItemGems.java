package com.builtbroken.mc.core.content.resources.items;

import com.builtbroken.mc.core.References;
import com.builtbroken.mc.core.content.resources.MetallicOres;
import com.builtbroken.mc.lib.helper.LanguageUtility;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

/**
 * @author - Kolatra
 */
public class ItemGems extends Item
{
    public IIcon[] icons = new IIcon[9];

    public ItemGems()
    {
        this.setHasSubtypes(true);
        setCreativeTab(CreativeTabs.tabMaterials);
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tabs, List list)
    {
        for (Gems gems : Gems.values())
        {
            list.add(new ItemStack(item, 1, gems.ordinal()));
        }
    }

    @SideOnly(Side.CLIENT) @Override
    public void registerIcons(IIconRegister register)
    {
        for (Gems gems : Gems.values())
        {
            icons[gems.ordinal()] = register.registerIcon(References.PREFIX + "_" + gems.name().toLowerCase() + "_gem");
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack)
    {
        if(itemstack.getItemDamage() < Gems.values().length)
        {
            return "item." + References.PREFIX + "gem"+ LanguageUtility.capitalizeFirst(Gems.values()[itemstack.getItemDamage()].name().toLowerCase());
        }
        return super.getUnlocalizedName(itemstack);
    }
}
