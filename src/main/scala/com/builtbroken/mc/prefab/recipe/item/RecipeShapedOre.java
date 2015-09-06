package com.builtbroken.mc.prefab.recipe.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeShapedOre implements IRecipe
{
    //Added in for future ease of change, but hard coded for now.
    private static final int MAX_CRAFT_GRID_WIDTH = 3;
    private static final int MAX_CRAFT_GRID_HEIGHT = 3;

    private ItemStack output = null;
    private Object[] input = null;
    private int width = 0;
    private int height = 0;
    private boolean mirrored = true;

    public RecipeShapedOre(Block result, Object... recipe) { this(new ItemStack(result), recipe); }

    public RecipeShapedOre(Item result, Object... recipe) { this(new ItemStack(result), recipe); }

    public RecipeShapedOre(ItemStack result, Object... recipe)
    {
        output = result.copy();
        process(recipe);
    }

    protected void process(Object... recipe)
    {
        String shape = "";
        int idx = 0;

        if (recipe[idx] instanceof Boolean)
        {
            mirrored = (Boolean) recipe[idx];
            if (recipe[idx + 1] instanceof Object[])
            {
                recipe = (Object[]) recipe[idx + 1];
            } else
            {
                idx = 1;
            }
        }

        //Parse for recipe
        if (recipe[idx] instanceof String[])
        {
            String[] parts = ((String[]) recipe[idx++]);

            for (String s : parts)
            {
                width = s.length();
                shape += s;
            }

            height = parts.length;
        } else
        {
            while (recipe[idx] instanceof String)
            {
                String s = (String) recipe[idx++];
                shape += s;
                width = s.length();
                height++;
            }
        }

        //Parse for recipe items
        if (width * height != shape.length())
        {
            String ret = "Invalid shaped ore recipe: ";
            for (Object tmp : recipe)
            {
                ret += tmp + ", ";
            }
            ret += output;
            throw new RuntimeException(ret);
        }

        HashMap<Character, Object> itemMap = new HashMap<Character, Object>();

        for (; idx < recipe.length; idx += 2)
        {
            Character chr = (Character) recipe[idx];
            Object in = recipe[idx + 1];
            ItemStack stack = toItemStack(in);
            if (stack == null)
            {
                if (in instanceof String && !OreDictionary.getOres((String) in).isEmpty())
                {
                    itemMap.put(chr, OreDictionary.getOres((String) in));
                } else
                {
                    String ret = "Invalid shaped ore recipe: ";
                    for (Object tmp : recipe)
                    {
                        ret += tmp + ", ";
                    }
                    ret += output;
                    throw new RuntimeException(ret);
                }
            } else
            {
                itemMap.put(chr, stack);
            }
        }

        input = new Object[width * height];
        int x = 0;
        for (char chr : shape.toCharArray())
        {
            input[x++] = itemMap.get(chr);
        }
    }

    protected ItemStack toItemStack(Object in)
    {
        if (in instanceof ItemStack)
        {
            return ((ItemStack) in).copy();
        } else if (in instanceof Item)
        {
            return new ItemStack((Item) in);
        } else if (in instanceof Block)
        {
            return new ItemStack((Block) in, 1, OreDictionary.WILDCARD_VALUE);
        }
        return null;
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    @Override
    public ItemStack getCraftingResult(InventoryCrafting var1) { return output.copy(); }

    /**
     * Returns the size of the recipe area
     */
    @Override
    public int getRecipeSize() { return input.length; }

    @Override
    public ItemStack getRecipeOutput() { return output; }

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    @Override
    public boolean matches(InventoryCrafting inv, World world)
    {
        for (int x = 0; x <= MAX_CRAFT_GRID_WIDTH - width; x++)
        {
            for (int y = 0; y <= MAX_CRAFT_GRID_HEIGHT - height; ++y)
            {
                if (checkMatch(inv, x, y, false))
                {
                    return true;
                }

                if (mirrored && checkMatch(inv, x, y, true))
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

                if (subX >= 0 && subY >= 0 && subX < width && subY < height)
                {
                    if (mirror)
                    {
                        target = input[width - subX - 1 + subY * width];
                    } else
                    {
                        target = input[subX + subY * width];
                    }
                }

                ItemStack slot = inv.getStackInRowAndColumn(x, y);

                if (target instanceof ItemStack)
                {
                    if (!itemMatches((ItemStack) target, slot, false))
                    {
                        return false;
                    }
                } else if (target instanceof ArrayList)
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
                } else if (target == null && slot != null)
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

    public RecipeShapedOre setMirrored(boolean mirror)
    {
        mirrored = mirror;
        return this;
    }

    /**
     * Returns the input for this recipe, any mod accessing this value should never
     * manipulate the values in this array as it will effect the recipe itself.
     *
     * @return The recipes input vales.
     */
    public Object[] getInput()
    {
        return this.input;
    }
}