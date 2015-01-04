package com.builtbroken.lib.prefab.tile

import net.minecraft.block.material.Material
import com.builtbroken.lib.content.prefab.TElectric
import com.builtbroken.lib.grid.energy.EnergyStorage
import com.builtbroken.lib.prefab.TEnergyBuffer

class TileElectric(material: Material) extends TileAdvanced(material: Material) with TElectric with TEnergyBuffer
{
  override def getEnergyStorage(): EnergyStorage = dcNode.getEnergyStorage()
}
