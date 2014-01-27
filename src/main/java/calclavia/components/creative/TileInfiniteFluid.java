package calclavia.components.creative;

import java.util.HashMap;

import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import calclavia.lib.prefab.tile.TileIO;

/**
 * Designed to debug fluid devices by draining everything that comes in at one time
 * 
 * @author DarkGuardsman
 */
public class TileInfiniteFluid extends TileIO implements IFluidHandler
{
	// TODO later add to this to make it actually have an ingame use other than debug
	public static HashMap<FluidStack, Long> storage = new HashMap<FluidStack, Long>();

	FluidTank tank = new FluidTank(Integer.MAX_VALUE);

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from)
	{
		return new FluidTankInfo[] { this.tank.getInfo() };
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		if (getInputDirections().contains(from))
			return resource.amount;

		return 0;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
	{
		if (getOutputDirections().contains(from))
			return resource;

		return null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		if (getOutputDirections().contains(from))
			return new FluidStack(FluidRegistry.WATER, maxDrain);

		return null;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid)
	{
		return getInputDirections().contains(from);
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid)
	{
		return getOutputDirections().contains(from);
	}

}
