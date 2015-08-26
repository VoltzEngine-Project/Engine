package com.builtbroken.mc.core.content.resources;

import com.builtbroken.mc.core.References;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

/**
 * Sheet metal item used only in crafting more complex recipes
 * Created by Dark on 8/25/2015.
 */
public class ItemSheetMetal extends Item
{
    public ItemSheetMetal()
    {
        this.setHasSubtypes(true);
        this.setUnlocalizedName(References.PREFIX + "sheetMetal");
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        if (stack.getItemDamage() >= 0 && stack.getItemDamage() < SheetMetal.values().length)
            return super.getUnlocalizedName() + "." + SheetMetal.values()[stack.getItemDamage()].name;
        return super.getUnlocalizedName();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list)
    {
        for (SheetMetal sheet : SheetMetal.values())
        {
            list.add(new ItemStack(item, 1, sheet.ordinal()));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister reg)
    {
        for (SheetMetal sheet : SheetMetal.values())
        {
            sheet.icon = reg.registerIcon(References.PREFIX + sheet.name);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int meta)
    {
        if (meta >= 0 && meta < SheetMetal.values().length)
        {
            return SheetMetal.values()[meta].icon;
        }
        return SheetMetal.FULL.icon;
    }

    public enum SheetMetal
    {
        FULL("metalSheet"),
        HALF("metalSheet_half"),
        QUARTER("metalSheet_quarter"),
        EIGHTH("metalSheet_eighth"),
        THIRD("metalSheet_third"),
        TRIANGLE("metalSheetTriangle"),
        CONE("metalCone");

        @SideOnly(Side.CLIENT)
        public IIcon icon;

        public final String name;

        SheetMetal(String name)
        {
            this.name = name;
        }
    }
}
