package com.builtbroken.lib.prefab.fluid

import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.fluids._

/**
 * Prefab Trait for IFluidHandler
 * @author Calclavia
 */
trait TFluidHandler extends IFluidHandler with TTankProvider
{
  override def fill(from: ForgeDirection, resource: FluidStack, doFill: Boolean): Int =
  {
    if (getPrimaryTank != null)
      return getPrimaryTank.fill(resource, doFill)
    else
      return 0
  }

  override def drain(from: ForgeDirection, resource: FluidStack, doDrain: Boolean): FluidStack =
  {
    if (getPrimaryTank != null && resource != null && resource.isFluidEqual(getPrimaryTank.getFluid))
    {
      return drain(from, resource.amount, doDrain)
    }
    return null
  }

  override def drain(from: ForgeDirection, maxDrain: Int, doDrain: Boolean): FluidStack =
  {
    if (getPrimaryTank != null)
      return getPrimaryTank.drain(maxDrain, doDrain)
    else
      return null
  }

  override def canFill(from: ForgeDirection, fluid: Fluid): Boolean = true

  override def canDrain(from: ForgeDirection, fluid: Fluid): Boolean = true

  override def getTankInfo(from: ForgeDirection): Array[FluidTankInfo] =
  {
    if (getPrimaryTank != null)
    {
      return Array[FluidTankInfo](getPrimaryTank.getInfo)
    }
    else
    {
      return Array[FluidTankInfo]()
    }
  }
}
