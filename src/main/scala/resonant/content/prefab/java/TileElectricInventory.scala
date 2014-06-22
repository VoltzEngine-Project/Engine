package resonant.lib.content.prefab.java

import resonant.lib.content.prefab.{TElectric, TInventory}
import net.minecraft.block.material.Material
import resonant.lib.utility.LanguageUtility
import resonant.content.spatial.block.TileBlock

/**
 * @author Calclavia
 */
class TileElectricInventory(name: String, material: Material) extends TileBlock(name, material) with TInventory with TElectric
{
  def this(newMaterial: Material) = this(LanguageUtility.decapitalizeFirst(getClass.getSimpleName.replaceFirst("Tile", "")), newMaterial)

}
