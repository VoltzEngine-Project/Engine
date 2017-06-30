package com.builtbroken.mc.core.content.resources.load;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.content.resources.DefinedGenItems;
import com.builtbroken.mc.core.content.resources.GenMaterial;
import com.builtbroken.mc.lib.helper.LanguageUtility;
import com.builtbroken.mc.lib.mod.loadable.AbstractLoadable;
import com.builtbroken.mc.lib.recipe.item.RecipeTool;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.EnumSet;

/**
 * Loads screw based recipes
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/29/2015.
 */
public class ScrewRecipeLoader extends AbstractLoadable
{
    @Override
    public void postInit()
    {
        if (DefinedGenItems.SCREW.item != null)
        {
            if (Engine.itemSimpleCraftingTools != null)
            {
                EnumSet<GenMaterial> mats = EnumSet.allOf(GenMaterial.class);
                mats.remove(GenMaterial.UNKNOWN);

                for (GenMaterial material : mats)
                {
                    if (OreDictionary.getOres("nugget" + LanguageUtility.capitalizeFirst(material.name().toLowerCase()), false).size() > 0 && !DefinedGenItems.SCREW.ignoreMaterials.contains(material))
                    {
                        //TODO change tools to iron
                        GameRegistry.addRecipe(new RecipeTool(new ItemStack(DefinedGenItems.SCREW.item, 1, material.ordinal()),
                                "WH", " F",
                                'W', "nugget" + LanguageUtility.capitalizeFirst(material.name().toLowerCase()),
                                'F', Engine.itemSimpleCraftingTools.getFile(),
                                'H', Engine.itemSimpleCraftingTools.getHammer()));
                    }
                }
            }
        }
    }
}
