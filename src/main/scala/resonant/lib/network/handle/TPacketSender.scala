package resonant.lib.network.handle

import io.netty.buffer.ByteBuf

/**
 * Implement this if an object can send a packet.
 * @author Calclavia
 */
trait TPacketSender
{
  def write(buf: ByteBuf)
}
