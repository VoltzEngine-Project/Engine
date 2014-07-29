package resonant.lib.content.prefab.java

import net.minecraft.block.material.Material
import resonant.content.prefab.java.TileAdvanced
import resonant.lib.content.prefab.{TElectric, TInventory}

/**
 * @author Calclavia
 */
abstract class TileElectricInventory(material: Material) extends TileAdvanced(material : Material) with TInventory with TElectric
{

}
