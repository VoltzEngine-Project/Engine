package com.builtbroken.mc.lib.helper.recipe;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;

/**
 * Allows recipes that are compatible in priority of: UE -> IC2 -> Buildcraft.
 *
 * @author Calclavia, Darkguardsman
 */
public class UniversalRecipe
{
    public static final ArrayList<UniversalRecipe> RECIPES = new ArrayList<>();

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

    private ArrayList alt_data;
    private String default_data;
    private Object recipe_item;
    private boolean didGenerate = false;

    static
    {
        // Metals
        PRIMARY_METAL = new UniversalRecipe("ingotSteel", "plateIron", new ItemStack(Items.iron_ingot));
        SECONDARY_METAL = new UniversalRecipe("ingotBronze", new ItemStack(Items.brick));

        // Plates
        PRIMARY_PLATE = new UniversalRecipe("plateSteel", "advancedAlloy", new ItemStack(Blocks.iron_block));
        SECONDARY_PLATE = new UniversalRecipe("plateBronze", "carbonPlate", new ItemStack(Blocks.brick_block));
        // Miscs
        CIRCUIT_T1 = new UniversalRecipe("circuitBasic", "electronicCircuit", new ItemStack(Items.redstone));
        CIRCUIT_T2 = new UniversalRecipe("circuitAdvanced", "advancedCircuit", new ItemStack(Items.repeater));
        CIRCUIT_T3 = new UniversalRecipe("circuitElite", "iridiumPlate", new ItemStack(Items.comparator));

        ADVANCED_BATTERY = new UniversalRecipe("advancedBattery", "energyCrystal", "battery", new ItemStack(Items.repeater));
        BATTERY = new UniversalRecipe("battery", "reBattery", new ItemStack(Items.repeater));
        BATTERY_BOX = new UniversalRecipe("batteryBox", "batBox", new ItemStack(Blocks.gold_block));

        WRENCH = new UniversalRecipe("wrench", "wrench", new ItemStack(Items.iron_axe));
        WIRE = new UniversalRecipe("wire", "copperWire", "ironWire", "copperCableBlock", new ItemStack(Items.redstone));

        MOTOR = new UniversalRecipe("motor", "gear", "generator", new ItemStack(Blocks.piston));
        RUBBER = new UniversalRecipe("rubber", "itemRubber", new ItemStack(Items.slime_ball));
    }

    public UniversalRecipe(String defaultRecipe, Object... alternativeRecipes)
    {
        this.default_data = defaultRecipe;
        alt_data = new ArrayList();
        for (Object object : alternativeRecipes)
            alt_data.add(object);
        RECIPES.add(this);
    }

    public void addAltData(Object object)
    {
        this.addAltData(object);
    }

    /**
     * Gets the most preferred recipe item based on alternatives.
     *
     * @return - Either a String for the OreDict recipe name, or an ItemStack
     */
    public Object get()
    {
        if (!didGenerate)
        {
            if (OreDictionary.getOres(default_data).size() > 0)
            {
                recipe_item = default_data;
                return recipe_item;
            }

            for (Object alt : alt_data)
            {
                if (alt instanceof String && OreDictionary.getOres((String) alt).size() > 0)
                {
                    recipe_item = alt;
                    return recipe_item;
                }
                else if (alt instanceof Item || alt instanceof Block || alt instanceof ItemStack)
                {
                    recipe_item = alt;
                    return recipe_item;
                }
            }
            recipe_item = Blocks.fire;
        }
        return recipe_item;
    }

}