package com.builtbroken.mc.lib.mod.compat.rf

import com.builtbroken.mc.api.energy.{IEnergyBuffer, IEnergyBufferProvider}
import com.builtbroken.mc.prefab.energy.EnergyBuffer
import com.builtbroken.mc.prefab.tile.multiblock.TileMulti
import net.minecraftforge.common.util.ForgeDirection

/**
 * Created by Dark on 8/15/2015.
 */
class TileMultiEnergyRF extends TileMulti with TEnergyHandler {

  override protected def getBattery(from: ForgeDirection): IEnergyBuffer = {
    if (getHost != null && getHost.isInstanceOf[IEnergyBufferProvider]) {
      return getHost.asInstanceOf[IEnergyBufferProvider].getEnergyBuffer(from);
    }
    return null
  }

  //TODO maybe implement a way to set this per side per block
  override def canConnectEnergy(from: ForgeDirection): Boolean = getBattery(from) != null
}