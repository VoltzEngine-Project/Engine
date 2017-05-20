package com.builtbroken.mc.lib.data.heat;

import com.builtbroken.mc.lib.world.edit.PlacementData;

/** Data entry used to log which blocks turn into other blocks threw
 * heat exchange. Example ICE -> WATER
 * Created by robert on 2/25/2015.
 */
public class BlockConversionData
{
    //If you need to change this data just make a new object and replace this reference to this one
    //If you badly need to change it use the reflection helper
    public final PlacementData original_block;
    public final PlacementData resulting_block;
    public final int temp_kelvin;

    public BlockConversionData(PlacementData original, PlacementData result, int kelvin)
    {
        this.original_block = original;
        this.resulting_block = result;
        this.temp_kelvin = kelvin;
    }
}
