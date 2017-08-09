package com.builtbroken.mc.framework.recipe.item;

import com.builtbroken.mc.api.items.tools.IItemTool;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Shapeless version of {@link RecipeTool}
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 11/6/2015.
 */
public class RecipeShapelessTool extends RecipeShapelessOre
{
    public RecipeShapelessTool(Block result, Object... recipe)
    {
        super(result, recipe);
    }

    public RecipeShapelessTool(Item result, Object... recipe)
    {
        super(result, recipe);
    }

    public RecipeShapelessTool(ItemStack result, Object... recipe)
    {
        super(result, recipe);
    }

    @Override
    public boolean itemMatches(ItemStack target, ItemStack input, boolean strict)
    {
        if (!strict && target != null && target.getItem() instanceof IItemTool && input != null && input.getItem() instanceof IItemTool)
        {
            return ((IItemTool) target.getItem()).getToolCategory(target).equalsIgnoreCase(((IItemTool) input.getItem()).getToolCategory(input)) && ((IItemTool) target.getItem()).getToolType(target).equalsIgnoreCase(((IItemTool) input.getItem()).getToolType(input));
        }
        return super.itemMatches(target, input, strict);
    }
}
