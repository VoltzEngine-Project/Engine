package universalelectricity.simulator.dc

import java.util._

import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.util.ForgeDirection
import universalelectricity.api.core.grid.{INode, INodeProvider, IUpdate}
import universalelectricity.compatibility.Compatibility
import universalelectricity.core.grid.{Grid, UpdateTicker}
import universalelectricity.core.transform.vector.Vector3
import universalelectricity.simulator.dc.component.SeriesComponent
import universalelectricity.simulator.grid.LinkedGrid
import universalelectricity.simulator.grid.parts.NetworkNode

import scala.collection.JavaConversions._

/**
 * Basic network of parts that function together to simulate a collection of co-existing tiles.
 * @author Darkguardsman, Calclavia
 */
class DCGrid extends LinkedGrid[DCNode](classOf[DCNode]) with IUpdate
{
  private var circuit: SeriesComponent = _

}