package resonant.lib.content.prefab.java

import net.minecraft.block.material.Material
import resonant.content.spatial.block.SpatialBlock
import resonant.lib.content.prefab.TRotatable
import resonant.lib.util.LanguageUtility

/**
 * @since 27/05/14
 * @author tgame14
 */
class TileRotatable(name: String, material: Material) extends SpatialBlock(name, material) with TRotatable
{
  def this(newMaterial: Material) = this(LanguageUtility.decapitalizeFirst(getClass.getSimpleName.replaceFirst("Tile", "")), newMaterial)
}
