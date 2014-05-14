package resonant.lib.recipe;

import ic2.api.item.Items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/** Allows recipes that are compatible in priority of: UE -> IC2 -> Buildcraft.
 * 
 * @author Calclavia */
public class UniversalRecipe
{
    public static final ArrayList<UniversalRecipe> RECIPES = new ArrayList<UniversalRecipe>();

    /** Primary Metal: Steel */
    public static final UniversalRecipe PRIMARY_METAL;
    public static final UniversalRecipe PRIMARY_PLATE;

    /** Secondary Metal: Bronze */
    public static final UniversalRecipe SECONDARY_METAL;
    public static final UniversalRecipe SECONDARY_PLATE;

    /** Circuits */
    public static final UniversalRecipe CIRCUIT_T1;
    public static final UniversalRecipe CIRCUIT_T2;
    public static final UniversalRecipe CIRCUIT_T3;

    /** Battery */
    public static final UniversalRecipe BATTERY;
    public static final UniversalRecipe ADVANCED_BATTERY;
    public static final UniversalRecipe BATTERY_BOX;

    /** Misc */
    public static final UniversalRecipe WRENCH;
    public static final UniversalRecipe WIRE;
    public static final UniversalRecipe MOTOR;
    public static final UniversalRecipe RUBBER;

    static
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
        WIRE = new UniversalRecipe("wire", "ironWire", "copperCableBlock", new ItemStack(Item.redstone));

        MOTOR = new UniversalRecipe("motor", "gear", Items.getItem("generator"), new ItemStack(Block.pistonBase));
        RUBBER = new UniversalRecipe("rubber", "itemRubber", new ItemStack(Item.slimeBall));
    }

    public String defaultRecipe;
    private final ArrayList alternatives = new ArrayList();
    private final Object[] originalRecipes;
    private boolean didGenerate = false;

    public UniversalRecipe(String defaultRecipe, Object... alternativeRecipes)
    {
        this.defaultRecipe = defaultRecipe;
        this.originalRecipes = alternativeRecipes;
        RECIPES.add(this);
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

    /** Gets the most preferred recipe item based on alternatives.
     * 
     * @return - Either a String for the OreDict recipe name, or an ItemStack */
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

    /** @param itemStacks
     * @return */
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

}