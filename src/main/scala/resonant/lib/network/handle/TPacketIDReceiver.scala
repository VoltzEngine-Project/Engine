package resonant.lib.network.handle

import io.netty.buffer.ByteBuf
import net.minecraft.entity.player.EntityPlayer
import resonant.lib.network.discriminator.PacketType

/**
 * Mixin prefab designed to implement basic packet handling & helper methods
 *
 * Handles NBTTagCompound packets for methods TileEntity.readFromNBT and ISave.load
 * with packet ID 0.
 *
 * Created on 8/6/2014.
 * @author robert(Darkguardsman)
 */
@deprecated
trait TPacketIDReceiver extends TPacketReceiver with IPacketIDReceiver
{
  override final def read(buf: ByteBuf, player: EntityPlayer, packet: PacketType)
  {
    val id = buf.readInt()
    read(buf, id, player, packet)
  }
}
