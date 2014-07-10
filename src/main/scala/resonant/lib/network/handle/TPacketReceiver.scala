package resonant.lib.network.handle

import io.netty.buffer.ByteBuf
import net.minecraft.entity.player.EntityPlayer

/**
 *
 * Implement this if an object can receive a packet.
 */
trait TPacketReceiver extends IPacketReceiver
{
  def read(buf: ByteBuf, player: EntityPlayer, extra: AnyRef*)

  /**
   * @param data   - data encoded into the packet
   * @param player - player that sent or is receiving the packet
   * @param extra  - Extra data provided. E.g: An item would receive an ItemStack.
   */
  final def onReceivePacket(data: ByteBuf, player: EntityPlayer, extra: AnyRef*) = read(data, player, extra)
}
