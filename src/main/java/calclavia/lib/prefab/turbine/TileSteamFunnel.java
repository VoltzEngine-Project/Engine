package calclavia.lib.prefab.turbine;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import calclavia.api.resonantinduction.IBoilHandler;
import calclavia.lib.prefab.tile.TileAdvanced;

/**
 * Funnel TileEntity
 */
public class TileSteamFunnel extends TileAdvanced implements IBoilHandler
{
	public final FluidTank tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME);

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (tank.getFluidAmount() > 0)
		{
			TileEntity tileEntity = this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord + 1, this.zCoord);

			if (tileEntity instanceof IFluidHandler)
			{
				IFluidHandler handler = (IFluidHandler) tileEntity;
				Fluid steam = FluidRegistry.getFluid("steam");

				if (handler.canFill(ForgeDirection.DOWN, steam))
				{
					FluidStack drainedStack = tank.drain(tank.getCapacity() / 10, false);

					if (drainedStack != null)
					{
						tank.drain(handler.fill(ForgeDirection.DOWN, drainedStack, true), true);
					}
				}
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		tank.writeToNBT(tag);
	}

	@Override
	public void writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);
		tank.readFromNBT(tag);
	}

	/**
	 * Tank Methods
	 */

	/* IFluidHandler */
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		return tank.fill(resource, doFill);
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		return this.tank.drain(maxDrain, doDrain);
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
	{
		if (resource == null || !resource.isFluidEqual(tank.getFluid()))
		{
			return null;
		}
		return tank.drain(resource.amount, doDrain);
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid)
	{
		if (fluid.getName().equalsIgnoreCase("steam") && from == ForgeDirection.DOWN)
		{
			return true;
		}

		return false;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid)
	{
		if (fluid.getName().equalsIgnoreCase("steam"))
		{
			return true;
		}

		return false;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from)
	{
		return new FluidTankInfo[] { this.tank.getInfo() };
	}
}
