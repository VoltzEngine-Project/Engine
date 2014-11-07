package resonant.lib.network.handle

import io.netty.buffer.ByteBuf
import net.minecraft.entity.player.{EntityPlayer, EntityPlayerMP}
import net.minecraft.tileentity.TileEntity
import resonant.engine.ResonantEngine
import resonant.lib.network.ByteBufWrapper.ByteBufWrapper
import resonant.lib.network.discriminator.PacketTile
import resonant.lib.network.netty.AbstractPacket
import resonant.lib.transform.vector.IVectorWorld

/**
 * Implement this if an object can send a packet with an ID
 *
 * GetDescriptionPacket will FORWARD with an packet ID of zero to the write method
 *
 * @author Calclavia
 */
trait TPacketSender extends TileEntity
{
  override def getDescriptionPacket = ResonantEngine.instance.packetHandler.toMCPacket(getPacket(0))

  /**
   * Override this method
   * Be sure to super this method or manually write the ID into the packet when sending
   */
  def write(buf: ByteBuf, id: Int)
  {
    buf <<< id
  }

  def getPacket(id: Int): PacketTile =
  {
    val packet = new PacketTile(this)
    write(packet.data, id)
    return packet
  }

  /** Sends the desc packet to all players around this tile */
  def sendDescPacket()
  {
    sendPacket(0)
  }

  def sendPacket(id: Int, distance: Double = -1)
  {
    if (distance > 0)
      sendPacket(getPacket(id), distance)
    else
      sendPacket(getPacket(id))
  }

  /**
   * Sends the packet to all players
   * @param packet - packet to send
   */
  def sendPacket(packet: AbstractPacket)
  {
    ResonantEngine.instance.packetHandler.sendToAll(packet)
  }

  /** Sends the packet to all players around this tile
    * @param packet - packet to send
    * @param distance - distance in blocks to search for players
    */
  def sendPacket(packet: AbstractPacket, distance: Double)
  {
    ResonantEngine.instance.packetHandler.sendToAllAround(packet, this.asInstanceOf[IVectorWorld], distance)
  }

  /** Sends the packet to the player. Useful for updating GUI information of those with GUIs open.
    * @param player - player to send the packet to
    * @param packet - packet to send
    */
  def sendPacket(packet: AbstractPacket, player: EntityPlayer)
  {
    if (player.isInstanceOf[EntityPlayerMP])
    {
      ResonantEngine.instance.packetHandler.sendToPlayer(packet, player.asInstanceOf[EntityPlayerMP])
    }
    else
    {
      throw new RuntimeException("[TPacketReceiver] Trying to send a packet to player from client side.")
    }
  }
}
