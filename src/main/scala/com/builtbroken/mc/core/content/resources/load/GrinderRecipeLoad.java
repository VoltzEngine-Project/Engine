package com.builtbroken.mc.core.content.resources.load;

import com.builtbroken.mc.api.recipe.MachineRecipeType;
import com.builtbroken.mc.core.content.resources.DefinedGenItems;
import com.builtbroken.mc.core.content.resources.GenMaterial;
import com.builtbroken.mc.lib.helper.LanguageUtility;
import com.builtbroken.mc.framework.recipe.item.MRItemStack;
import com.builtbroken.mc.framework.recipe.item.MRLoaderItemStack;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Loadable that handles dust recipes for machines
 * Created by robert on 1/10/2015.
 */
@Deprecated //TODO transition to JSON
public class GrinderRecipeLoad extends MRLoaderItemStack
{
    public GrinderRecipeLoad()
    {
        super(MachineRecipeType.ITEM_GRINDER.INTERNAL_NAME);
    }

    @Override
    protected void generateRecipes(List<MRItemStack> recipes)
    {
        recipes.add(newRecipe(Blocks.sand).addInputOption(Blocks.stone).addInputOption(Blocks.cobblestone));
        recipes.add(newRecipe(new ItemStack(Blocks.sand, 4)).addInputOption(Blocks.sandstone));

        //Generate ore dictionary recipes for impure dust
        if (DefinedGenItems.DUST_IMPURE.item != null)
        {
            for (GenMaterial mat : GenMaterial.values())
            {
                if (mat != GenMaterial.UNKNOWN && !DefinedGenItems.DUST_IMPURE.ignoreMaterials.contains(mat))
                {
                    final String type = LanguageUtility.capitalizeFirst(mat.name().toLowerCase());

                    //Ore -> Dust
                    //TODO add output of tiny dust, and stone dust
                    processOreRecipes(recipes, DefinedGenItems.DUST_IMPURE.stack(mat), "ore" + type);

                    if (DefinedGenItems.RUBBLE.item != null)
                    {
                        //TODO add output of tiny dust, and stone dust
                        recipes.add(newRecipe(DefinedGenItems.DUST_IMPURE.stack(mat, 2)).addInputOption(DefinedGenItems.RUBBLE.stack(mat)));
                    }
                }
            }
        }

        if (DefinedGenItems.DUST.item != null)
        {
            for (GenMaterial mat : GenMaterial.values())
            {
                if (mat != GenMaterial.UNKNOWN && !DefinedGenItems.DUST.ignoreMaterials.contains(mat))
                {
                    final String type = LanguageUtility.capitalizeFirst(mat.name().toLowerCase());

                    //Ingot -> Dust
                    processOreRecipes(recipes, DefinedGenItems.DUST.stack(mat), "ingot" + type);

                    if (DefinedGenItems.PLATE.item != null)
                    {
                        recipes.add(newRecipe(DefinedGenItems.DUST.stack(mat)).addInputOption(DefinedGenItems.PLATE.stack(mat)));
                    }

                    if (DefinedGenItems.ROD.item != null)
                    {
                        recipes.add(newRecipe(DefinedGenItems.DUST.stack(mat)).addInputOption(DefinedGenItems.ROD.stack(mat)));
                    }

                    if (DefinedGenItems.GEAR.item != null)
                    {
                        recipes.add(newRecipe(DefinedGenItems.DUST.stack(mat)).addInputOption(DefinedGenItems.GEAR.stack(mat)));
                    }

                    if (DefinedGenItems.AX_HEAD.item != null)
                    {
                        recipes.add(newRecipe(DefinedGenItems.DUST.stack(mat, 3)).addInputOption(DefinedGenItems.AX_HEAD.stack(mat)));
                    }

                    if (DefinedGenItems.SHOVEL_HEAD.item != null)
                    {
                        recipes.add(newRecipe(DefinedGenItems.DUST.stack(mat)).addInputOption(DefinedGenItems.SHOVEL_HEAD.stack(mat)));
                    }

                    if (DefinedGenItems.SWORD_BLADE.item != null)
                    {
                        recipes.add(newRecipe(DefinedGenItems.DUST.stack(mat, 2)).addInputOption(DefinedGenItems.SWORD_BLADE.stack(mat)));
                    }

                    if (DefinedGenItems.PICK_HEAD.item != null)
                    {
                        recipes.add(newRecipe(DefinedGenItems.DUST.stack(mat, 3)).addInputOption(DefinedGenItems.PICK_HEAD.stack(mat)));
                    }

                    if (DefinedGenItems.HOE_HEAD.item != null)
                    {
                        recipes.add(newRecipe(DefinedGenItems.DUST.stack(mat, 2)).addInputOption(DefinedGenItems.HOE_HEAD.stack(mat)));
                    }
                }
            }
        }
    }
}
