package net.minecraft.src.universalelectricity.recipe;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.CraftingManager;
import net.minecraft.src.FurnaceRecipes;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
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
public class UERecipeManager
{
	//Crafting Recipes
    private static final List<UERecipe> shapedRecipes = new ArrayList<UERecipe>();
    private static final List<UERecipe> shapelessRecipes = new ArrayList<UERecipe>();
    //Smelting Recipes
    private static final List<UEFurnaceRecipe> furnaceRecipes = new ArrayList<UEFurnaceRecipe>();

    //Custom recipes for UE machines
    private static final List<UECustomRecipe> customRecipes = new ArrayList<UECustomRecipe>();
    
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
    
    public static List<UERecipe> getRecipes() { return shapedRecipes; }

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
    
    public static List<UERecipe> getShapelessRecipes() { return shapelessRecipes; }

    //Furnace Smelting Recipes
    public static void addSmelting(ItemStack input, ItemStack output)
    {
        furnaceRecipes.add(new UEFurnaceRecipe(output, input));
    }

    public static void addSmelting(Item input, ItemStack output)
    {
        addSmelting(new ItemStack(input), output);
    }
    
    public static List<UEFurnaceRecipe> getSmeltingRecipes() { return furnaceRecipes; }
    
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
    	customRecipes.add(new UECustomRecipe(name, output, input));
    }
    
    public static List<UECustomRecipe> getCustomRecipesByName(String name)
    {
    	List<UECustomRecipe> returnArray = new ArrayList<UECustomRecipe>();
    	
    	for(UECustomRecipe recipe: customRecipes)
    	{
    		if(recipe.name == name)
    		{
    			returnArray.add(recipe);
    		}
    	}
    	
    	return returnArray;
    }
    
    public static List<UECustomRecipe> getAllCustomRecipes() { return customRecipes; }

    //Adds all recipes and checks for recipe replacements
    public static void initialize()
    {
        //Replace all recipes
        for (NetworkMod addon : UniversalElectricity.addons)
        {
            if (addon instanceof IRecipeReplacementHandler)
            {
                IRecipeReplacementHandler recipeHandler = (IRecipeReplacementHandler)addon;

                for (UERecipe recipe : shapedRecipes)
                {
                    if (recipeHandler.onReplaceShapedRecipe(recipe) != null)
                    {
                        shapedRecipes.set(shapedRecipes.indexOf(recipe), recipeHandler.onReplaceRecipe(recipe));
                    }
                }

                for (UERecipe recipe : shapelessRecipes)
                {
                    if (recipeHandler.onReplaceShapelessRecipe(recipe) != null)
                    {
                        shaplessRecipes.set(shaplessRecipes.indexOf(recipe), recipeHandler.onReplaceRecipe(recipe));
                    }
                }

                for (UEFurnaceRecipe recipe : furnaceRecipes)
                {
                    if (recipeHandler.onReplaceSmeltingRecipe(recipe) != null)
                    {
                        furnaceRecipes.set(furnaceRecipes.indexOf(recipe), recipeHandler.onReplaceRecipe(recipe));
                    }
                }
            }
        }

        for (UERecipe recipe : shapedRecipes)
        {
            CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(recipe.output, recipe.input));
        }

        for (UERecipe recipe : shapelessRecipes)
        {
            CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(recipe.output, recipe.input));
        }

        for (UEFurnaceRecipe recipe : furnaceRecipes)
        {
            FurnaceRecipes.smelting().addSmelting(recipe.input.itemID, recipe.input.getItemDamage(), recipe.output);
        }
    }
}
