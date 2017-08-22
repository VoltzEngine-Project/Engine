package com.builtbroken.mc.framework.recipe.item;

import com.builtbroken.mc.core.References;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import javax.annotation.Nonnull;
import java.util.Iterator;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 11/6/2015.
 */
public class RecipeShapelessOre extends ShapelessOreRecipe
{
    public RecipeShapelessOre(Block result, Object... recipe)
    {
        super(new ResourceLocation(References.DOMAIN, result.getUnlocalizedName()), result, recipe);
    }

    public RecipeShapelessOre(Item result, Object... recipe)
    {
        super(new ResourceLocation(References.DOMAIN, result.getUnlocalizedName()), result, recipe);
    }

    public RecipeShapelessOre(ItemStack result, Object... recipe)
    {
        super(new ResourceLocation(References.DOMAIN, result.getUnlocalizedName()), result, recipe);
    }

    @Override
    public boolean matches(@Nonnull InventoryCrafting var1, @Nonnull World world)
    {
        NonNullList<Ingredient> required = NonNullList.create();
        required.addAll(input);

        for (int x = 0; x < var1.getSizeInventory(); x++)
        {
            ItemStack slot = var1.getStackInSlot(x);

            if (!slot.isEmpty())
            {
                boolean inRecipe = false;
                Iterator<Ingredient> req = required.iterator();

                while (req.hasNext())
                {
                    Ingredient ingredient = req.next();
                    if (ingredient.apply(slot))
                    {
                        inRecipe = true;
                        req.remove();
                        break;
                    }
                    else
                    {
                        for (ItemStack itemstack : ingredient.getMatchingStacks())
                        {
                            if (itemMatches(itemstack, slot, false))
                            {
                                inRecipe = true;
                                req.remove();
                                break;
                            }
                        }
                    }
                }

                if (!inRecipe)
                {
                    return false;
                }
            }
        }

        return required.isEmpty();
    }

    /**
     * Checks if the two items match each other based on some conditions. Override to change the conditions
     * based on the target or input itemstack
     *
     * @param target - item we are looking/testing against
     * @param input  - item that is passed in as the input
     * @param strict - is the metadata value for the recipe strict
     * @return true if the items match all conditions
     */
    protected boolean itemMatches(ItemStack target, ItemStack input, boolean strict)
    {
        if (input == null && target != null || input != null && target == null)
        {
            return false;
        }
        if (target.getItem() == input.getItem())
        {
            if ((target.getItemDamage() == OreDictionary.WILDCARD_VALUE && !strict) || target.getItemDamage() == input.getItemDamage())
            {
                return target.getTagCompound() == null && input.getTagCompound() == null || target.getTagCompound() != null && input.getTagCompound() != null && target.getTagCompound().equals(input.getTagCompound());
            }
        }
        return false;
    }
}
