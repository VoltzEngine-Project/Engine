package com.builtbroken.mc.codegen.tests;

import com.builtbroken.mc.api.tile.provider.ITankProvider;
import com.builtbroken.mc.framework.logic.TileNode;
import com.builtbroken.mc.codegen.annotations.TankProviderWrapped;
import com.builtbroken.mc.codegen.annotations.TileWrapped;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/1/2017.
 */
@TileWrapped(className = ".tile.TileEntityWrapperTestTank")
@TankProviderWrapped()
public class TileFluidTank extends TileNode implements ITankProvider
{
    protected FluidTank tank;

    public TileFluidTank()
    {
        super("tile.test.fluid", "null");
    }

    @Override
    public IFluidTank getTankForFluid(Fluid fluid)
    {
        if(tank  == null)
        {
            tank = new FluidTank(1000);
        }
        return tank;
    }
}
