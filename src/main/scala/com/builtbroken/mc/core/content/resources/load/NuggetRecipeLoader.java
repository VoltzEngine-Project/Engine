package com.builtbroken.mc.core.content.resources.load;

import com.builtbroken.mc.core.content.resources.DefinedGenItems;
import com.builtbroken.mc.core.content.resources.GenMaterial;
import com.builtbroken.mc.lib.helper.LanguageUtility;
import com.builtbroken.mc.framework.mod.loadable.AbstractLoadable;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.EnumSet;

/**
 * Generates recipes for nuggets
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/29/2015.
 */
@Deprecated //TODO transition to JSON
public class NuggetRecipeLoader extends AbstractLoadable
{
    @Override
    public void postInit()
    {
        if (DefinedGenItems.NUGGET.item != null)
        {
            EnumSet<GenMaterial> mats = EnumSet.allOf(GenMaterial.class);
            mats.remove(GenMaterial.UNKNOWN);

            for (GenMaterial material : mats)
            {
                if (OreDictionary.getOres("ingot" + LanguageUtility.capitalizeFirst(material.name().toLowerCase()), false).size() > 0 && !DefinedGenItems.NUGGET.ignoreMaterials.contains(material))
                {
                    GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(DefinedGenItems.NUGGET.item, 9, material.ordinal()), "ingot" + LanguageUtility.capitalizeFirst(material.name().toLowerCase())));
                    if (DefinedGenItems.INGOT.item != null)
                    {
                        GameRegistry.addRecipe(new ShapedOreRecipe(DefinedGenItems.INGOT.stack(material), "nnn", "nnn", "nnn", 'n', new ItemStack(DefinedGenItems.NUGGET.item, 1, material.ordinal())));
                    }
                    else
                    {
                        GameRegistry.addRecipe(new ShapedOreRecipe(OreDictionary.getOres("ingot" + LanguageUtility.capitalizeFirst(material.name().toLowerCase())).get(0), "nnn", "nnn", "nnn", 'n', new ItemStack(DefinedGenItems.NUGGET.item, 1, material.ordinal())));
                    }
                }
            }
        }
    }
}
