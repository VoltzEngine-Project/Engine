package resonant.lib.content.prefab.java

import net.minecraft.block.material.Material
import resonant.content.prefab.java.TileNode
import resonant.lib.content.prefab.TIO

abstract class TileIO(material: Material) extends TileNode(material: Material) with TIO
{
}
