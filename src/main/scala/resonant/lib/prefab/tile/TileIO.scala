package resonant.lib.prefab.tile

import net.minecraft.block.material.Material
import resonant.lib.content.prefab.TIO

abstract class TileIO(material: Material) extends TileNode(material: Material) with TIO
{
}
