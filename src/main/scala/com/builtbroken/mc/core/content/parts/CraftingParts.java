package com.builtbroken.mc.core.content.parts;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.content.resources.items.ItemSheetMetal;
import com.builtbroken.mc.lib.helper.recipe.OreNames;
import com.builtbroken.mc.lib.helper.recipe.UniversalRecipe;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Enum of parts only used for crafting recipes or upgrades
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 11/14/2016.
 */
@Deprecated //TODO move to JSON
public enum CraftingParts
{
    /** Coil of copper wire */
    COPPER_COIL("copperCoil", "coilCopper"),
    /** More expensive motor */
    DC_MOTOR("dcMotor", "motorDC"),
    /** Simple motor */
    MOTOR("motor", "motor"),
    /** Upgrade of the DC motor with more control */
    STEPPER_MOTOR("stepperMotor", "motorStepper"),
    /** 4 Coils wired together */
    STEPPER_MOTOR_COIL_ASSEMBLY("copperCoilStepper", "copperCoilStepper");

    public final String oreName;
    public final String name;
    /** Only stored during load time, cleared when used */
    public List<IRecipe> recipes;
    protected IIcon icon;

    CraftingParts(String name, String oreName)
    {
        this.name = name;
        this.oreName = oreName;
    }

    /** Generates a stack for the part */
    public ItemStack toStack()
    {
        return new ItemStack(Engine.itemCraftingParts, 1, ordinal());
    }

    /**
     * Generates a stack
     *
     * @param sum - amount
     * @return stack with ammount
     */
    public ItemStack toStack(int sum)
    {
        return new ItemStack(Engine.itemCraftingParts, sum, ordinal());
    }

    /** Called to load the recipes into memory */
    public static void loadRecipes()
    {
        final Object casingItem;
        if (Engine.itemSheetMetal != null)
        {
            casingItem = ItemSheetMetal.SheetMetal.EIGHTH.stack();
        }
        else
        {
            casingItem = OreNames.INGOT_IRON;
        }

        COPPER_COIL.recipes = new ArrayList();
        COPPER_COIL.recipes.add(new ShapedOreRecipe(COPPER_COIL.toStack(6), "WPW", "WSW", "WPW", 'W', OreNames.WIRE_COPPER, 'P', Items.paper, 'S', OreNames.WOOD_STICK));

        MOTOR.recipes = new ArrayList();
        MOTOR.recipes.add(new ShapedOreRecipe(MOTOR.toStack(), "WCI", "CRC", "WCI", 'I', casingItem, 'C', COPPER_COIL.oreName, 'W', OreNames.WIRE_COPPER, 'R', OreNames.ROD_IRON));

        DC_MOTOR.recipes = new ArrayList();
        DC_MOTOR.recipes.add(new ShapedOreRecipe(DC_MOTOR.toStack(), "CWC", "RMW", "CWC", 'C', COPPER_COIL.oreName, 'W', OreNames.WIRE_COPPER, 'R', UniversalRecipe.CIRCUIT_T1.get(), 'M', MOTOR.oreName));

        STEPPER_MOTOR_COIL_ASSEMBLY.recipes = new ArrayList();
        STEPPER_MOTOR_COIL_ASSEMBLY.recipes.add(new ShapedOreRecipe(STEPPER_MOTOR_COIL_ASSEMBLY.toStack(), "WCW", "CIC", "WCW", 'I', casingItem, 'C', COPPER_COIL.oreName, 'W', OreNames.WIRE_COPPER));

        STEPPER_MOTOR.recipes = new ArrayList();
        STEPPER_MOTOR.recipes.add(new ShapedOreRecipe(STEPPER_MOTOR.toStack(), "IIW", "GRC", "IIW", 'I', casingItem, 'C', STEPPER_MOTOR_COIL_ASSEMBLY.oreName, 'W', OreNames.WIRE_COPPER, 'G', OreNames.GEAR_IRON, 'R', OreNames.ROD_IRON));
    }
}
