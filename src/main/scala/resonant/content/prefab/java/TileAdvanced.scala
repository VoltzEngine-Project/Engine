package resonant.content.prefab.java

import net.minecraft.block.material.Material
import net.minecraft.network.Packet
import resonant.content.spatial.block.SpatialTile
import resonant.engine.ResonantEngine
import resonant.lib.content.prefab.TRotatable
import resonant.lib.network.netty.AbstractPacket

/**
 * Created by robert on 7/29/2014.
 */
class TileAdvanced(material : Material) extends SpatialTile(material: Material) with TRotatable
{

  override def getDescriptionPacket: Packet = {
    ResonantEngine.instance.packetHandler.toMCPacket(getDescPacket)
  }

  def getDescPacket: AbstractPacket = {
    return null
  }

  def sendPacket(packet: AbstractPacket) {ResonantEngine.instance.packetHandler.sendToAllAround(packet, this) }
}
