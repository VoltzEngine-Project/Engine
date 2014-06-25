package resonant.lib.content.prefab.java

import net.minecraft.block.material.Material
import resonant.content.spatial.block.SpatialBlock
import resonant.lib.content.prefab.{TElectric, TInventory}

/**
 * @author Calclavia
 */
class TileElectricInventory(material: Material) extends SpatialBlock(material) with TInventory with TElectric
{

}
