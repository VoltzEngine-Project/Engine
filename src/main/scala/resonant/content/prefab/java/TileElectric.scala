package resonant.lib.content.prefab.java

import net.minecraft.block.material.Material
import resonant.content.prefab.java.TileAdvanced
import resonant.lib.content.prefab.TElectric
import resonant.lib.grid.energy.EnergyStorage
import resonant.lib.prefab.TEnergyBuffer

class TileElectric(material: Material) extends TileAdvanced(material: Material) with TElectric with TEnergyBuffer
{
  override def getEnergyStorage(): EnergyStorage = dcNode.getEnergyStorage()
}
