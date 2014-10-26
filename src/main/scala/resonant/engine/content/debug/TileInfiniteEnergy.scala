package resonant.engine.content.debug

import net.minecraft.block.material.Material
import resonant.lib.content.prefab.java.TileElectricStorage
import universalelectricity.api.core.grid.INode

class TileInfiniteEnergy extends TileElectricStorage(Material.iron)
{
  energy.setCapacity(Double.MaxValue)
  energy.setMaxTransfer(Double.MaxValue)
  ioMap = 728
//  electricNode.setCanShareEnergy(true)

  override def update()
  {
    super.update()
    energy.setEnergy(Double.MaxValue)
//    electricNode.shareEnergy()
  }

  override def getNodes(nodes: java.util.List[INode])
  {
//    nodes.add(electricNode)
  }
}
