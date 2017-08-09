package com.builtbroken.mc.framework.recipe.item.sheetmetal;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.content.tool.ItemSheetMetalTools;
import com.builtbroken.mc.framework.recipe.item.RecipeTool;
import net.minecraft.item.ItemStack;

/**
 * Recipe designed to better handle support for sheet metal tools
 * Created by DarkCow on 8/26/2015.
 */
public class RecipeSheetMetal extends RecipeTool
{
    public RecipeSheetMetal(ItemStack result, Object... recipe)
    {
        super(result, recipe);
    }

    @Override
    protected ItemStack toItemStack(Object in)
    {
        if (Engine.itemSheetMetalTools != null)
        {
            if (in instanceof String && in.equals("sheetMetalShears"))
            {
                return ItemSheetMetalTools.getHammer();
            }
            else if (in instanceof String && in.equals("sheetMetalHammer"))
            {
                return ItemSheetMetalTools.getHammer();
            }
        }
        return super.toItemStack(in);
    }
}
