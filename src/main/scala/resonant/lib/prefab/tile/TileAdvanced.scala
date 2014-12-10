package resonant.lib.prefab.tile

import net.minecraft.block.material.Material
import net.minecraft.network.Packet
import resonant.lib.prefab.tile.traits.TRotatable
import resonant.engine.ResonantEngine
import resonant.lib.network.netty.AbstractPacket
import resonant.lib.prefab.tile.spatial.SpatialTile

class TileAdvanced(material: Material) extends SpatialTile(material: Material) with TRotatable
{
  override def getDescriptionPacket: Packet =
  {
    ResonantEngine.instance.packetHandler.toMCPacket(getDescPacket)
  }

  def getDescPacket: AbstractPacket = null
}
