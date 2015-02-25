package com.builtbroken.mc.lib.world.heat;

import com.builtbroken.jlib.data.science.units.TemperatureUnit;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.lib.world.edit.PlacementData;
import cpw.mods.fml.common.registry.FMLControlledNamespacedRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.config.Configuration;

import java.util.HashMap;

/**
 * Created by robert on 2/25/2015.
 */
public class HeatedBlockRegistry
{
    public static HashMap<Block, BlockConversionData> warm_up_conversion = new HashMap();
    public static HashMap<Block, BlockConversionData> cool_down_conversion = new HashMap();

    public static HashMap<Material, Integer> default_melting_point_mat = new HashMap();
    public static HashMap<Material, Integer> default_temp_mat = new HashMap();

    public static HashMap<Integer, Integer> default_temp_dim = new HashMap();

    static
    {
        default_temp_dim.put(1, 327);
        default_temp_dim.put(0, 293);
        default_temp_dim.put(-1, 227);

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

    public static PlacementData getResult(Block block)
    {
        BlockConversionData conversion = getData(block);
        if (conversion != null)
        {
            return conversion.resulting_block;
        }
        return null;
    }

    public static PlacementData getResultWarmUp(Block block, int temp)
    {
        return null;
    }

    public static PlacementData getResultCoolDown(Block block, int temp)
    {
        return null;
    }

    public static BlockConversionData getData(Block block)
    {
        if (warm_up_conversion.containsKey(block))
        {
            return warm_up_conversion.get(block);
        }
        return null;
    }

    public static void init(Configuration config)
    {
        config.setCategoryComment("Block_Heat_Conversions", "Conversion of one block into another when a lot of heat is added. \'Air\' as an entry means the block turned into dust");

        //Constant data
        addNewHeatingConversion(Blocks.ice, Blocks.water, (int) TemperatureUnit.Fahrenheit.conversion.toKelvin(32));
        addNewHeatingConversion(Blocks.obsidian, Blocks.lava, 1293);
        addNewHeatingConversion(Blocks.stone, Blocks.lava, 1293);
        addNewHeatingConversion(Blocks.grass, Blocks.dirt, 600); //Made up conversion

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
                    int temp = 0;

                    if (mat.getCanBurn())
                    {
                        temp = 600;
                    }

                    if (temp > 0)
                    {
                        String conversion = config.getString("", "Heat_Conversions", reg.getNameForObject(Blocks.air), "");

                        if (reg.getObject(conversion) != null)
                        {
                            Object c_obj = reg.getObject(conversion);
                            if (c_obj instanceof Block)
                            {
                                if (!warm_up_conversion.containsKey(obj))
                                {
                                    addNewHeatingConversion((Block) obj, (Block) c_obj, 600);
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
