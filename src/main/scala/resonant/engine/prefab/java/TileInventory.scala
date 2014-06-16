package resonant.lib.content.prefab.java

import resonant.lib.content.prefab.TInventory
import net.minecraft.block.material.Material
import resonant.lib.utility.LanguageUtility
import resonant.engine.block.TileBlock

class TileInventory(name: String, material: Material) extends TileBlock(name, material) with TInventory
{
  def this(newMaterial: Material) = this(LanguageUtility.decapitalizeFirst(getClass.getSimpleName.replaceFirst("Tile", "")), newMaterial)

}