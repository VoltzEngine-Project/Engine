package com.builtbroken.mc.prefab.tile.scal

import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.fluids._

/**
 * Created by Dark on 8/9/2015.
 */
trait TFluidHandler extends IFluidHandler
{
  protected var tank : FluidTank;

  @Override
  def fill(from: ForgeDirection, resource: FluidStack, doFill: Boolean): Int =
  {
    return 0;
  }

  @Override
  def drain(from: ForgeDirection, resource: FluidStack, doDrain: Boolean): FluidStack =
  {
    return null;
  }

  @Override
  def drain(from: ForgeDirection, maxDrain: Int, doDrain: Boolean): FluidStack =
  {
    return null;
  }

  @Override
  def canFill(from: ForgeDirection, fluid: Fluid): Boolean = true;

  @Override
  def canDrain(from: ForgeDirection, fluid: Fluid): Boolean = true;

  @Override
  def getTankInfo(from: ForgeDirection): Array[FluidTankInfo] =
  {

  }
}
