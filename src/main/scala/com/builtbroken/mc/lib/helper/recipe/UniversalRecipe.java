package com.builtbroken.mc.lib.helper.recipe;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Allows recipes that are compatible in priority of: UE -> IC2 -> Buildcraft.
 *
 * @author Calclavia
 */
public class UniversalRecipe
{
	public static final ArrayList<UniversalRecipe> RECIPES = new ArrayList<UniversalRecipe>();

	/**
	 * Primary Metal: Steel
	 */
	public static final UniversalRecipe PRIMARY_METAL;
	public static final UniversalRecipe PRIMARY_PLATE;

	/**
	 * Secondary Metal: Bronze
	 */
	public static final UniversalRecipe SECONDARY_METAL;
	public static final UniversalRecipe SECONDARY_PLATE;

	/**
	 * Circuits
	 */
	public static final UniversalRecipe CIRCUIT_T1;
	public static final UniversalRecipe CIRCUIT_T2;
	public static final UniversalRecipe CIRCUIT_T3;

	/**
	 * Battery
	 */
	public static final UniversalRecipe BATTERY;
	public static final UniversalRecipe ADVANCED_BATTERY;
	public static final UniversalRecipe BATTERY_BOX;

	/**
	 * Misc
	 */
	public static final UniversalRecipe WRENCH;
	public static final UniversalRecipe WIRE;
	public static final UniversalRecipe MOTOR;
	public static final UniversalRecipe RUBBER;

	//TODO: FIX!! SUCH A COMPATIBILITY BELONGS TO ICOMPATPROXY
	private final ArrayList alternatives = new ArrayList();
	private final Object[] originalRecipes;
	public String defaultRecipe;
	private boolean didGenerate = false;

	static
	{
		// Metals
		PRIMARY_METAL = new UniversalRecipe("ingotSteel", "plateiron", new ItemStack(Items.iron_ingot));
		SECONDARY_METAL = new UniversalRecipe("ingotBronze", new ItemStack(Items.brick));

		// Plates
		PRIMARY_PLATE = new UniversalRecipe("plateSteel", "advancedAlloy", new ItemStack(Blocks.iron_block));
		SECONDARY_PLATE = new UniversalRecipe("plateBronze", "carbonPlate", new ItemStack(Blocks.brick_block));
		// Miscs
		CIRCUIT_T1 = new UniversalRecipe("circuitBasic", "electronicCircuit", new ItemStack(Blocks.unlit_redstone_torch));
		CIRCUIT_T2 = new UniversalRecipe("circuitAdvanced", "advancedCircuit", new ItemStack(Items.repeater));
		CIRCUIT_T3 = new UniversalRecipe("circuitElite", "iridiumPlate", new ItemStack(Items.comparator));

		ADVANCED_BATTERY = new UniversalRecipe("advancedBattery", "energyCrystal", "battery", new ItemStack(Items.repeater));
		BATTERY = new UniversalRecipe("battery", "reBattery", new ItemStack(Items.repeater));
		BATTERY_BOX = new UniversalRecipe("batteryBox", "batBox", new ItemStack(Blocks.gold_block));

		WRENCH = new UniversalRecipe("wrench", "wrench", new ItemStack(Items.iron_axe));
		WIRE = new UniversalRecipe("wire", "ironWire", "copperCableBlock", new ItemStack(Items.redstone));

		MOTOR = new UniversalRecipe("motor", "gear", "generator", new ItemStack(Blocks.piston));
		RUBBER = new UniversalRecipe("rubber", "itemRubber", new ItemStack(Items.slime_ball));
	}

	public UniversalRecipe(String defaultRecipe, Object... alternativeRecipes)
	{
		this.defaultRecipe = defaultRecipe;
		this.originalRecipes = alternativeRecipes;
		RECIPES.add(this);
	}

	/**
	 * @param itemStacks
	 * @return
	 */
	public static boolean recipeExists(List<ItemStack> itemStacks)
	{
		if (itemStacks != null && itemStacks.size() > 0)
		{
			for (ItemStack stack : itemStacks)
			{
				if (stack == null)
				{
					return false;
				}
			}

			return true;
		}

		return false;
	}

	public static boolean recipeExists(ItemStack... itemStacks)
	{
		return recipeExists(Arrays.asList(itemStacks));
	}

	public static boolean recipeExists(String stackName)
	{
		return recipeExists(OreDictionary.getOres(stackName));
	}

	public void generate()
	{
		if (recipeExists(this.defaultRecipe))
		{
			this.alternatives.add(this.defaultRecipe);
		}

		for (Object alternative : this.originalRecipes)
		{
			if (alternative instanceof ItemStack)
			{
				if (recipeExists((ItemStack) alternative))
				{
					this.alternatives.add(alternative);
				}
			}
			else if (alternative instanceof String)
			{
				if (recipeExists((String) alternative))
				{
					this.alternatives.add(alternative);
				}
			}
		}

		didGenerate = true;
	}

	/**
	 * Gets the most preferred recipe item based on alternatives.
	 *
	 * @return - Either a String for the OreDict recipe name, or an ItemStack
	 */
	public Object get(boolean allowAlternatives)
	{
		if (!didGenerate)
		{
			generate();
		}

		if (allowAlternatives)
		{
			return this.alternatives.get(0);
		}

		return recipeExists(this.defaultRecipe) ? this.defaultRecipe : this.alternatives.get(this.alternatives.size() - 1);
	}

	public Object get()
	{
		return get(true);
	}

}