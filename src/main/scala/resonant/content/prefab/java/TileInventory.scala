package resonant.lib.content.prefab.java

import net.minecraft.block.material.Material
import resonant.lib.content.prefab.TInventory
import resonant.lib.util.LanguageUtility

class TileInventory(name: String, material: Material) extends TileBlock(name, material) with TInventory
{
  def this(newMaterial: Material) = this(LanguageUtility.decapitalizeFirst(getClass.getSimpleName.replaceFirst("Tile", "")), newMaterial)

}