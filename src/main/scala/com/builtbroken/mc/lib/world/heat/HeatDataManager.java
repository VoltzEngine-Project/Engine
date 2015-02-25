package com.builtbroken.mc.lib.world.heat;

import com.builtbroken.jlib.data.science.units.TemperatureUnit;
import com.builtbroken.mc.lib.transform.vector.Location;
import net.minecraft.world.World;

import java.util.HashMap;

/**
 * Created by robert on 2/25/2015.
 */
public class HeatDataManager
{
    private static HashMap<Integer, WorldHeatMap> dims = new HashMap();
    private static int default_return = 293;

    public static WorldHeatMap getMapForWorld(World world)
    {
        return getMapForDim(world.provider.dimensionId);
    }

    public static WorldHeatMap getMapForDim(int dim)
    {
        if(!dims.containsKey(dim))
        {
            dims.put(dim, new WorldHeatMap(dim));
        }
        return dims.get(dim);
    }

    public static int getTempKelvin(Location location)
    {
        return default_return;
    }

    public static int getTempFahrenheit(Location location)
    {
        return (int)TemperatureUnit.Fahrenheit.conversion.fromKelvin(default_return);
    }

    public static int getTempCelsius(Location location)
    {
        return (int)TemperatureUnit.Celsius.conversion.fromKelvin(default_return);
    }
}
