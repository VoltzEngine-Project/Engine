package resonant.lib.content.prefab.java

import resonant.lib.content.module.TileBase
import resonant.lib.content.prefab.TElectricStorage
import net.minecraft.block.material.Material
import resonant.lib.utility.LanguageUtility

/**
 * @since 27/05/14
 * @author tgame14
 */
class TileElectricStorage(name: String, material: Material) extends TileBase(name, material) with TElectricStorage
{
  def this(newMaterial: Material) = this(LanguageUtility.decapitalizeFirst(getClass.getSimpleName.replaceFirst("Tile", "")), newMaterial)
}
