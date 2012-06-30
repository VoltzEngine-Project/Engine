package net.minecraft.src.universalelectricity.api;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.CraftingManager;
import net.minecraft.src.FurnaceRecipes;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.src.forge.NetworkMod;
import net.minecraft.src.forge.oredict.ShapedOreRecipe;
import net.minecraft.src.forge.oredict.ShapelessOreRecipe;
import net.minecraft.src.universalelectricity.UniversalElectricity;

/**
 * Adds recipes with full Forge ore dictionary support and replacable by add-ons.
 * Use UERecipes.addRecipe(....) like how you use ModLoader.addRecipe.
 * @author Calclavia
 *
 */
public class UERecipes
{
	public static final List<UERecipe> shapedRecipes = new ArrayList<UERecipe>();
	public static final List<UERecipe> shapelessRecipes = new ArrayList<UERecipe>();
	public static final List<UEFurnaceRecipe> furnaceRecipes = new ArrayList<UEFurnaceRecipe>();
	
	//Shaped Recipes
	public static void addRecipe(ItemStack output, Object[] input)
	{
		shapedRecipes.add(new UERecipe(output, input));
	}
	
	public static void addRecipe(Item output, Object[] input)
	{
		addRecipe(new ItemStack(output), input);
	}
	
	public static void addRecipe(Block output, Object[] input)
	{
		addRecipe(new ItemStack(output), input);
	}
	
	//Shapeless Recipes
	public static void addShapelessRecipe(ItemStack output, Object[] input)
	{
		shapelessRecipes.add(new UERecipe(output, input));
	}
	
	public static void addShapelessRecipe(Item output, Object[] input)
	{
		addShapelessRecipe(new ItemStack(output), input);
	}
	
	public static void addShapelessRecipe(Block output, Object[] input)
	{
		addShapelessRecipe(new ItemStack(output), input);
	}
	
	//Furnace Smelting Recipes
	public static void addSmelting(ItemStack input, ItemStack output)
	{
		furnaceRecipes.add(new UEFurnaceRecipe(output, input));
	}
	
	public static void addSmelting(Item input, ItemStack output)
	{
		addSmelting(new ItemStack(input), output);
	}
	
	//Adds all recipes and checks for recipe replacements
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
			CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(recipe.output, recipe.input));
		}
		
		for(UERecipe recipe : shapelessRecipes)
		{
			CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(recipe.output, recipe.input));
		}
		
		for(UEFurnaceRecipe recipe : furnaceRecipes)
		{
			FurnaceRecipes.smelting().addSmelting(recipe.input.itemID, recipe.input.getItemDamage(), recipe.output);
		}
	}
	
}
