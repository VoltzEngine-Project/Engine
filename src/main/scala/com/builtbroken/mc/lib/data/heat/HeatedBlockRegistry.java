package com.builtbroken.mc.lib.data.heat;

import com.builtbroken.jlib.data.science.units.TemperatureUnit;
import com.builtbroken.mc.api.VoltzEngineAPI;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.imp.transform.vector.BlockPos;
import com.builtbroken.mc.lib.world.edit.PlacementData;
import com.builtbroken.mc.lib.world.map.heat.HeatDataManager;
import cpw.mods.fml.common.registry.FMLControlledNamespacedRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;

import java.util.HashMap;

/**
 * Created by robert on 2/25/2015.
 */
public class HeatedBlockRegistry
{
    /** Block to heated block, ex ice to water */
    public static HashMap<Block, BlockConversionData> warm_up_conversion = new HashMap();//TODO add handlers and meta support
    /** Block to cooled block, ex water to ice */
    public static HashMap<Block, BlockConversionData> cool_down_conversion = new HashMap(); //TODO add handlers and meta support

    /** Default melting point of a material, used as a backup value */
    public static HashMap<Material, Integer> default_melting_point_mat = new HashMap(); //TODO add handlers
    /** Default temperature of the material, used only if something does not match world temp... eg. lava & fire */
    public static HashMap<Material, Integer> default_temp_mat = new HashMap();

    /** Defaults for materials, Specific heat is the amount of energy it takes to heat 1 gram of matter by 1 degree C */
    public static HashMap<Material, Float> default_specific_heat_mat = new HashMap(); //TODO calculate and cache per block (add support for blocks made of several materials by averaging)
    public static HashMap<Material, Float> default_fusion_heat_mat = new HashMap();
    public static HashMap<Material, Float> default_vaporization_mat = new HashMap();

    /** Default background temperature of a world */
    public static HashMap<Integer, Integer> default_temp_dim = new HashMap();

    //TODO get a biome temp default values
    //TODO get vaporization point of material, e.g. block -> nothing

    static
    {
        //TODO move to JSON file
        default_temp_dim.put(1, 327);
        default_temp_dim.put(0, 293);
        default_temp_dim.put(-1, 227); //TODO loop worlds to apply default temp based on world type

        default_melting_point_mat.put(Material.carpet, 600);
        default_melting_point_mat.put(Material.cloth, 600);
        default_melting_point_mat.put(Material.wood, 800);
        default_melting_point_mat.put(Material.rock, 1200);
        default_melting_point_mat.put(Material.clay, 2000);
        default_melting_point_mat.put(Material.anvil, 1900);
        default_melting_point_mat.put(Material.iron, 1900);
        default_melting_point_mat.put(Material.water, 373);
        default_melting_point_mat.put(Material.snow, 273);
        default_melting_point_mat.put(Material.ice, 273);
        default_melting_point_mat.put(Material.packedIce, 273);
        default_melting_point_mat.put(Material.craftedSnow, 273);

        default_temp_mat.put(Material.ice, 253); //slightly bellow freezing
        default_temp_mat.put(Material.packedIce, 253);
        default_temp_mat.put(Material.snow, 253);
        default_temp_mat.put(Material.craftedSnow, 253);
    }

    /**
     * Called to calculate how much energy it would take to heat
     * the block at the location in world to the new temperature
     *
     * @param world   - world the block is inside
     * @param pos     - position of the block
     * @param newTemp - desired temperature
     * @return energy cost to change the temperature, negative means a loss of energy from the block
     */
    public static float getEnergyNeededToHeat(World world, BlockPos pos, int newTemp)
    {
        Block block = pos.getBlock(world);
        if (block != null)
        {
            float mass = (float) VoltzEngineAPI.massRegistry.getMass(block);
            int actual_temp = HeatDataManager.getTempKelvin(world, pos.x, pos.y, pos.z);
            float change = TemperatureUnit.Celsius.conversion.fromKelvin(Math.abs(actual_temp - newTemp));
            if (change >= 1)
            {
                if (newTemp > actual_temp)
                {
                    ////http://hyperphysics.phy-astr.gsu.edu/hbase/thermo/spht.html
                    //http://hyperphysics.phy-astr.gsu.edu/hbase/thermo/Dulong.html#c1
                    //http://hyperphysics.phy-astr.gsu.edu/hbase/Tables/sphtt.html#c1
                    // Q = c * m * delta T
                    // Q = heat added
                    // c = specific heat        4.186j/g
                    // m = mass
                    // delta T = change in temp C
                    return getSpecificHeat(block) * mass * change;

                    //TODO implement vaporization Q = m * L
                    //Q = energy
                    //m = mass
                    //L = vaporization heat
                    // 2600j/g for water

                    //TODO implement fusion
                    // same equation but L is heat of fusion
                    // 333j/g for water
                }
                else
                {
                    return -getSpecificHeat(block) * mass * change; //TODO see if this is correct
                }
            }
            return 0;
        }
        return -1; //No data to heat
    }

    public static void addNewHeatingConversion(Block block, Block result, int kelvin)
    {
        addNewHeatingConversion(block, new PlacementData(result, -1), kelvin);
    }

    public static void addNewHeatingConversion(Block block, PlacementData result, int kelvin)
    {
        addNewHeatingConversion(new PlacementData(block, -1), result, kelvin);
    }

