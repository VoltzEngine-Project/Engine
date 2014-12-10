package resonant.lib.world.schematic

import java.util.HashMap

import net.minecraft.block.Block
import net.minecraftforge.common.util.ForgeDirection
import resonant.lib.`type`.Pair
import resonant.lib.transform.vector.Vector3

/**
 * Creates a flat box shaped world gen
 *
 * @param name - unlocalized name for translation
 * @param block - block to build with
 */
class SchematicPlate(name: String, block: Block) extends Schematic
{
  override def getName: String = name

  def getStructure(dir: ForgeDirection, size: Int): HashMap[Vector3, Pair[Block, Integer]] =
  {
    val returnMap = new HashMap[Vector3, Pair[Block, Integer]]

    for (x <- -size to size; y <- -size to size; z <- -size to size)
    {
      if ((dir.offsetX != 0 && x == 0) || (dir.offsetY != 0 && y == 0) || (dir.offsetZ != 0 && z == 0))
      {
        returnMap.put(new Vector3(x, y, z), new Pair[Block, Integer](block, dir.ordinal))
      }
    }

    return returnMap
  }
}