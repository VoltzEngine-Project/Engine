package com.builtbroken.lib.prefab.tile

import net.minecraft.block.material.Material
import com.builtbroken.lib.content.prefab.TElectric
import com.builtbroken.lib.grid.energy.EnergyStorage
import com.builtbroken.lib.prefab.TEnergyBuffer

/**
 * @author Calclavia
 */
abstract class TileElectricInventory(material: Material) extends TileInventory(material: Material) with TElectric with TEnergyBuffer
{
  override def getEnergyStorage() : EnergyStorage = dcNode.getEnergyStorage()
}
