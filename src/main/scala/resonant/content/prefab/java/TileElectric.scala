package resonant.lib.content.prefab.java

import net.minecraft.block.material.Material
import resonant.lib.content.prefab.TElectric
import resonant.lib.util.LanguageUtility

/**
 * @since 27/05/14
 * @author tgame14
 */
class TileElectric(name: String, material: Material) extends TileBlock(name, material) with TElectric
{
  def this(newMaterial: Material) = this(LanguageUtility.decapitalizeFirst(getClass.getSimpleName.replaceFirst("Tile", "")), newMaterial)

}
