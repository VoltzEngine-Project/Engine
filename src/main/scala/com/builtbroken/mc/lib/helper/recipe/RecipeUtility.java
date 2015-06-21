package com.builtbroken.mc.lib.helper.recipe;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.config.Configuration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Alias class of Recipes
 */
@Deprecated
public class RecipeUtility
{
	public static final String CATEGORY = "crafting";

	public static List<IRecipe> getRecipesByOutput(ItemStack output)
	{
		List<IRecipe> list = new ArrayList<IRecipe>();

		for (Object obj : CraftingManager.getInstance().getRecipeList())
		{
			if (obj instanceof IRecipe)
			{
				if (ItemStack.areItemStacksEqual(((IRecipe) obj).getRecipeOutput(), output))
				{
					list.add((IRecipe) obj);
				}
			}
		}

		return list;
	}

	/**
	 * Replaces a recipe with a new IRecipe.
	 *
	 * @return True if successful
	 */
	public static boolean replaceRecipe(IRecipe recipe, IRecipe newRecipe)
	{
		for (Object obj : CraftingManager.getInstance().getRecipeList())
		{
			if (obj instanceof IRecipe)
			{
				if (obj.equals(recipe) || obj == recipe)
				{
					CraftingManager.getInstance().getRecipeList().remove(obj);
					CraftingManager.getInstance().getRecipeList().add(newRecipe);
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Replaces a recipe with the resulting ItemStack with a new IRecipe.
	 *
	 * @return True if successful
	 */
	public static boolean replaceRecipe(ItemStack recipe, IRecipe newRecipe)
	{
		if (removeRecipe(recipe))
		{
			CraftingManager.getInstance().getRecipeList().add(newRecipe);
			return true;
		}

		return false;
	}

	/**
	 * Removes a recipe by its IRecipe class.
	 *
	 * @return True if successful
	 */
	public static boolean removeRecipe(IRecipe recipe)
	{
		for (Object obj : CraftingManager.getInstance().getRecipeList())
		{
			if (obj != null)
			{
				if (obj instanceof IRecipe)
				{
					if (obj.equals(recipe) || obj == recipe)
					{
						CraftingManager.getInstance().getRecipeList().remove(obj);
						return true;
					}
				}
			}
		}

		return false;
	}

	/**
	 * Removes the first recipe found by its output.
	 *
	 * @return True if successful
	 */
	public static boolean removeRecipe(ItemStack stack)
	{
		for (Object obj : CraftingManager.getInstance().getRecipeList())
		{
			if (obj != null)
			{
				if (obj instanceof IRecipe)
				{
					if (((IRecipe) obj).getRecipeOutput() != null)
					{
						if (((IRecipe) obj).getRecipeOutput().isItemEqual(stack))
						{
							CraftingManager.getInstance().getRecipeList().remove(obj);
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	/**
	 * Removes all recipes found that has this output. You may use this with Forge Ore Dictionary to
	 * remove all recipes with the FoD ID.
	 *
	 * @return True if successful
	 */
	public static boolean removeRecipes(ItemStack... itemStacks)
	{
		boolean didRemove = false;

		for (Iterator itr = CraftingManager.getInstance().getRecipeList().iterator(); itr.hasNext(); )
		{
			Object obj = itr.next();

			if (obj != null)
			{
				if (obj instanceof IRecipe)
				{
					if (((IRecipe) obj).getRecipeOutput() != null)
					{
						for (ItemStack itemStack : itemStacks)
						{
							if (((IRecipe) obj).getRecipeOutput().isItemEqual(itemStack))
							{
								itr.remove();
								didRemove = true;
								break;
							}
						}
					}
				}
			}
		}

		return didRemove;
	}

	/**
	 * Use this function if you want to check if the recipe is allowed in the configuration file.
	 */
	public static void addRecipe(IRecipe recipe, String name, Configuration configuration, boolean defaultBoolean)
	{
		if (configuration != null)
		{
			configuration.load();

			if (configuration.get(CATEGORY, "Allow " + name + " Crafting", defaultBoolean).getBoolean(defaultBoolean))
			{
				GameRegistry.addRecipe(recipe);
			}

			configuration.save();
		}
	}

	public static void addRecipe(IRecipe recipe, Configuration config, boolean defaultBoolean)
	{
		addRecipe(recipe, recipe.getRecipeOutput().getUnlocalizedName(), config, defaultBoolean);
	}

	/**
	 * Use this function if you want to check if the recipe is allowed in the configuration file.
	 */
	public static void addShaplessRecipe(ItemStack itemStack, Object[] items, String name, Configuration configuration, boolean defaultBoolean)
	{
		if (configuration != null)
		{
			configuration.load();

			if (configuration.get(CATEGORY, "Allow " + name + " Crafting", defaultBoolean).getBoolean(defaultBoolean))
			{
				CraftingManager.getInstance().addShapelessRecipe(itemStack, items);
			}

			configuration.save();
		}
	}

	public static void addShaplessRecipe(ItemStack itemStack, Object[] items, Configuration config, boolean defaultBoolean)
	{
		addShaplessRecipe(itemStack, items, itemStack.getItem().getUnlocalizedName(), config, defaultBoolean);
	}
}
