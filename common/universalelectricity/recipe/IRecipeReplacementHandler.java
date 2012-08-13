package universalelectricity.recipe;


/**
 * Implement this class into your BaseMod class if you wish to replace default UE recipes.
 * Make sure you used UniversalElectricity.registerAddon or this will not work.
 * @author Calclavia
 *
 */
public interface IRecipeReplacementHandler
{
    /**
     * Called in an attempt to see if any add-on wishes to replace this specific recipe.
     * @param recipe - The recipe attempted to being replaced
     * @return - Return null if not replacing this recipe or a new recipe input replacement
     */
    public UERecipe onReplaceShapedRecipe(UERecipe recipe);

    public UERecipe onReplaceShapelessRecipe(UERecipe recipe);

    public UEFurnaceRecipe onReplaceSmeltingRecipe(UEFurnaceRecipe recipe);
}
