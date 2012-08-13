package universalelectricity.recipe;

import net.minecraft.src.ItemStack;

public class UEFurnaceRecipe
{
    public ItemStack output;
    public ItemStack input;

    public UEFurnaceRecipe(ItemStack output, ItemStack input)
    {
        this.output = output;
        this.input = input;
    }
}
