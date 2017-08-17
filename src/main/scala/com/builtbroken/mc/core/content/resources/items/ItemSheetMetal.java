package com.builtbroken.mc.core.content.resources.items;

import com.builtbroken.mc.core.Engine;
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
@Deprecated //TODO move to JSON
public class ItemSheetMetal extends Item
{
    public ItemSheetMetal()
    {
        this.setHasSubtypes(true);
        this.setUnlocalizedName(References.PREFIX + "sheetMetal");
        this.setCreativeTab(CreativeTabs.tabMaterials);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        if (stack.getItemDamage() >= 0 && stack.getItemDamage() < SheetMetal.values().length)
        {
            return super.getUnlocalizedName() + "." + SheetMetal.values()[stack.getItemDamage()].name;
        }
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
        /** Normal full 1 meter sized plate */
        FULL("metalSheet", "sheetMetal"),
        /** 1/2 of a normal plate */
        HALF("metalSheet_half", "halfSheetMetal"),
        /** 1/4 of a normal plate */
        QUARTER("metalSheet_quarter", "quarterSheetMetal"),
        /** 1/8 of a normal plate */
        EIGHTH("metalSheet_eighth", "eighthSheetMetal"),
        /** 1/3 of a normal plate */
        THIRD("metalSheet_third", "shirtSheetMetal"),
        /** Wedge or triangle shape equal to half of a plate */
        TRIANGLE("metalSheetTriangle", "triangleSheetMetal"),
        /** Cone shape made from a triangle plate */
        CONE_MICRO("metalCone_micro", "sheetMicroConeMetal"),
        /** Cone shape made from 3 triangle plates */
        CONE_SMALL("metalCone_small", "sheetSmallConeMetal"),
        /** Slightly curved */
        CURVED_1("metalSheet_curved1", "sheetCurvedOneMetal"),
        /** Slightly more curved */
        CURVED_2("metalSheet_curved2", "sheetCurvedTwoMetal"),
        /** Decently curved */
        CURVED_3("metalSheet_curved3", "sheetCurvedThreeMetal"),
        /** Almost a 1/4 cylinder shape */
        CURVED_4("metalSheet_curved4", "sheetCurvedFourMetal"),
        /** Small tube shape about 1/3 of a meter diameter */
        SMALL_CYLINDER("metalSheet_smallCylinder", "sheetSmallCylinderMetal"),
        /** Half of the cylinder sheet metal shape */
        HALF_CYLINDER("metalSheet_halfCylinder", "halfSheetCylinderMetal"),
        /** Average tube shape about 3/4 of a meter diameter */
        CYLINDER("metalSheet_cylinder", "sheetCylinderMetal"),
        /** Small bolt that is pressed together to bind sheet metal together */
        RIVETS("rivets", "rivetSheetMetal"),
        /** Triangle cut in half */
        FIN_MICRO("metalSheet_microFin", "sheetMicroFinMetal"),
        /** Triangle cut in half */
        FIN_SMALL("metalSheet_smallFin", "sheetSmallFinMetal"),
        /** Cone shape made from 3 triangle plates */
        CONE_MEDIUM("metalCone_medium", "sheetMediumConeMetal"),;

        @SideOnly(Side.CLIENT)
        public IIcon icon;

        public final String name;
        public final String oreName;

        SheetMetal(String name, String oreName)
        {
            this.name = name;
            this.oreName = oreName;
        }

        public ItemStack stack()
        {
            return stack(1);
        }

        public ItemStack stack(int size)
        {
            return new ItemStack(Engine.itemSheetMetal, size, ordinal());
        }
    }
}
