package resonant.lib.network.handle

import io.netty.buffer.ByteBuf
import net.minecraft.entity.player.EntityPlayer
import resonant.lib.network.discriminator.PacketType

/**
 * A packet receiver with a special ID to indicate special arguments.
 * @author Calclavia
 */
trait TPacketIDReceiver extends TPacketReceiver
{
  override def read(buf: ByteBuf, player: EntityPlayer, packet: PacketType)
  {
    read(buf, buf.readInt, player, packet)
  }

  def read(buf: ByteBuf, id: Int, player: EntityPlayer, packet: PacketType)
}
