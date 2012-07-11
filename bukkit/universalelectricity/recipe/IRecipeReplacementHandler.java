package universalelectricity.recipe;

import net.minecraft.server.ItemStack;


/**
 * The Interface IRecipeReplacementHandler.
 */
public interface IRecipeReplacementHandler
{
    
    /**
     * On replace shaped recipe.
     *
     * @param uerecipe the uerecipe
     * @return the object[]
     */
    public abstract Object[] onReplaceShapedRecipe(UERecipe uerecipe);

    /**
     * On replace shapeless recipe.
     *
     * @param uerecipe the uerecipe
     * @return the object[]
     */
    public abstract Object[] onReplaceShapelessRecipe(UERecipe uerecipe);

    /**
     * On replace smelting recipe.
     *
     * @param uefurnacerecipe the uefurnacerecipe
     * @return the item stack
     */
    public abstract ItemStack onReplaceSmeltingRecipe(UEFurnaceRecipe uefurnacerecipe);
}
