package resonant.lib.network.handle

import io.netty.buffer.ByteBuf
import net.minecraft.entity.player.EntityPlayer
import resonant.lib.network.discriminator.PacketType

/**
 *
 * Implement this if an object can receive a packet.
 */
trait TPacketReceiver
{
  /**
   * Reads a packet
   * @param buf   - data encoded into the packet
   * @param player - player that is receiving the packet
   * @param packet - The packet instance that was sending this packet.
   */
  def read(buf: ByteBuf, player: EntityPlayer, packet: PacketType)
}
