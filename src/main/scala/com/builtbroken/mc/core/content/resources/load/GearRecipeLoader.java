package com.builtbroken.mc.core.content.resources.load;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.content.resources.DefinedGenItems;
import com.builtbroken.mc.core.content.resources.GenMaterial;
import com.builtbroken.mc.lib.mod.loadable.AbstractLoadable;
import com.builtbroken.mc.prefab.recipe.item.RecipeTool;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/29/2015.
 */
public class GearRecipeLoader extends AbstractLoadable
{
    @Override
    public void postInit()
    {
        if (DefinedGenItems.GEAR.item != null)
        {
            if (Engine.itemSimpleCraftingTools != null)
            {
                GameRegistry.addRecipe(new RecipeTool(new ItemStack(DefinedGenItems.GEAR.item, 1, GenMaterial.WOOD.ordinal()),
                        "WH", "DC",
                        'W', "plankWood",
                        'H', Engine.itemSimpleCraftingTools.getHammer(),
                        'C', Engine.itemSimpleCraftingTools.getChisel(),
                        'D', Engine.itemSimpleCraftingTools.getDrill()));

                GameRegistry.addRecipe(new RecipeTool(new ItemStack(DefinedGenItems.GEAR.item, 1, GenMaterial.STONE.ordinal()),
                        "WH", "DC",
                        'W', "stone",
                        'H', Engine.itemSimpleCraftingTools.getHammer(),
                        'C', Engine.itemSimpleCraftingTools.getChisel(),
                        'D', Engine.itemSimpleCraftingTools.getDrill()));

                //TODO change tools to iron
                GameRegistry.addRecipe(new RecipeTool(new ItemStack(DefinedGenItems.GEAR.item, 1, GenMaterial.TIN.ordinal()),
                        "WH", "DC",
                        'W', "ingotTin",
                        'H', Engine.itemSimpleCraftingTools.getHammer(),
                        'C', Engine.itemSimpleCraftingTools.getChisel(),
                        'D', Engine.itemSimpleCraftingTools.getDrill()));

                //TODO change tools to iron
                GameRegistry.addRecipe(new RecipeTool(new ItemStack(DefinedGenItems.GEAR.item, 1, GenMaterial.COPPER.ordinal()),
                        "WH", "DC",
                        'W', "ingotCopper",
                        'H', Engine.itemSimpleCraftingTools.getHammer(),
                        'C', Engine.itemSimpleCraftingTools.getChisel(),
                        'D', Engine.itemSimpleCraftingTools.getDrill()));

                //TODO change tools to iron
                GameRegistry.addRecipe(new RecipeTool(new ItemStack(DefinedGenItems.GEAR.item, 1, GenMaterial.BRONZE.ordinal()),
                        "WH", "DC",
                        'W', "ingotBronze",
                        'H', Engine.itemSimpleCraftingTools.getHammer(),
                        'C', Engine.itemSimpleCraftingTools.getChisel(),
                        'D', Engine.itemSimpleCraftingTools.getDrill()));

                //TODO change tools to iron
                GameRegistry.addRecipe(new RecipeTool(new ItemStack(DefinedGenItems.GEAR.item, 1, GenMaterial.STEEL.ordinal()),
                        "WH", "DC",
                        'W', "ingotSteel",
                        'H', Engine.itemSimpleCraftingTools.getHammer(),
                        'C', Engine.itemSimpleCraftingTools.getChisel(),
                        'D', Engine.itemSimpleCraftingTools.getDrill()));

                //TODO change tools to iron
                GameRegistry.addRecipe(new RecipeTool(new ItemStack(DefinedGenItems.GEAR.item, 1, GenMaterial.IRON.ordinal()),
                        "WH", "DC",
                        'W', "ingotIron",
                        'H', Engine.itemSimpleCraftingTools.getHammer(),
                        'C', Engine.itemSimpleCraftingTools.getChisel(),
                        'D', Engine.itemSimpleCraftingTools.getDrill()));

                //TODO change tools to iron
                GameRegistry.addRecipe(new RecipeTool(new ItemStack(DefinedGenItems.GEAR.item, 1, GenMaterial.DIAMOND.ordinal()),
                        "WH", "DC",
                        'W', "gemDiamond",
                        'H', Engine.itemSimpleCraftingTools.getHammer(),
                        'C', Engine.itemSimpleCraftingTools.getChisel(),
                        'D', Engine.itemSimpleCraftingTools.getDrill()));
            }
        }
    }
}
