package resonant.content.prefab.java

import net.minecraft.block.material.Material
import resonant.content.spatial.block.SpatialBlock

/**
 * Created by robert on 7/29/2014.
 */
class TileAdvanced(material : Material) extends SpatialBlock(material : Material)
{
  protected var ticks : Long = 0

  def initiate()
  {

  }

  override def update()
  {
    super.update()
    if(ticks == 0)
      initiate
    ticks += 1
    if(ticks >= Long.MaxValue - 1)
      ticks = 1
  }
}
