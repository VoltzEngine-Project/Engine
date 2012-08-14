package universalelectricity.recipe;

import java.util.ArrayList;
import java.util.List;

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
    private static final List<Recipe> shapedRecipes = new ArrayList<Recipe>();
    private static final List<Recipe> shapelessRecipes = new ArrayList<Recipe>();
    //Smelting Recipes
    private static final List<SmeltingRecipe> smeltingRecipes = new ArrayList<SmeltingRecipe>();

    //Custom recipes for UE machines
    private static final List<CustomRecipe> customRecipes = new ArrayList<CustomRecipe>();
    
    //Shaped Recipes
    public static void addRecipe(ItemStack output, Object[] input)
    {
        shapedRecipes.add(new Recipe(output, input));
    }

    public static void addRecipe(Item output, Object[] input)
    {
        addRecipe(new ItemStack(output), input);
    }

    public static void addRecipe(Block output, Object[] input)
    {
        addRecipe(new ItemStack(output), input);
    }
    
    public static List<Recipe> getRecipes() { return shapedRecipes; }
    
    public static Recipe getRecipeByOutput(ItemStack output)
    {
    	for(Recipe recipe : shapedRecipes)
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
        shapelessRecipes.add(new Recipe(output, input));
    }

    public static void addShapelessRecipe(Item output, Object[] input)
    {
        addShapelessRecipe(new ItemStack(output), input);
    }

    public static void addShapelessRecipe(Block output, Object[] input)
    {
        addShapelessRecipe(new ItemStack(output), input);
    }
    
    public static List<Recipe> getShapelessRecipes() { return shapelessRecipes; }
    
    public static Recipe getShapelessRecipeByOutput(ItemStack output)
    {
    	for(Recipe recipe : shapelessRecipes)
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
        smeltingRecipes.add(new SmeltingRecipe(output, input));
    }

    public static void addSmelting(Item input, ItemStack output)
    {
        addSmelting(new ItemStack(input), output);
    }
    
    public static List<SmeltingRecipe> getSmeltingRecipes() { return smeltingRecipes; }
    
    public static SmeltingRecipe getSmeltingRecipeByOutput(ItemStack output)
    {
    	for(SmeltingRecipe recipe : smeltingRecipes)
        {
            if(recipe.output == output)
            {
            	return recipe;
            }
        }
		return null;
    }
    
    /**
     * Adds a custom recipe for a custom UE machine that other mods can access.
     * 
     * @param name - The name of your machine. Grinder will have the name "Grinder".
     * 				Other mods will be using this name to find the recipes 
     * 				corresponding to the MACHINE (not the output item!).
     * @param input - All possible inputs in as an object array.
     * @param output - All possible outputs from this input as an object array.
     */
    public static void addCustomRecipe(String name, Object[] input, Object[] output)
    {
    	customRecipes.add(new CustomRecipe(name, output, input));
    }
    
    public static List<CustomRecipe> getCustomRecipesByName(String name)
    {
    	List<CustomRecipe> returnArray = new ArrayList<CustomRecipe>();
    	
    	for(CustomRecipe recipe: customRecipes)
    	{
    		if(recipe.name == name)
    		{
    			returnArray.add(recipe);
    		}
    	}
    	
    	return returnArray;
    }
    
    public static List<CustomRecipe> getAllCustomRecipes() { return customRecipes; }

    /**
     * Replacement functions must be called before post mod initialization!
     */
    
    public static void replaceRecipe(Recipe recipeToReplace, Recipe newRecipe)
    {
    	for(Recipe recipe : shapedRecipes)
        {
            if(recipe.isEqual(recipeToReplace))
            {
            	recipe = newRecipe;
            }
        }
    }
    
    public static void replaceShapelessRecipe(Recipe recipeToReplace, Recipe newRecipe)
    {
    	for(Recipe recipe : shapelessRecipes)
        {
            if(recipe.isEqual(recipeToReplace))
            {
            	recipe = newRecipe;
            }
        }
    }
    
    public static void replaceSmeltingRecipe(SmeltingRecipe recipeToReplace, SmeltingRecipe newRecipe)
    {
        for(SmeltingRecipe recipe : smeltingRecipes)
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
        for (Recipe recipe : shapedRecipes)
        {
            CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(recipe.output, recipe.input));
        }

        for (Recipe recipe : shapelessRecipes)
        {
            CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(recipe.output, recipe.input));
        }

        for (SmeltingRecipe recipe : smeltingRecipes)
        {
            FurnaceRecipes.smelting().addSmelting(recipe.input.itemID, recipe.input.getItemDamage(), recipe.output);
        }
    }
}
