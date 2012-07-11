package universalelectricity.recipe;

import net.minecraft.server.ItemStack;


/**
 * The Class UEFurnaceRecipe.
 */
public class UEFurnaceRecipe
{
    
    /** The output. */
    public ItemStack output;
    
    /** The input. */
    public ItemStack input;

    /**
     * Instantiates a new uE furnace recipe.
     *
     * @param itemstack the itemstack
     * @param itemstack1 the itemstack1
     */
    public UEFurnaceRecipe(ItemStack itemstack, ItemStack itemstack1)
    {
        output = itemstack;
        input = itemstack1;
    }
}
