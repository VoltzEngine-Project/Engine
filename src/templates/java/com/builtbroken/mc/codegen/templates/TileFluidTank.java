package com.builtbroken.mc.codegen.templates;

import com.builtbroken.mc.api.tile.ITankProvider;
import com.builtbroken.mc.codegen.processors.TileWrappedTemplate;
import com.builtbroken.mc.framework.logic.ITileNode;
import com.builtbroken.mc.framework.logic.annotations.TankProviderWrapped;
import com.builtbroken.mc.framework.logic.wrapper.TileEntityWrapper;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/1/2017.
 */
@TileWrappedTemplate(annotationName = "TankProviderWrapped")
public class TileFluidTank extends TileEntityWrapper implements IFluidHandler
{
    public TileFluidTank(ITileNode controller)
    {
        super(controller);
    }

    //#StartMethods#
    protected IFluidTank getFluidTank(ForgeDirection from, Fluid fluid)
    {
        if (getTileNode() instanceof ITankProvider)
        {
            return ((ITankProvider) getTileNode()).getTankForFluid(from, fluid);
        }
        return null;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
    {
        if (resource != null)
        {
            IFluidTank tank = getFluidTank(from, resource.getFluid());
            if (tank != null)
            {
                return tank.fill(resource, doFill);
            }
        }
        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
    {
        if (resource != null)
        {
            IFluidTank tank = getFluidTank(from, resource.getFluid());
            if (tank != null && tank.getFluid() != null && tank.getFluid().getFluid() == resource.getFluid())
            {
                return tank.drain(resource.amount, doDrain);
            }
        }
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
    {
        IFluidTank tank = getFluidTank(from, null);
        if (tank != null && tank.getFluid() != null)
        {
            return tank.drain(maxDrain, doDrain);
        }
        return null;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid)
    {
        if (getTileNode() instanceof ITankProvider)
        {
            return ((ITankProvider) getTileNode()).canFill(from, fluid);
        }
        return false;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid)
    {
        if (getTileNode() instanceof ITankProvider)
        {
            return ((ITankProvider) getTileNode()).canDrain(from, fluid);
        }
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from)
    {
        return new FluidTankInfo[0];
    }
    //#EndMethods#
}
