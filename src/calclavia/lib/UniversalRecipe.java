package calclavia.lib;

import ic2.api.item.Items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.FMLLog;

/**
 * Allows recipes that are compatible in priority of: UE -> IC2 -> Buildcraft.
 * 
 * @author Calclavia
 * 
 */
public class UniversalRecipe
{
	/**
	 * Primary Metal: Steel
	 */
	public static UniversalRecipe PRIMARY_METAL;
	public static UniversalRecipe PRIMARY_PLATE;

	/**
	 * Secondary Metal: Bronze
	 */
	public static UniversalRecipe SECONDARY_METAL;
	public static UniversalRecipe SECONDARY_PLATE;

	/**
	 * Circuits
	 */
	public static UniversalRecipe CIRCUIT_T1;
	public static UniversalRecipe CIRCUIT_T2;
	public static UniversalRecipe CIRCUIT_T3;

	/**
	 * Battery
	 */
	public static UniversalRecipe BATTERY;
	public static UniversalRecipe ADVANCED_BATTERY;
	public static UniversalRecipe BATTERY_BOX;

	/**
	 * Misc
	 */
	public static UniversalRecipe WRENCH;
	public static UniversalRecipe WIRE;
	public static UniversalRecipe MOTOR;

	private static boolean isInit = false;

	public static boolean MARK_ALLOW_ALTERNATIVES = true;

	public static void init()
	{
		if (!isInit)
		{
			// Metals
			PRIMARY_METAL = new UniversalRecipe("ingotSteel", "ingotRefinedIron", new ItemStack(Item.ingotIron));
			SECONDARY_METAL = new UniversalRecipe("ingotBronze", new ItemStack(Item.brick));

			// Plates
			PRIMARY_PLATE = new UniversalRecipe("plateSteel", Items.getItem("advancedAlloy"), new ItemStack(Block.blockIron));
			SECONDARY_PLATE = new UniversalRecipe("plateBronze", Items.getItem("carbonPlate"), new ItemStack(Block.brick));
			// Miscs
			CIRCUIT_T1 = new UniversalRecipe("circuitBasic", Items.getItem("electronicCircuit"), new ItemStack(Block.torchRedstoneActive));
			CIRCUIT_T2 = new UniversalRecipe("circuitAdvanced", Items.getItem("advancedCircuit"), new ItemStack(Item.redstoneRepeater));
			CIRCUIT_T3 = new UniversalRecipe("circuitElite", Items.getItem("iridiumPlate"), new ItemStack(Item.comparator));

			ADVANCED_BATTERY = new UniversalRecipe("advancedBattery", Items.getItem("energyCrystal"), "battery", new ItemStack(Item.redstoneRepeater));
			BATTERY = new UniversalRecipe("battery", Items.getItem("reBattery"), new ItemStack(Item.redstoneRepeater));
			BATTERY_BOX = new UniversalRecipe("batteryBox", Items.getItem("batBox"), new ItemStack(Block.blockGold));

			WRENCH = new UniversalRecipe("wrench", Items.getItem("wrench"), new ItemStack(Item.axeIron));
			WIRE = new UniversalRecipe("copperWire", "copperCableBlock", new ItemStack(Item.redstone));

			MOTOR = new UniversalRecipe("motor", Items.getItem("generator"), new ItemStack(Block.pistonBase));

			isInit = true;
		}
	}

	public String defaultRecipe;
	private final ArrayList alternatives = new ArrayList();

	public UniversalRecipe(String defaultRecipe, Object... alternativeRecipes)
	{
		this.defaultRecipe = defaultRecipe;

		if (recipeExists(this.defaultRecipe))
		{
			this.alternatives.add(this.defaultRecipe);
		}

		for (Object alternative : alternativeRecipes)
		{
			if (alternative instanceof ItemStack)
			{
				if (recipeExists((ItemStack) alternative))
				{
					this.alternatives.add(alternative);
				}

				continue;
			}
			else if (alternative instanceof String)
			{
				if (recipeExists((String) alternative))
				{
					this.alternatives.add(alternative);
				}

				continue;
			}
		}

	}

	/**
	 * Gets the most preferred recipe item based on alternatives.
	 * 
	 * @return - Either a String for the OreDict recipe name, or an ItemStack
	 */
	public Object get()
	{
		if (MARK_ALLOW_ALTERNATIVES)
		{
			return this.alternatives.get(0);
		}

		return this.defaultRecipe;
	}

	public static boolean recipeExists(List<ItemStack> itemStacks)
	{
		if (itemStacks != null)
		{
			for (ItemStack stack : itemStacks)
			{
				if (stack != null)
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

}