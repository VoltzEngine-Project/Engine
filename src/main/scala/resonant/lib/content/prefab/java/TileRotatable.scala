package resonant.lib.content.prefab.java

import resonant.lib.content.module.TileBlock
import net.minecraft.block.material.Material
import resonant.lib.content.prefab.TraitRotatable

/**
 * @since 27/05/14
 * @author tgame14
 */
class TileRotatable(name: String, material: Material) extends TileBlock(name, material) with TraitRotatable
{
    //def this(mat: Material) TODO: call multiple super constructors


}
