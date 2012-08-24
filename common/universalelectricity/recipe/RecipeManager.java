package universalelectricity.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.src.Block;
import net.minecraft.src.CraftingManager;
import net.minecraft.src.FurnaceRecipes;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

/**
 * Adds recipes with full Forge ore dictionary support and replaceable by add-ons.
 * Use UERecipes.addRecipe(....) like how you use ModLoader.addRecipe.
 * 
 * MAKE SURE YOU ADD/REPLACE YOUR RECIPE BEFORE ModsLoaded()/@PostInit OR IT WONT BE REPLACABLE!
 * @author Calclavia
 *
 */
public class RecipeManager
{
	//Crafting Recipes
	private static final List<CraftingRecipe> SHAPED_RECIPES = new ArrayList<CraftingRecipe>();
	private static final List<CraftingRecipe> SHAPELESS_RECIPES = new ArrayList<CraftingRecipe>();
	
    //Smelting Recipes
	private static final List<SmeltingRecipe> SMELTING_RECIPES = new ArrayList<SmeltingRecipe>();

    //Custom recipe handlers for UE machines
	private static final Map<String, IRecipeHandler> RECIPE_HANDLERS = new HashMap<String, IRecipeHandler>();
    
    //Shaped Recipes
    public static void addRecipe(ItemStack output, Object[] input)
    {
        SHAPED_RECIPES.add(new CraftingRecipe(output, input));
    }

    public static void addRecipe(Item output, Object[] input)
    {
        addRecipe(new ItemStack(output), input);
    }

    public static void addRecipe(Block output, Object[] input)
    {
        addRecipe(new ItemStack(output), input);
    }
    
    public static List<CraftingRecipe> getRecipes() { return SHAPED_RECIPES; }
    
    public static CraftingRecipe getRecipeByOutput(ItemStack output)
    {
    	for(CraftingRecipe recipe : SHAPED_RECIPES)
        {
            if(recipe.output == output)
            {
            	return recipe;
            }
        }
		return null;
    }

    //Shapeless Recipes
    public static void addShapelessRecipe(ItemStack output, Object[] input)
    {
        SHAPELESS_RECIPES.add(new CraftingRecipe(output, input));
    }

    public static void addShapelessRecipe(Item output, Object[] input)
    {
        addShapelessRecipe(new ItemStack(output), input);
    }

    public static void addShapelessRecipe(Block output, Object[] input)
    {
        addShapelessRecipe(new ItemStack(output), input);
    }
    
    public static List<CraftingRecipe> getShapelessRecipes() { return SHAPELESS_RECIPES; }
    
    public static CraftingRecipe getShapelessRecipeByOutput(ItemStack output)
    {
    	for(CraftingRecipe recipe : SHAPELESS_RECIPES)
        {
            if(recipe.output == output)
            {
            	return recipe;
            }
        }
		return null;
    }

    //Furnace Smelting Recipes
    public static void addSmelting(ItemStack input, ItemStack output)
    {
        SMELTING_RECIPES.add(new SmeltingRecipe(output, input));
    }

    public static void addSmelting(Item input, ItemStack output)
    {
        addSmelting(new ItemStack(input), output);
    }
    
    public static List<SmeltingRecipe> getSmeltingRecipes() { return SMELTING_RECIPES; }
    
    public static SmeltingRecipe getSmeltingRecipeByOutput(ItemStack output)
    {
    	for(SmeltingRecipe recipe : SMELTING_RECIPES)
        {
            if(recipe.output == output)
            {
            	return recipe;
            }
        }
		return null;
    }
    /**
     * Registers your {@link #IRecipeHandler} to the Recipe Manager so other UE mods can access and modify your recipes.
     * @param handlerName - The name of your recipe handler. Make it something unique. This String is what other mods
     * will be using to access your recipe handler.
     * @param handler - An instance of your IRecipeHandler
     */
    public static void registerRecipeHandler(String handlerName, IRecipeHandler handler)
    {
    	if(!RECIPE_HANDLERS.containsKey(handlerName))
    	{
    		RECIPE_HANDLERS.put(handlerName, handler);
    	}
    }
    
    public static IRecipeHandler getRecipeHandler(String name)
    {
    	return RECIPE_HANDLERS.get(name);
    }
    
    public static Map<String, IRecipeHandler> getAllCustomRecipes() { return RECIPE_HANDLERS; }

    /**
     * Replacement functions must be called before post mod initialization!
     */
    
    public static void replaceRecipe(CraftingRecipe recipeToReplace, CraftingRecipe newRecipe)
    {
    	for(CraftingRecipe recipe : SHAPED_RECIPES)
        {
            if(recipe.isEqual(recipeToReplace))
            {
            	recipe = newRecipe;
            }
        }
    }
    
    public static void replaceShapelessRecipe(CraftingRecipe recipeToReplace, CraftingRecipe newRecipe)
    {
    	for(CraftingRecipe recipe : SHAPELESS_RECIPES)
        {
            if(recipe.isEqual(recipeToReplace))
            {
            	recipe = newRecipe;
            }
        }
    }
    
    public static void replaceSmeltingRecipe(SmeltingRecipe recipeToReplace, SmeltingRecipe newRecipe)
    {
        for(SmeltingRecipe recipe : SMELTING_RECIPES)
        {
            if(recipe.isEqual(recipeToReplace))
            {
            	recipe = newRecipe;
            }
        }
    }
    
    /**
     * Called in post init by {@link #BasicComponenets} to add all recipes. Don't call this function.
     */
    public static void addRecipes()
    {
        for (CraftingRecipe recipe : SHAPED_RECIPES)
        {
            CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(recipe.output, recipe.input));
        }

        for (CraftingRecipe recipe : SHAPELESS_RECIPES)
        {
            CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(recipe.output, recipe.input));
        }

        for (SmeltingRecipe recipe : SMELTING_RECIPES)
        {
            FurnaceRecipes.smelting().addSmelting(recipe.input.itemID, recipe.input.getItemDamage(), recipe.output);
        }
    }
}
