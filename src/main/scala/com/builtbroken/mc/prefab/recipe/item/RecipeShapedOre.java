package com.builtbroken.mc.prefab.recipe.item;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;

public class RecipeShapedOre extends ShapedOreRecipe
{
    private static final int MAX_CRAFT_GRID_WIDTH = 3;
    private static final int MAX_CRAFT_GRID_HEIGHT = 3;
    int w = 0;
    int h = 0;

    public RecipeShapedOre(Block result, Object... recipe) { this(new ItemStack(result), recipe); }

    public RecipeShapedOre(Item result, Object... recipe) { this(new ItemStack(result), recipe); }

    public RecipeShapedOre(ItemStack result, Object... recipe)
    {
        super(result, recipe);
        getV();
    }

    private void getV()
    {
        try
        {
            Field field = ShapedOreRecipe.class.getDeclaredFields()[4];
            field.setAccessible(true);
            w = field.getInt(this);

            field = ShapedOreRecipe.class.getDeclaredFields()[5];
            field.setAccessible(true);
            h = field.getInt(this);
        } catch (IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
    }

    protected ItemStack toItemStack(Object in)
    {
        if (in instanceof ItemStack)
        {
            return ((ItemStack) in).copy();
        }
        else if (in instanceof Item)
        {
            return new ItemStack((Item) in);
        }
        else if (in instanceof Block)
        {
            return new ItemStack((Block) in, 1, OreDictionary.WILDCARD_VALUE);
        }
        return null;
    }


    /**
     * Used to check if a recipe matches current crafting inventory
     */
    @Override
    public boolean matches(InventoryCrafting inv, World world)
    {
        for (int x = 0; x <= MAX_CRAFT_GRID_WIDTH - w; x++)
        {
            for (int y = 0; y <= MAX_CRAFT_GRID_HEIGHT - h; ++y)
            {
                if (checkMatch(inv, x, y, false))
                {
                    return true;
                }
            }
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    private boolean checkMatch(InventoryCrafting inv, int startX, int startY, boolean mirror)
    {
        for (int x = 0; x < MAX_CRAFT_GRID_WIDTH; x++)
        {
            for (int y = 0; y < MAX_CRAFT_GRID_HEIGHT; y++)
            {
                int subX = x - startX;
                int subY = y - startY;
                Object target = null;

                if (subX >= 0 && subY >= 0 && subX < w && subY < h)
                {
                    if (mirror)
                    {
                        target = getInput()[w - subX - 1 + subY * w];
                    }
                    else
                    {
                        target = getInput()[subX + subY * w];
                    }
                }

                ItemStack slot = inv.getStackInRowAndColumn(x, y);

                if (target instanceof ItemStack)
                {
                    if (!itemMatches((ItemStack) target, slot, false))
                    {
                        return false;
                    }
                }
                else if (target instanceof ArrayList)
                {
                    boolean matched = false;

                    Iterator<ItemStack> itr = ((ArrayList<ItemStack>) target).iterator();
                    while (itr.hasNext() && !matched)
                    {
                        matched = itemMatches(itr.next(), slot, false);
                    }

                    if (!matched)
                    {
                        return false;
                    }
                }
                else if (target == null && slot != null)
                {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean itemMatches(ItemStack target, ItemStack input, boolean strict)
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