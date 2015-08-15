package com.builtbroken.mc.lib.mod.compat.rf

import cofh.api.energy.IEnergyHandler
import com.builtbroken.mc.prefab.energy.EnergyBuffer
import net.minecraftforge.common.util.ForgeDirection

/**
 * Created by Dark on 8/15/2015.
 */
trait TIEnergyHandler extends IEnergyHandler {

  def receiveEnergy(from: ForgeDirection, maxReceive: Int, simulate: Boolean): Int = {
    if (getBattery(from) != null) {

    }
    return 0
  }

  def extractEnergy(from: ForgeDirection, maxExtract: Int, simulate: Boolean): Int = {
    if (getBattery(from) != null) {

    }
    return 0
  }

  def getEnergyStored(from: ForgeDirection): Int = {
    if (getBattery(from) != null) {

    }
    return 0
  }

  def getMaxEnergyStored(from: ForgeDirection): Int = {
    if (getBattery(from) != null) {

    }
    return 0
  }

  protected def getBattery(from: ForgeDirection): EnergyBuffer
}
