package resonant.lib.content.prefab.java

import resonant.lib.content.prefab.TIO
import net.minecraft.block.material.Material
import resonant.lib.utility.LanguageUtility
import resonant.content.spatial.block.TileBlock

/**
 * @since 27/05/14
 * @author tgame14
 */
class TileIO(name: String, material: Material) extends TileBlock(name, material) with TIO
{
  def this(newMaterial: Material) = this(LanguageUtility.decapitalizeFirst(getClass.getSimpleName.replaceFirst("Tile", "")), newMaterial)

}
