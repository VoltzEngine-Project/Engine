package resonant.lib.prefab.tile

import net.minecraft.block.material.Material
import resonant.lib.content.prefab.TElectric
import resonant.lib.grid.energy.EnergyStorage
import resonant.lib.prefab.TEnergyBuffer

/**
 * @author Calclavia
 */
abstract class TileElectricInventory(material: Material) extends TileInventory(material: Material) with TElectric with TEnergyBuffer
{
  override def getEnergyStorage() : EnergyStorage = dcNode.getEnergyStorage()
}
