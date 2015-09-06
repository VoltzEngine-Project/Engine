package com.builtbroken.mc.lib.mod.compat.rf

import cofh.api.energy.IEnergyHandler
import com.builtbroken.mc.api.energy.IEnergyBuffer
import net.minecraftforge.common.util.ForgeDirection

/**
 * Created by Dark on 8/15/2015.
 */
trait TEnergyHandler extends IEnergyHandler {

  def receiveEnergy(from: ForgeDirection, maxReceive: Int, simulate: Boolean): Int = {

    if (maxReceive > 0 && getBattery(from) != null) {
      val added: Double = getBattery(from).addEnergyToStorage((maxReceive * RFEnergyHandler.TO_UE_FROM_RF).asInstanceOf[Int], !simulate)
      val converted: Double = added * RFEnergyHandler.TO_RF_FROM_UE

      //In theory this should prevent energy from being wasted
      if (!simulate) {
        val extra: Double = converted - converted.asInstanceOf[Int]
        getBattery(from).removeEnergyFromStorage((extra * RFEnergyHandler.TO_UE_FROM_RF).asInstanceOf[Int], true)
      }

      return converted.asInstanceOf[Int]
    }
    return 0
  }

  def extractEnergy(from: ForgeDirection, maxExtract: Int, simulate: Boolean): Int = {
    if (maxExtract > 0 && getBattery(from) != null) {
      val removed: Double = getBattery(from).removeEnergyFromStorage((maxExtract * RFEnergyHandler.TO_UE_FROM_RF).asInstanceOf[Int], !simulate)
      val converted: Double = removed * RFEnergyHandler.TO_RF_FROM_UE

      //In theory this should prevent energy from being wasted
      if (!simulate) {
        val extra: Double = converted - converted.asInstanceOf[Int]
        getBattery(from).addEnergyToStorage((extra * RFEnergyHandler.TO_UE_FROM_RF).asInstanceOf[Int], !simulate)
      }

      return converted.asInstanceOf[Int]
    }
    return 0
  }

  def getEnergyStored(from: ForgeDirection): Int = {
    if (getBattery(from) != null) {
      return (getBattery(from).getEnergyStored * RFEnergyHandler.TO_RF_FROM_UE).asInstanceOf[Int]
    }
    return 0
  }

  def getMaxEnergyStored(from: ForgeDirection): Int = {
    if (getBattery(from) != null) {
      return (getBattery(from).getMaxBufferSize * RFEnergyHandler.TO_RF_FROM_UE).asInstanceOf[Int]
    }
    return 0
  }

  protected def getBattery(from: ForgeDirection): IEnergyBuffer
}
