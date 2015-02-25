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
    public static HashMap<Block, BlockConversionData> data = new HashMap();
    public static HashMap<Material, Integer> default__conversion_temp_mat = new HashMap();

    static
    {
        default__conversion_temp_mat.put(Material.carpet, 600);
        default__conversion_temp_mat.put(Material.cloth, 600);
        default__conversion_temp_mat.put(Material.wood, 800);
        default__conversion_temp_mat.put(Material.rock, 1200);
        default__conversion_temp_mat.put(Material.clay, 2000);
        default__conversion_temp_mat.put(Material.anvil, 1900);
        default__conversion_temp_mat.put(Material.iron, 1900);
        default__conversion_temp_mat.put(Material.water, 393);
        default__conversion_temp_mat.put(Material.snow, 293);
        default__conversion_temp_mat.put(Material.ice, 293);
        default__conversion_temp_mat.put(Material.packedIce, 293);
    }

    public static void addNewConversion(Block block, Block result, int kelvin)
    {
        addNewConversion(block, new PlacementData(result, -1), kelvin);
    }

    public static void addNewConversion(Block block, PlacementData result, int kelvin)
    {
        addNewConversion(new PlacementData(block, -1), result, kelvin);
    }

    public static void addNewConversion(PlacementData block, PlacementData result, int kelvin)
    {
        if (data.containsKey(block.block()))
        {
            Engine.instance.logger().error("HeatedBlockRegistry: Block[" + block + "] conversion to " + data.get(block.block()) +" is being replaced by " + result);

        }
        data.put(block.block(), new BlockConversionData(block, result, kelvin));
    }

    public static void init(Configuration config)
    {
        config.setCategoryComment("Block_Heat_Conversions", "Conversion of one block into another when a lot of heat is added. \'Air\' as an entry means the block turned into dust");

        //Constant data
        addNewConversion(Blocks.ice, Blocks.water, (int) TemperatureUnit.Fahrenheit.conversion.toKelvin(32));
        addNewConversion(Blocks.obsidian, Blocks.lava, 1293);
        addNewConversion(Blocks.stone, Blocks.lava, 1293);
        addNewConversion(Blocks.grass, Blocks.dirt, 600); //Made up conversion

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
                                if (!data.containsKey(obj))
                                {
                                    addNewConversion((Block) obj, (Block) c_obj, 600);
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
