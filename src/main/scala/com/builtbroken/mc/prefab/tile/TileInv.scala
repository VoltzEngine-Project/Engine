package com.builtbroken.mc.prefab.tile

import com.builtbroken.mc.prefab.tile.traits.TInventory
import net.minecraft.block.material.Material

/** Basic implementation of inventory support for a tile with no additional code
 * Created by Dark on 9/1/2015.
 */
abstract class TileInv(name : String, material : Material) extends Tile(name, material) with TInventory
{
}
