package com.builtbroken.mc.test.prefab.energy

import com.builtbroken.mc.api.energy.IEnergyBuffer
import com.builtbroken.mc.lib.mod.compat.rf.TEnergyHandler
import com.builtbroken.mc.prefab.energy.EnergyBuffer
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.util.ForgeDirection

/**
 * Created by Dark on 8/15/2015.
 */
class TileTEnergyHandler extends TileEntity with TEnergyHandler {

  var buffer: EnergyBuffer = new EnergyBuffer(100)

  override protected def getBattery(from: ForgeDirection): IEnergyBuffer = buffer

  override def canConnectEnergy(from: ForgeDirection): Boolean = true
}
