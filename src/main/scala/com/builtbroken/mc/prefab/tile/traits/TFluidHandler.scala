package com.builtbroken.mc.prefab.tile.traits

import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.fluids._

/**
 * Created by Dark on 8/9/2015.
 */
trait TFluidHandler extends IFluidHandler {
  def getTank(fluid: Fluid): IFluidTank

  @Override
  def fill(from: ForgeDirection, resource: FluidStack, doFill: Boolean): Int = {
    if (resource != null && getTank(resource.getFluid) != null) {
      return getTank(resource.getFluid).fill(resource, doFill)
    }
    return 0
  }

  @Override
  def drain(from: ForgeDirection, resource: FluidStack, doDrain: Boolean): FluidStack = {
    if (resource != null && getTank(resource.getFluid) != null) {
      return getTank(resource.getFluid).drain(resource.amount, doDrain)
    }
    return null
  }

  @Override
  def drain(from: ForgeDirection, maxDrain: Int, doDrain: Boolean): FluidStack = {
    if (getTank(null) != null) {
      return getTank(null).drain(maxDrain, doDrain)
    }
    return null
  }

  @Override
  def canFill(from: ForgeDirection, fluid: Fluid): Boolean = getTank(fluid) != null

  @Override
  def canDrain(from: ForgeDirection, fluid: Fluid): Boolean = getTank(fluid) != null

  @Override
  def getTankInfo(from: ForgeDirection): Array[FluidTankInfo] = if (getTank(null) != null) Array(getTank(null).getInfo) else null
}
