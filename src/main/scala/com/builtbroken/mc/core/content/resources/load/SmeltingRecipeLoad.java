package com.builtbroken.mc.core.content.resources.load;

import com.builtbroken.mc.api.recipe.MachineRecipeType;
import com.builtbroken.mc.core.content.resources.DefinedGenItems;
import com.builtbroken.mc.core.content.resources.GenMaterial;
import com.builtbroken.mc.lib.helper.LanguageUtility;
import com.builtbroken.mc.lib.recipe.item.MRItemStack;
import com.builtbroken.mc.lib.recipe.item.MRLoaderItemStack;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

/**
 * Loadable that handles all machine recipes that crush items
 * Created by robert on 1/10/2015.
 */
public class SmeltingRecipeLoad extends MRLoaderItemStack
{
    public SmeltingRecipeLoad()
    {
        super(MachineRecipeType.ITEM_CRUSHER.INTERNAL_NAME);
    }

    @Override
    protected void generateRecipes(List<MRItemStack> recipes)
    {
        for (GenMaterial mat : GenMaterial.values())
        {
            if (mat != GenMaterial.UNKNOWN)
            {
                List<ItemStack> ingots = OreDictionary.getOres("ingot" + LanguageUtility.capitalizeFirst(mat.name().toLowerCase()));
                if (!ingots.isEmpty())
                {
                    ItemStack ingotStack = ingots.get(0);
                    if (ingotStack == null)
                    {
                        int i = 1;
                        while (ingotStack == null && i < ingots.size())
                        {
                            ingotStack = ingots.get(i);
                        }
                    }
                    if (ingotStack != null)
                    {
                        if (DefinedGenItems.RUBBLE.item != null && !DefinedGenItems.RUBBLE.ignoreMaterials.contains(mat))
                        {
                            GameRegistry.addSmelting(DefinedGenItems.RUBBLE.stack(mat), ingotStack, 0.01f);
                        }
                        if (DefinedGenItems.DUST.item != null && !DefinedGenItems.DUST.ignoreMaterials.contains(mat))
                        {
                            GameRegistry.addSmelting(DefinedGenItems.DUST.stack(mat), ingotStack, 0.02f);
                        }
                        if (DefinedGenItems.DUST_IMPURE.item != null && !DefinedGenItems.DUST_IMPURE.ignoreMaterials.contains(mat))
                        {
                            GameRegistry.addSmelting(DefinedGenItems.DUST_IMPURE.stack(mat), ingotStack, 0.02f);
                        }
                    }
                }
            }
        }
    }
}
