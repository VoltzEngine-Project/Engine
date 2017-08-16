package com.builtbroken.mc.framework.recipe.item.sheetmetal;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.content.tool.ItemSheetMetalTools;
import com.builtbroken.mc.framework.recipe.item.RecipeTool;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Recipe designed to better handle support for sheet metal tools
 * Created by DarkCow on 8/26/2015.
 */
@Deprecated //broken as toItemStack is not actually impelemented
public class RecipeSheetMetal extends RecipeTool
{
    public RecipeSheetMetal(Block result, Object... recipe)
    {
        super(result, recipe);
    }

    public RecipeSheetMetal(Item result, Object... recipe)
    {
        super(result, recipe);
    }

    public RecipeSheetMetal(ItemStack result, Object... recipe)
    {
        super(result, recipe);
    }

    @Override
    protected ItemStack toItemStack(Object in)
    {
        if (Engine.itemSheetMetalTools != null)
        {
            if (in instanceof String && (in.equals("sheetMetalShears") || in.equals("tool@shear")))
            {
                return ItemSheetMetalTools.getShears();
            }
            else if (in instanceof String && (in.equals("sheetMetalHammer") || in.equals("tool@hammer")))
            {
                return ItemSheetMetalTools.getHammer();
            }
        }
        return super.toItemStack(in);
    }
}
