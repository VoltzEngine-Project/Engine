package resonant.lib.content.prefab.java

import resonant.lib.content.prefab.TElectricStorage
import net.minecraft.block.material.Material
import resonant.lib.utility.LanguageUtility
import resonant.content.spatial.block.SpatialTile

/**
 * @since 27/05/14
 * @author tgame14
 */
class TileElectricStorage(name: String, material: Material) extends SpatialTile(name, material) with TElectricStorage
{
  def this(newMaterial: Material) = this(LanguageUtility.decapitalizeFirst(getClass.getSimpleName.replaceFirst("Tile", "")), newMaterial)
}
