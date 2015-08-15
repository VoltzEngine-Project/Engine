package com.builtbroken.mc.lib.mod.compat.rf

import com.builtbroken.mc.prefab.energy.EnergyBuffer
import com.builtbroken.mc.prefab.tile.multiblock.TileMulti
import net.minecraftforge.common.util.ForgeDirection

/**
 * Created by Dark on 8/15/2015.
 */
class TileMultiEnergyRF extends TileMulti with TIEnergyHandler {

  override protected def getBattery(from: ForgeDirection): EnergyBuffer = {
    return null
  }

  //TODO maybe implement a way to set this per side per block
  override def canConnectEnergy(from: ForgeDirection): Boolean = true
}