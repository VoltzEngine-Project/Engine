package com.builtbroken.mc.core.content.parts;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.References;
import com.builtbroken.mc.core.registry.implement.IPostInit;
import com.builtbroken.mc.core.registry.implement.IRegistryInit;
import com.builtbroken.mc.prefab.recipe.item.RecipeTool;
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
public class ItemCircuits extends Item implements IRegistryInit, IPostInit
{
    public ItemCircuits()
    {
        this.setUnlocalizedName(References.PREFIX + "circuits");
        this.setCreativeTab(CreativeTabs.tabMaterials);
    }

    @Override
    public void onPostInit()
    {
        if (Engine.itemSimpleCraftingTools != null)
        {
            GameRegistry.addRecipe(new RecipeTool(new ItemStack(Engine.itemCircuits), "wcw", "dpt", "wrw", 'w', "wireTin", 'p', "plankWood", 'c', Items.clay_ball, 'r', Items.redstone, 'd', Engine.itemSimpleCraftingTools.getDrill(), 't', Engine.itemSimpleCraftingTools.getCutters()));
            GameRegistry.addRecipe(new RecipeTool(new ItemStack(Engine.itemCircuits, 1), "wcw", "dpt", "wrw", 'w', "wireCopper", 'p', "plateCopper", 'c', Items.clay_ball, 'r', Items.redstone, 'd', Engine.itemSimpleCraftingTools.getDrill(), 't', Engine.itemSimpleCraftingTools.getCutters()));
            GameRegistry.addRecipe(new RecipeTool(new ItemStack(Engine.itemCircuits, 2), "wcw", "dpt", "wrw", 'w', "wireGold", 'p', "plateGold", 'c', Items.clay_ball, 'r', Items.redstone, 'd', Engine.itemSimpleCraftingTools.getDrill(), 't', Engine.itemSimpleCraftingTools.getCutters()));

        }
        else
        {
            GameRegistry.addRecipe(new RecipeTool(new ItemStack(Engine.itemCircuits), "wcw", "rpr", "wcw", 'w', "wireTin", 'p', "plankWood", 'c', "clay", 'r', Items.redstone));
            GameRegistry.addRecipe(new RecipeTool(new ItemStack(Engine.itemCircuits, 1), "wcw", "rpr", "wcw", 'w', "wireCopper", 'p', "ingotIron", 'c', "clay", 'r', Items.redstone));
            GameRegistry.addRecipe(new RecipeTool(new ItemStack(Engine.itemCircuits, 2), "wcw", "rpr", "wcw", 'w', "wireGold", 'p', "ingotGold", 'c', "clay", 'r', Items.redstone));

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
                return values()[meta];
            return null;
        }
    }
}
