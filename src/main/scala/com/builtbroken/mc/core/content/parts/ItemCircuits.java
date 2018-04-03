package com.builtbroken.mc.core.content.parts;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.References;
import com.builtbroken.mc.core.registry.implement.IPostInit;
import com.builtbroken.mc.core.registry.implement.IRegistryInit;
import com.builtbroken.mc.lib.helper.recipe.OreNames;
import com.builtbroken.mc.framework.recipe.item.RecipeTool;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

/**
 * Item for circuit boards
 * Created by robert on 2/23/2015.
 */
@Deprecated //TODO move to JSON
public class ItemCircuits extends Item implements IRegistryInit, IPostInit
{
    public ItemCircuits()
    {
        this.setUnlocalizedName(References.PREFIX + "circuits");
        this.setCreativeTab(CreativeTabs.tabMaterials);
        this.setHasSubtypes(true);
    }

    @Override
    public void onPostInit()
    {
        if (Engine.itemSimpleCraftingTools != null)
        {
            GameRegistry.addRecipe(new RecipeTool(new ItemStack(Engine.itemCircuits), "wcw", "dpt", "wrw", 'w', OreNames.WIRE_TIN, 'p', OreNames.WOOD, 'c', Items.clay_ball, 'r', OreNames.REDSTONE, 'd', Engine.itemSimpleCraftingTools.getDrill(), 't', Engine.itemSimpleCraftingTools.getCutters()));
            GameRegistry.addRecipe(new RecipeTool(new ItemStack(Engine.itemCircuits, 1, 1), "wcw", "dpt", "wrw", 'w', OreNames.WIRE_COPPER, 'p', OreNames.PLATE_COPPER, 'c', Items.clay_ball, 'r', OreNames.REDSTONE, 'd', Engine.itemSimpleCraftingTools.getDrill(), 't', Engine.itemSimpleCraftingTools.getCutters()));
            GameRegistry.addRecipe(new RecipeTool(new ItemStack(Engine.itemCircuits, 1, 2), "wcw", "dpt", "wrw", 'w', OreNames.WIRE_GOLD, 'p', OreNames.PLATE_GOLD, 'c', Items.clay_ball, 'r', OreNames.REDSTONE, 'd', Engine.itemSimpleCraftingTools.getDrill(), 't', Engine.itemSimpleCraftingTools.getCutters()));

        }
        else
        {
            GameRegistry.addRecipe(new RecipeTool(new ItemStack(Engine.itemCircuits),
                    "wcw",
                    "rpr",
                    "wcw",
                    'w', OreNames.WIRE_TIN,
                    'p', OreNames.WOOD,
                    'c', "clay",
                    'r', OreNames.REDSTONE));
            GameRegistry.addRecipe(new RecipeTool(new ItemStack(Engine.itemCircuits, 1, 1),
                    "wcw",
                    "rpr",
                    "wcw",
                    'w', OreNames.WIRE_COPPER,
                    'p', OreNames.INGOT_IRON,
                    'c', "clay",
                    'r', OreNames.REDSTONE));
            GameRegistry.addRecipe(new RecipeTool(new ItemStack(Engine.itemCircuits, 1, 2),
                    "wcw",
                    "rpr",
                    "wcw",
                    'w', OreNames.WIRE_GOLD,
                    'p', OreNames.INGOT_GOLD,
                    'c', "clay",
                    'r', OreNames.REDSTONE));
        }
    }

    @Override
    public void onRegistered()
    {
        for (EnumCircuits part : EnumCircuits.values())
        {
            OreDictionary.registerOre(part.oreName, new ItemStack(Engine.itemCircuits, 1, part.ordinal()));
        }
    }

    @Override
    public void onClientRegistered()
    {

    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        if (stack.getItemDamage() >= 0 && stack.getItemDamage() < EnumCircuits.values().length)
        {
            return super.getUnlocalizedName() + "." + EnumCircuits.values()[stack.getItemDamage()].name;
        }
        return super.getUnlocalizedName();
    }

    @Override
    public void registerIcons(IIconRegister reg)
    {
        for (EnumCircuits part : EnumCircuits.values())
        {
            part.icon = reg.registerIcon(References.PREFIX + part.name);
        }
    }

    @Override
    public IIcon getIconFromDamage(int meta)
    {
        if (meta >= 0 && meta < EnumCircuits.values().length)
        {
            return EnumCircuits.values()[meta].icon;
        }
        return Items.string.getIconFromDamage(0);
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List items)
    {
        for (EnumCircuits part : EnumCircuits.values())
        {
            items.add(new ItemStack(item, 1, part.ordinal()));
        }
    }

    public enum EnumCircuits
    {
        BASIC_CIRCUIT("circuitBasic", "circuitBasic"),
        ADVANCED_CIRCUIT("circuitAdvanced", "circuitAdvanced"),
        ELITE_CIRCUIT("circuitElite", "circuitElite");

        public final String oreName;
        public final String name;
        protected IIcon icon;

        EnumCircuits(String oreName, String name)
        {
            this.oreName = oreName;
            this.name = name;
        }

        public static EnumCircuits get(int meta)
        {
            if (meta >= 0 && meta < values().length)
            {
                return values()[meta];
            }
            return null;
        }
    }
}
