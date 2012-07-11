package universalelectricity.recipe;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.Block;
import net.minecraft.server.CraftingManager;
import net.minecraft.server.FurnaceRecipes;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import universalelectricity.UniversalElectricity;
import forge.NetworkMod;
import forge.oredict.ShapedOreRecipe;
import forge.oredict.ShapelessOreRecipe;


/**
 * The Class UERecipeManager.
 */
public class UERecipeManager
{
	
	/** The Constant shapedRecipes. */
	public static final List<UERecipe> shapedRecipes = new ArrayList<UERecipe>();
	
	/** The Constant shapelessRecipes. */
	public static final List<UERecipe> shapelessRecipes = new ArrayList<UERecipe>();
	
	/** The Constant furnaceRecipes. */
	public static final List<UEFurnaceRecipe> furnaceRecipes = new ArrayList<UEFurnaceRecipe>();

	//Shaped Recipes
	/**
	 * Adds the recipe.
	 *
	 * @param output the output
	 * @param input the input
	 */
	public static void addRecipe(ItemStack output, Object[] input)
	{
		shapedRecipes.add(new UERecipe(output, input));
	}

	/**
	 * Adds the recipe.
	 *
	 * @param output the output
	 * @param input the input
	 */
	public static void addRecipe(Item output, Object[] input)
	{
		addRecipe(new ItemStack(output), input);
	}

	/**
	 * Adds the recipe.
	 *
	 * @param output the output
	 * @param input the input
	 */
	public static void addRecipe(Block output, Object[] input)
	{
		addRecipe(new ItemStack(output), input);
	}

	//Shapeless Recipes
	/**
	 * Adds the shapeless recipe.
	 *
	 * @param output the output
	 * @param input the input
	 */
	public static void addShapelessRecipe(ItemStack output, Object[] input)
	{
		shapelessRecipes.add(new UERecipe(output, input));
	}

	/**
	 * Adds the shapeless recipe.
	 *
	 * @param output the output
	 * @param input the input
	 */
	public static void addShapelessRecipe(Item output, Object[] input)
	{
		addShapelessRecipe(new ItemStack(output), input);
	}

	/**
	 * Adds the shapeless recipe.
	 *
	 * @param output the output
	 * @param input the input
	 */
	public static void addShapelessRecipe(Block output, Object[] input)
	{
		addShapelessRecipe(new ItemStack(output), input);
	}

	//Furnace Smelting Recipes
	/**
	 * Adds the smelting.
	 *
	 * @param input the input
	 * @param output the output
	 */
	public static void addSmelting(ItemStack input, ItemStack output)
	{
		furnaceRecipes.add(new UEFurnaceRecipe(output, input));
	}

	/**
	 * Adds the smelting.
	 *
	 * @param input the input
	 * @param output the output
	 */
	public static void addSmelting(Item input, ItemStack output)
	{
		addSmelting(new ItemStack(input), output);
	}

	//Adds all recipes and checks for recipe replacements
	/**
	 * Initialize.
	 */
	public static void initialize()
	{
		//Replace all recipes
		for(NetworkMod addon : UniversalElectricity.addons)
		{
			if(addon instanceof IRecipeReplacementHandler)
			{
				IRecipeReplacementHandler recipeHandler = (IRecipeReplacementHandler)addon;

				for(UERecipe recipe : shapedRecipes)
				{
					if(recipeHandler.onReplaceShapedRecipe(recipe) != null)
					{
						recipe.input = recipeHandler.onReplaceShapedRecipe(recipe);
					}
				}

				for(UERecipe recipe : shapelessRecipes)
				{
					if(recipeHandler.onReplaceShapelessRecipe(recipe) != null)
					{
						recipe.input = recipeHandler.onReplaceShapelessRecipe(recipe);
					}
				}

				for(UEFurnaceRecipe recipe : furnaceRecipes)
				{
					if(recipeHandler.onReplaceSmeltingRecipe(recipe) != null)
					{
						recipe.input = recipeHandler.onReplaceSmeltingRecipe(recipe);
					}
				}
			}
		}

		for(UERecipe recipe : shapedRecipes)
		{
			CraftingManager.getInstance().getRecipies().add(new ShapedOreRecipe(recipe.output, recipe.input));
		}

		for(UERecipe recipe : shapelessRecipes)
		{
			CraftingManager.getInstance().getRecipies().add(new ShapelessOreRecipe(recipe.output, recipe.input));
		}

		for(UEFurnaceRecipe recipe : furnaceRecipes)
		{
			FurnaceRecipes.getInstance().addSmelting(recipe.input.id, recipe.input.getData(), recipe.output);
		}
	}

}