    public static void addNewHeatingConversion(PlacementData block, PlacementData result, int kelvin)
    {
        if (warm_up_conversion.containsKey(block.block()))
        {
            Engine.instance.logger().error("HeatedBlockRegistry: Block[" + block + "] conversion to " + warm_up_conversion.get(block.block()) + " is being replaced by " + result);

        }
        warm_up_conversion.put(block.block(), new BlockConversionData(block, result, kelvin));
    }

    public static void addNewCoolingConversion(Block block, Block result, int kelvin)
    {
        addNewCoolingConversion(block, new PlacementData(result, -1), kelvin);
    }

    public static void addNewCoolingConversion(Block block, PlacementData result, int kelvin)
    {
        addNewCoolingConversion(new PlacementData(block, -1), result, kelvin);
    }

    public static void addNewCoolingConversion(PlacementData block, PlacementData result, int kelvin)
    {
        if (cool_down_conversion.containsKey(block.block()))
        {
            Engine.instance.logger().error("HeatedBlockRegistry: Block[" + block + "] conversion to " + warm_up_conversion.get(block.block()) + " is being replaced by " + result);

        }
        cool_down_conversion.put(block.block(), new BlockConversionData(block, result, kelvin));
    }

    public static float getSpecificHeat(Block block)
    {
        if (default_specific_heat_mat.containsKey(block.blockMaterial))
        {
            return default_specific_heat_mat.get(block.blockMaterial);
        }
        return 1;
    }

    public static PlacementData getResult(Block block)
    {
        BlockConversionData conversion = getWarnUpData(block);
        if (conversion != null)
        {
            return conversion.resulting_block;
        }
        return null;
    }

    public static PlacementData getResultWarmUp(Block block, int temp)
    {
        BlockConversionData conversion = getWarnUpData(block);
        if (conversion != null && conversion.temp_kelvin <= temp)
        {
            return conversion.resulting_block;
        }
        return null;
    }

    public static PlacementData getResultCoolDown(Block block, int temp)
    {
        BlockConversionData conversion = getCoolDownData(block);
        if (conversion != null && conversion.temp_kelvin >= temp)
        {
            return conversion.resulting_block;
        }
        return null;
    }

    public static BlockConversionData getWarnUpData(Block block)
    {
        if (warm_up_conversion.containsKey(block))
        {
            return warm_up_conversion.get(block);
        }
        return null;
    }

    public static BlockConversionData getCoolDownData(Block block)
    {
        if (cool_down_conversion.containsKey(block))
        {
            return cool_down_conversion.get(block);
        }
        return null;
    }

    public static int getDefaultTemp(World world, Block block)
    {
        Material mat = block.getMaterial();
        if (default_temp_mat.containsKey(mat))
        {
            return default_temp_mat.get(mat);
        }
        else if (default_temp_dim.containsKey(world.provider.dimensionId))
        {
            return default_temp_dim.get(world.provider.dimensionId);
        }
        return 293; //20c, 69f, room temp
    }

    public static void init(Configuration config)
    {
        config.setCategoryComment("Block_Heat_Conversions", "Conversion of one block into another when a lot of heat is added. \'Air\' as an entry means the block turned into dust");

        //Vanilla block handling
        //---------------------------------------------------------------------

        //Heating values
        addNewHeatingConversion(Blocks.ice, Blocks.water, (int) TemperatureUnit.Fahrenheit.conversion.toKelvin(32));
        addNewHeatingConversion(Blocks.obsidian, Blocks.lava, 1293);
        addNewHeatingConversion(Blocks.grass, Blocks.dirt, 600); //Made up conversion

        //Cooling values
        addNewCoolingConversion(Blocks.water, Blocks.ice, 273);
        addNewCoolingConversion(Blocks.lava, Blocks.obsidian, 1200); //made up value

        //---------------------------------------------------------------------

        //Everything else not registered, init with default data to make life simple
        if (Block.blockRegistry instanceof FMLControlledNamespacedRegistry)
        {
            FMLControlledNamespacedRegistry reg = ((FMLControlledNamespacedRegistry) Block.blockRegistry);
            for (Object obj : reg.typeSafeIterable())
            {
                if (obj instanceof Block)
                {
                    String name = reg.getNameForObject(obj);
                    Material mat = ((Block) obj).getMaterial();
                    Block blockToConvertTo = Blocks.air;
                    int temp = 0;

                    if (mat.getCanBurn())
                    {
                        temp = 600;
                    }
                    else if (mat == Material.rock)
                    {
                        temp = 1293;
                        blockToConvertTo = Blocks.lava;
                    }

                    if (temp > 0)
                    {
                        String conversion = config.getString(name, "Heat_Conversions", reg.getNameForObject(blockToConvertTo), "");

                        if (reg.getObject(conversion) != null)
                        {
                            Object c_obj = reg.getObject(conversion);
                            if (c_obj instanceof Block)
                            {
                                if (!warm_up_conversion.containsKey(obj))
                                {
                                    if (blockToConvertTo == Blocks.lava)
                                    {
                                        addNewHeatingConversion((Block) obj, new PlacementData(Blocks.cobblestone, 0), temp);
                                    }
                                    else
                                    {
                                        addNewHeatingConversion((Block) obj, (Block) c_obj, temp);
                                    }
                                }
                            }
                            else
                            {
                                Engine.instance.logger().error("Error c_obj is not an instance of a block");
                            }
                        }
                        else
                        {
                            Engine.instance.logger().error("Config entry for heat conversion " + name + " has an invalid conversion of " + conversion);
                        }
                    }
                }
            }
        }
    }
}
