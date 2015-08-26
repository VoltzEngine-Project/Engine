package com.builtbroken.mc.prefab.recipe.item.sheetmetal;

import com.builtbroken.mc.core.content.tool.ItemSheetMetalTools;
import com.builtbroken.mc.prefab.recipe.item.RecipeShapedOre;
import net.minecraft.item.ItemStack;

/**
 * Recipe designed to better handle support for sheet metal tools
 * Created by Cow Pi on 8/26/2015.
 */
public class RecipeSheetMetal extends RecipeShapedOre
{
    public RecipeSheetMetal(ItemStack result, Object... recipe)
    {
        super(result, recipe);
    }

    @Override
    protected ItemStack toItemStack(Object in)
    {
        if (in instanceof String && in.equals("sheetMetalShears"))
        {
            return ItemSheetMetalTools.getHammer();
        }
        else if (in instanceof String && in.equals("sheetMetalHammer"))
        {
            return ItemSheetMetalTools.getHammer();
        }
        return super.toItemStack(in);
    }

    @Override
    public boolean itemMatches(ItemStack target, ItemStack input, boolean strict)
    {
        if (!strict && target != null && target.getItem() instanceof ItemSheetMetalTools && input != null && input.getItem() instanceof ItemSheetMetalTools)
        {
            return ((ItemSheetMetalTools) target.getItem()).getType(target).equals(((ItemSheetMetalTools) input.getItem()).getType(input));
        }
        return super.itemMatches(target, input, strict);
    }

}
