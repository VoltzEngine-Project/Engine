package com.builtbroken.mc.core.content.resources.load;

import com.builtbroken.mc.api.recipe.MachineRecipeType;
import com.builtbroken.mc.core.content.resources.DefinedGenItems;
import com.builtbroken.mc.core.content.resources.GenMaterial;
import com.builtbroken.mc.lib.helper.LanguageUtility;
import com.builtbroken.mc.framework.recipe.item.MRItemStack;
import com.builtbroken.mc.framework.recipe.item.MRLoaderItemStack;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

/**
 * Loadable that handles all machine recipes that crush items
 * Created by robert on 1/10/2015.
 */
@Deprecated //TODO transition to JSON
public class CrusherRecipeLoad extends MRLoaderItemStack
{
    public CrusherRecipeLoad()
    {
        super(MachineRecipeType.ITEM_CRUSHER.INTERNAL_NAME);
    }

    @Override
    protected void generateRecipes(List<MRItemStack> recipes)
    {
        recipes.add(newRecipe(Blocks.cobblestone).addInputOption(Blocks.stone));
        recipes.add(newRecipe(new ItemStack(Blocks.cobblestone, 4)).addInputOption(Blocks.stonebrick));
        if (DefinedGenItems.RUBBLE.item != null)
        {
            for (GenMaterial mat : GenMaterial.values())
            {
                if (mat != GenMaterial.UNKNOWN && !DefinedGenItems.RUBBLE.ignoreMaterials.contains(mat))
                {
                    List<ItemStack> oreStacks = OreDictionary.getOres("ore" + LanguageUtility.capitalizeFirst(mat.name().toLowerCase()));
                    if (oreStacks != null && !oreStacks.isEmpty())
                    {
                        for (ItemStack stack : oreStacks)
                        {
                            if (stack != null && stack.getItem() != null)
                            {
                                recipes.add(newRecipe(DefinedGenItems.RUBBLE.stack(mat)).addInputOption(stack));
                            }
                        }
                    }
                }
            }
        }
    }
}
