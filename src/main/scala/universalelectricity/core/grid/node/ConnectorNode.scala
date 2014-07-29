package universalelectricity.core.grid.node

import java.lang.Byte._

import net.minecraftforge.common.util.ForgeDirection
import universalelectricity.api.core.grid.INodeProvider
import universalelectricity.core.grid.Node

/**
 * Created by Darkguardsman on 7/29/2014.
 */
class ConnectorNode[N](parent: INodeProvider) extends Node[N](parent)
{
  var connectionMap = parseByte("111111", 2)

  override def canConnect(from: ForgeDirection, source: AnyRef): Boolean =
  {
    return source.isInstanceOf[N] && (connectionMap & (1 << from.ordinal)) != 0
  }
}
