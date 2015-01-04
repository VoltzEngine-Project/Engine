package com.builtbroken.lib.prefab.tile

import net.minecraft.block.material.Material
import com.builtbroken.lib.prefab.tile.traits.TRotatable

/**
 * @since 27/05/14
 * @author tgame14
 */
class TileRotatable(material: Material) extends TileAdvanced(material: Material) with TRotatable
{
}
