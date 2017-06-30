package com.builtbroken.mc.core.content.resources.load;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.content.resources.DefinedGenItems;
import com.builtbroken.mc.core.content.resources.GenMaterial;
import com.builtbroken.mc.lib.helper.LanguageUtility;
import com.builtbroken.mc.lib.helper.recipe.OreNames;
import com.builtbroken.mc.lib.mod.loadable.AbstractLoadable;
import com.builtbroken.mc.lib.recipe.item.RecipeTool;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.EnumSet;

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
                GameRegistry.addRecipe(new RecipeTool(new ItemStack(DefinedGenItems.GEAR.item, 2, GenMaterial.WOOD.ordinal()),
                        "WH", "DC",
                        'W', OreNames.WOOD,
                        'H', Engine.itemSimpleCraftingTools.getHammer(),
                        'C', Engine.itemSimpleCraftingTools.getChisel(),
                        'D', Engine.itemSimpleCraftingTools.getDrill()));

                GameRegistry.addRecipe(new RecipeTool(new ItemStack(DefinedGenItems.GEAR.item, 2, GenMaterial.WOOD.ordinal()),
                        "HS ", "SWS", " SC",
                        'W', OreNames.WOOD,
                        'S', OreNames.WOOD_STICK,
                        'H', Engine.itemSimpleCraftingTools.getHammer(),
                        'C', Engine.itemSimpleCraftingTools.getChisel()));

                GameRegistry.addRecipe(new RecipeTool(new ItemStack(DefinedGenItems.GEAR.item, 1, GenMaterial.STONE.ordinal()),
                        "WH", "DC",
                        'W', OreNames.STONE,
                        'H', Engine.itemSimpleCraftingTools.getHammer(),
                        'C', Engine.itemSimpleCraftingTools.getChisel(),
                        'D', Engine.itemSimpleCraftingTools.getDrill()));

                EnumSet<GenMaterial> mats = EnumSet.allOf(GenMaterial.class);
                mats.remove(GenMaterial.UNKNOWN);

                for (GenMaterial material : mats)
                {
                    if (OreDictionary.getOres("ingot" + LanguageUtility.capitalizeFirst(material.name().toLowerCase()), false).size() > 0 && !DefinedGenItems.GEAR.ignoreMaterials.contains(material))
                    {
                        //TODO change tools to iron
                        GameRegistry.addRecipe(new RecipeTool(new ItemStack(DefinedGenItems.GEAR.item, 1, material.ordinal()),
                                "WH", "DC",
                                'W', "ingot" + LanguageUtility.capitalizeFirst(material.name().toLowerCase()),
                                'H', Engine.itemSimpleCraftingTools.getHammer(),
                                'C', Engine.itemSimpleCraftingTools.getChisel(),
                                'D', Engine.itemSimpleCraftingTools.getDrill()));
                    }
                }
            }
        }
    }
}
