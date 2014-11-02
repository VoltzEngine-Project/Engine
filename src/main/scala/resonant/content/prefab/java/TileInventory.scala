package resonant.lib.content.prefab.java

import net.minecraft.block.material.Material
import resonant.content.prefab.java.{TileAdvanced, TileNode}
import resonant.lib.content.prefab.TInventory

abstract class TileInventory(material: Material) extends TileAdvanced(material: Material) with TInventory
{
  var maxSlots = 0

  override def getSizeInventory = maxSlots

  def setSizeInventory(slots: Int)
  {
      maxSlots = slots
  }

}