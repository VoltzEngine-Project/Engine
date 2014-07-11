package resonant.content.prefab.java

import net.minecraft.block.material.Material
import resonant.content.prefab.scala.TThermal
import resonant.content.spatial.block.SpatialBlock

/**
 * @since 27/05/14
 * @author tgame14
 */
abstract class TileThermal(material: Material) extends SpatialBlock(material) with TThermal
{
}
