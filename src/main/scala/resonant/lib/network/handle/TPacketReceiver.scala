package resonant.lib.network.handle

import io.netty.buffer.ByteBuf
import net.minecraft.entity.player.EntityPlayer
import resonant.lib.network.discriminator.PacketType

/**
 * Created by robert on 8/6/2014.
 */
trait TPacketReceiver extends IPacketReceiver
{
  override def read(buf: ByteBuf, player: EntityPlayer, packet: PacketType)
}
