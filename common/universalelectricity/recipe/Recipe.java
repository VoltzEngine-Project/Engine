package universalelectricity.recipe;

import net.minecraft.src.ItemStack;

public class Recipe
{
    public ItemStack output;
    public Object[] input;

    public Recipe(ItemStack output, Object[] input)
    {
        this.output = output;
        this.input = input;
    }
    
    public boolean isEqual(Recipe comparingRecipe)
    {
    	if(this.input == comparingRecipe.input && this.output == comparingRecipe.output)
    	{
    		return true;
    	}
    	
    	return false;
    }
}
