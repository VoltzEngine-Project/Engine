package resonant.lib.network.handle

import io.netty.buffer.ByteBuf

/**
 * @author Calclavia
 */
trait TPacketIDSender extends TPacketSender
{
  override def write(buf: ByteBuf)
  {
    write(buf, 0)
  }

  /**
   * Be sure to write the ID into the packet when sending!
   */
  def write(buf: ByteBuf, id: Int)
}
