package resonant.lib.content.prefab.java

import net.minecraft.block.material.Material
import resonant.content.spatial.block.SpatialBlock
import resonant.lib.content.prefab.{TElectric, TInventory}
import resonant.lib.util.LanguageUtility

/**
 * @author Calclavia
 */
class TileElectricInventory(name: String, material: Material) extends SpatialBlock(name, material) with TInventory with TElectric
{
  def this(newMaterial: Material) = this(LanguageUtility.decapitalizeFirst(getClass.getSimpleName.replaceFirst("Tile", "")), newMaterial)

}
