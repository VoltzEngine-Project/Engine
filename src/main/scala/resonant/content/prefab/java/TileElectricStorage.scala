package resonant.lib.content.prefab.java

import net.minecraft.block.material.Material
import resonant.content.prefab.java.{TileAdvanced, TileNode}
import resonant.lib.content.prefab.TEnergyStorage

/**
 * @since 27/05/14
 * @author tgame14
 */
class TileElectricStorage(material: Material) extends TileAdvanced(material) with TEnergyStorage
{
}
