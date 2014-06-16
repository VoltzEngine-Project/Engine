package resonant.lib.content.prefab.java

import net.minecraft.block.material.Material
import resonant.lib.content.prefab.TRotatable
import resonant.lib.utility.LanguageUtility
import resonant.engine.block.TileBlock

/**
 * @since 27/05/14
 * @author tgame14
 */
class TileRotatable(name: String, material: Material) extends TileBlock(name, material) with TRotatable
{
  def this(newMaterial: Material) = this(LanguageUtility.decapitalizeFirst(getClass.getSimpleName.replaceFirst("Tile", "")), newMaterial)
}
