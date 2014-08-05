package resonant.lib.content.prefab.java

import net.minecraft.block.material.Material
import resonant.content.prefab.java.TileAdvanced
import resonant.lib.content.prefab.TInventory

abstract class TileInventory(material: Material) extends TileAdvanced(material : Material) with TInventory
{
  var maxSlots : Integer = 0;
  
  def setSizeInventory(slots : Integer) { maxSlots = slots}
 
  override def getSizeInventory: Int = maxSlots
  
}