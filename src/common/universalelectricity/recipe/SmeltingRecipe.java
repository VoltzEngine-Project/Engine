package universalelectricity.recipe;

import net.minecraft.src.ItemStack;

public class SmeltingRecipe
{
    public ItemStack output;
    public ItemStack input;

    public SmeltingRecipe(ItemStack output, ItemStack input)
    {
        this.output = output;
        this.input = input;
    }
    
    public boolean isEqual(SmeltingRecipe comparingRecipe)
    {
    	if(this.input == comparingRecipe.input && this.output == comparingRecipe.output)
    	{
    		return true;
    	}
    	
    	return false;
    }
}
