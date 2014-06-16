package resonant.engine.wrapper

import resonant.engine.spatial.block.SpatialBlock
import resonant.engine.component.render.RenderDynamic
import net.minecraft.tileentity.TileEntity
import universalelectricity.core.transform.vector.Vector3

/**
 * @author Calclavia
 */
@SideOnly(Side.CLIENT)
class RenderTileDummy extends TileEntitySpecialRenderer
{
  def renderTileEntityAt(tile: TileEntity, x: Double, y: Double, z: Double, f: Float)
  {
    if (tile.isInstanceOf[SpatialBlock])
    {
      val spatial = tile.asInstanceOf[SpatialBlock]
      spatial.components.get(classOf[RenderDynamic]).foreach(_.renderDynamic(new Vector3(x, y, z), false, f))
    }
  }
}