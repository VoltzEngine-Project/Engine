package net.minecraft.src.basiccomponents;

import net.minecraft.src.universalelectricity.extend.TileEntityConductor;

public class TileEntityCopperWire extends TileEntityConductor
{
    @Override
    public double getResistance()
    {
        return 0.3;
    }
}
