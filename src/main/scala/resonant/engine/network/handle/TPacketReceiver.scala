package resonant.engine.network.handle

import io.netty.buffer.ByteBuf
import net.minecraft.entity.player.EntityPlayer
import resonant.engine.network.discriminator.PacketType
import resonant.lib.transform.vector.TVectorWorld

/**
 * A trait that is applied to packet receivers
 * @author Calclavia
 */
trait TPacketReceiver extends IPacketReceiver with TVectorWorld
{
  override def read(buf: ByteBuf, player: EntityPlayer, packet: PacketType)
  {
    val id = buf.readInt()
    read(buf, id, packet)
  }

  def read(buf: ByteBuf, id: Int, packetType: PacketType)
  {

  }
}
