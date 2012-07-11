package universalelectricity.recipe;

import net.minecraft.server.ItemStack;


/**
 * The Class UERecipe.
 */
public class UERecipe
{
    
    /** The output. */
    public ItemStack output;
    
    /** The input. */
    public Object input[];

    /**
     * Instantiates a new uE recipe.
     *
     * @param itemstack the itemstack
     * @param aobj the aobj
     */
    public UERecipe(ItemStack itemstack, Object aobj[])
    {
        output = itemstack;
        input = aobj;
    }
}
