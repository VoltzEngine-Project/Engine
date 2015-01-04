package com.builtbroken.lib.prefab.fluid

import net.minecraftforge.fluids.{FluidStack, FluidTankInfo, IFluidTank}

/** Prefab designed to redirecting access to an instance of internally FluidTank instance.
  * This allows any object to be treated as a FluidTank without actually fully implementing its own code.
  *
  * Created by robert(DarkGuardsman) on 9/25/2014.
  */
trait TFluidTank extends IFluidTank with TTankProvider
{
  override def getFluid: FluidStack = if (getPrimaryTank != null) getPrimaryTank.getFluid else null

  override def getFluidAmount: Int = if (getPrimaryTank != null) getPrimaryTank.getFluidAmount else 0

  override def getCapacity: Int = if (getPrimaryTank != null) getPrimaryTank.getCapacity else 0

  override def getInfo: FluidTankInfo = if (getPrimaryTank != null) getPrimaryTank.getInfo else null

  override def fill(resource: FluidStack, doFill: Boolean): Int =
  {
    if (getPrimaryTank != null)
      return getPrimaryTank.fill(resource, doFill)
    else
      return 0
  }

  override def drain(maxDrain: Int, doDrain: Boolean): FluidStack =
  {
    if (getPrimaryTank != null)
      return getPrimaryTank.drain(maxDrain, doDrain)
    else
      return null
  }

  /** Sets the tank's capacity
    * @param capacity - capacity in milli buckets
    * @return true if the capacity equals the tanks capacity
    */
  def setCapacity(capacity: Int): Boolean =
  {
    if (getPrimaryTank != null)
    {
      getPrimaryTank.setCapacity(capacity)
      return getPrimaryTank.getCapacity == capacity
    }
    return false
  }

  /** Sets the tanks content to null */
  def clearTank(): Boolean =
  {
    if (getPrimaryTank != null)
    {
      getPrimaryTank.setFluid(null)
      return getPrimaryTank.getFluid == null
    }
    return false
  }
}
