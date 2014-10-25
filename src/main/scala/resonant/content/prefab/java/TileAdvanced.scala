package resonant.content.prefab.java

import net.minecraft.block.material.Material
import net.minecraft.network.Packet
import resonant.content.spatial.block.SpatialTile
import resonant.engine.ResonantEngine
import resonant.lib.content.prefab.TRotatable
import resonant.lib.network.netty.AbstractPacket

class TileAdvanced(material: Material) extends SpatialTile(material: Material) with TRotatable
{
  override def getDescriptionPacket: Packet =
  {
    ResonantEngine.instance.packetHandler.toMCPacket(getDescPacket)
  }

  def getDescPacket: AbstractPacket =
  {
    return null
  }
}
