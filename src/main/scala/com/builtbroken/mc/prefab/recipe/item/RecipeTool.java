package com.builtbroken.mc.prefab.recipe.item;

import com.builtbroken.mc.api.items.tools.IItemTool;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Recipe that handles tool with metadata for damage. These tools do not leave the grid but rather take damage.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/2/2015.
 */
public class RecipeTool extends RecipeShapedOre
{
    public RecipeTool(Block result, Object... recipe)
    {
        super(result, recipe);
    }

    public RecipeTool(Item result, Object... recipe)
    {
        super(result, recipe);
    }

    public RecipeTool(ItemStack result, Object... recipe)
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
