package com.builtbroken.mc.core.content.resources.load;

import com.builtbroken.mc.api.recipe.MachineRecipeType;
import com.builtbroken.mc.core.content.resources.DefinedGenItems;
import com.builtbroken.mc.core.content.resources.GenMaterial;
import com.builtbroken.mc.lib.helper.LanguageUtility;
import com.builtbroken.mc.prefab.recipe.fluid.MRFluidStack;
import com.builtbroken.mc.prefab.recipe.fluid.MRLoaderFluidStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Loadable for fluid smelting machine recipes
 * Created by Dark on 6/21/2015.
 */
public class FluidSmelterRecipeLoad extends MRLoaderFluidStack
{
    public static boolean loaded = false;

    public FluidSmelterRecipeLoad()
    {
        super(MachineRecipeType.FLUID_SMELTER.INTERNAL_NAME);
    }

    @Override
    protected void generateRecipes(List<MRFluidStack> recipes)
    {
        ///TODO add a config to not generate new fluids if they don't exist
        loaded = true;

        List<DefinedGenItems> items = new ArrayList();
        items.add(DefinedGenItems.DUST_IMPURE);
        items.add(DefinedGenItems.GEAR);
        items.add(DefinedGenItems.ROD);
        items.add(DefinedGenItems.PLATE);
        items.add(DefinedGenItems.SHOVEL_HEAD);

        for (GenMaterial material : GenMaterial.values())
        {
            if (material != GenMaterial.UNKNOWN && material != GenMaterial.WOOD)
            {
                material.moltenFluid = registerMoltenFluid("gold.molten");
                String type = LanguageUtility.capitalizeFirst(material.name().toLowerCase());
                processOreRecipes(recipes, material.moltenFluid, 2, "ore" + type);
                processOreRecipes(recipes, material.moltenFluid, 1, "ingot" + type);
                processOreRecipes(recipes, material.moltenFluid, 1, "dust" + type);

                for (DefinedGenItems genItem : items)
                {
                    if (genItem.item != null && !genItem.ignoreMaterials.contains(material))
                    {
                        recipes.add(newRecipe(material.moltenFluid, 1, genItem.stack(material)));
                    }
                }
                if (DefinedGenItems.AX_HEAD.item != null)
                {
                    recipes.add(newRecipe(material.moltenFluid, 3, DefinedGenItems.AX_HEAD.stack(material)));
                }

                if (DefinedGenItems.SWORD_BLADE.item != null)
                {
                    recipes.add(newRecipe(material.moltenFluid, 2, DefinedGenItems.SWORD_BLADE.stack(material)));
                }

                if (DefinedGenItems.PICK_HEAD.item != null)
                {
                    recipes.add(newRecipe(material.moltenFluid, 3, DefinedGenItems.PICK_HEAD.stack(material)));
                }

                if (DefinedGenItems.HOE_HEAD.item != null)
                {
                    recipes.add(newRecipe(material.moltenFluid, 2, DefinedGenItems.HOE_HEAD.stack(material)));
                }

            }
        }
    }
}
