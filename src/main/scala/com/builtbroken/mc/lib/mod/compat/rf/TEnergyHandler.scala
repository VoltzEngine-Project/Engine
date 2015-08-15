package com.builtbroken.mc.lib.mod.compat.rf

import cofh.api.energy.IEnergyHandler
import com.builtbroken.mc.api.energy.IEnergyBuffer
import com.builtbroken.mc.prefab.energy.EnergyBuffer
import net.minecraftforge.common.util.ForgeDirection

/**
 * Created by Dark on 8/15/2015.
 */
trait TEnergyHandler extends IEnergyHandler {

  def receiveEnergy(from: ForgeDirection, maxReceive: Int, simulate: Boolean): Int = {
    if (getBattery(from) != null) {
      val added: Int = getBattery(from).addEnergyToStorage((maxReceive * RFEnergyHandler.RECIPROCAL_RATIO).asInstanceOf[Int], !simulate);
      val converted: Double = added * RFEnergyHandler.RATIO

      //In theory this should prevent energy from being wasted
      if (!simulate) {
        val extra: Double = converted - converted.asInstanceOf[Int];
        getBattery(from).removeEnergyFromStorage((extra * RFEnergyHandler.RECIPROCAL_RATIO).asInstanceOf[Int], !simulate)
      }

      return converted.asInstanceOf[Int]
    }
    return 0
  }

  def extractEnergy(from: ForgeDirection, maxExtract: Int, simulate: Boolean): Int = {
    if (getBattery(from) != null) {
      val removed: Int = getBattery(from).removeEnergyFromStorage((maxExtract * RFEnergyHandler.RECIPROCAL_RATIO).asInstanceOf[Int], !simulate)
      val converted: Double = removed * RFEnergyHandler.RATIO

      //In theory this should prevent energy from being wasted
      if (!simulate) {
        val extra: Double = converted - converted.asInstanceOf[Int];
        getBattery(from).addEnergyToStorage((extra * RFEnergyHandler.RECIPROCAL_RATIO).asInstanceOf[Int], !simulate)
      }

      return converted.asInstanceOf[Int]
    }
    return 0
  }

  def getEnergyStored(from: ForgeDirection): Int = {
    if (getBattery(from) != null) {
      return (getBattery(from).getEnergyStored * RFEnergyHandler.RATIO).asInstanceOf[Int]
    }
    return 0
  }

  def getMaxEnergyStored(from: ForgeDirection): Int = {
    if (getBattery(from) != null) {
      return (getBattery(from).getMaxBufferSize * RFEnergyHandler.RATIO).asInstanceOf[Int]
    }
    return 0
  }

  protected def getBattery(from: ForgeDirection): IEnergyBuffer
}
