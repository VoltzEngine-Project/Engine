package resonant.lib.content.prefab.java

import net.minecraft.block.material.Material
import resonant.content.spatial.block.SpatialTile
import resonant.lib.content.prefab.TElectricStorage
import resonant.lib.utility.LanguageUtility

/**
 * @since 27/05/14
 * @author tgame14
 */
class TileElectricStorage(name: String, material: Material) extends SpatialTile(name, material) with TElectricStorage
{
  def this(newMaterial: Material) = this(LanguageUtility.decapitalizeFirst(getClass.getSimpleName.replaceFirst("Tile", "")), newMaterial)
}
