package resonant.lib.content.prefab.java

import net.minecraft.block.material.Material
import resonant.content.spatial.block.SpatialBlock
import resonant.lib.content.prefab.TInventory

abstract class TileInventory(material: Material) extends SpatialBlock(material) with TInventory
{

}