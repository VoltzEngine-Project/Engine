package com.builtbroken.mc.lib.world.map.heat;

import com.builtbroken.jlib.data.science.units.TemperatureUnit;
import com.builtbroken.mc.imp.transform.vector.Location;
import com.builtbroken.mc.lib.data.heat.HeatedBlockRegistry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;

/**
 * Created by robert on 2/25/2015.
 */
public class HeatDataManager
{
    private static HashMap<Integer, HeatMap> dims = new HashMap();
    private static int default_return = 293;

    public static HeatMap getMapForWorld(World world)
    {
        return getMapForDim(world.provider.getDimension());
    }

    public static HeatMap getMapForDim(int dim)
    {
        if (!dims.containsKey(dim))
        {
            dims.put(dim, new HeatMap(dim));
        }
        return dims.get(dim);
    }

    public static int getTempKelvin(Location location)
    {
        return getTempKelvin(location.world, location.toBlockPos());
    }

    public static int getTempKelvin(World world, BlockPos pos)
    {
        //TODO get temp at location
        return HeatedBlockRegistry.getDefaultTemp(world, world.getBlockState(pos));
    }

    public static int getTempFahrenheit(Location location)
    {
        return (int) TemperatureUnit.Fahrenheit.conversion.fromKelvin(default_return);
    }

    public static int getTempCelsius(Location location)
    {
        return (int) TemperatureUnit.Celsius.conversion.fromKelvin(default_return);
    }
}
