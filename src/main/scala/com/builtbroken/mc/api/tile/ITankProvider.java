package com.builtbroken.mc.api.tile;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.IFluidTank;

/**
 * Created by Dark on 8/9/2015.
 */
public interface ITankProvider
{
    IFluidTank getTankForFluid(Fluid fluid);
}
