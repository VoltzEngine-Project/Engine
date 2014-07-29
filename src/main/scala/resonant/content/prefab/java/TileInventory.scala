package resonant.lib.content.prefab.java

import net.minecraft.block.material.Material
import resonant.content.prefab.java.TileAdvanced
import resonant.lib.content.prefab.TInventory

abstract class TileInventory(material: Material) extends TileAdvanced(material : Material) with TInventory
{

}