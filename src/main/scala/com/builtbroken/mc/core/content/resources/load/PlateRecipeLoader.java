package com.builtbroken.mc.core.content.resources.load;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.content.resources.DefinedGenItems;
import com.builtbroken.mc.core.content.resources.GenMaterial;
import com.builtbroken.mc.lib.helper.LanguageUtility;
import com.builtbroken.mc.framework.mod.loadable.AbstractLoadable;
import com.builtbroken.mc.framework.recipe.item.RecipeTool;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;

import java.util.EnumSet;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/29/2015.
 */
@Deprecated //TODO transition to JSON
public class PlateRecipeLoader extends AbstractLoadable
{
    @Override
    public void postInit()
    {
        if (DefinedGenItems.PLATE.item != null)
        {
            if (Engine.itemSimpleCraftingTools != null)
            {
                GameRegistry.addRecipe(new RecipeTool(new ItemStack(DefinedGenItems.PLATE.item, 1, GenMaterial.WOOD.ordinal()),
                        "H ", "CW",
                        'W', "plankWood",
                        'H', Engine.itemSimpleCraftingTools.getHammer(),
                        'C', Engine.itemSimpleCraftingTools.getChisel()));
                GameRegistry.addRecipe(new RecipeTool(new ItemStack(DefinedGenItems.PLATE.item, 1, GenMaterial.STONE.ordinal()),
                        "H ", "CW",
                        'W', "stone",
                        'H', Engine.itemSimpleCraftingTools.getHammer(),
                        'C', Engine.itemSimpleCraftingTools.getChisel(),
                        'F', Engine.itemSimpleCraftingTools.getFile()));

                EnumSet<GenMaterial> mats = EnumSet.allOf(GenMaterial.class);
                mats.remove(GenMaterial.UNKNOWN);
                mats.remove(GenMaterial.WOOD);
                mats.remove(GenMaterial.STONE);

                for (GenMaterial material : mats)
                {
                    if (!DefinedGenItems.PLATE.ignoreMaterials.contains(material))
                    {
                        //TODO change to iron tools
                        GameRegistry.addRecipe(new RecipeTool(new ItemStack(DefinedGenItems.PLATE.item, 1, material.ordinal()),
                                "WH", "F ",
                                'W', "ingot" + LanguageUtility.capitalizeFirst(material.name().toLowerCase()),
                                'H', Engine.itemSimpleCraftingTools.getHammer(),
                                'F', Engine.itemSimpleCraftingTools.getFile()));
                    }
                }

            }
        }
    }
}
