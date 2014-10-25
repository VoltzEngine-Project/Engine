package resonant.lib.network.handle

import cpw.mods.fml.common.network.ByteBufUtils
import io.netty.buffer.ByteBuf
import net.minecraft.entity.player.{EntityPlayer, EntityPlayerMP}
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import resonant.engine.ResonantEngine
import resonant.lib.network.discriminator.{PacketTile, PacketType}
import resonant.lib.network.netty.AbstractPacket
import universalelectricity.api.core.grid.ISave
import universalelectricity.core.transform.vector.{IVectorWorld, TVectorWorld}

/**
 * Mixin prefab designed to implement basic packet handling & helper methods
 * Created on 8/6/2014.
 * @author robert(Darkguardsman)
 */
trait TPacketReceiver extends IPacketReceiver with TVectorWorld
{
  override def read(buf: ByteBuf, player: EntityPlayer, packet: PacketType)
  {
    if (this.isInstanceOf[TileEntity])
    {
      this.asInstanceOf[TileEntity].readFromNBT(ByteBufUtils.readTag(buf))
    }
    else if (this.isInstanceOf[ISave])
    {
      this.asInstanceOf[ISave].load(ByteBufUtils.readTag(buf))
    }
  }

  /** Sends the desc packet to all players around this tile */
  def sendDescPacket
  {
    sendPacket(getDescPacket)
  }

  /** Gets the description packet */
  def getDescPacket: AbstractPacket =
  {
    var nbt: NBTTagCompound = null

    if (this.isInstanceOf[TileEntity])
    {
      nbt = new NBTTagCompound
      this.asInstanceOf[TileEntity].writeToNBT(nbt)
      return new PacketTile(x.asInstanceOf[Int], y.asInstanceOf[Int], z.asInstanceOf[Int], Array[Any](0, nbt))
    }
    else if (this.isInstanceOf[ISave])
    {
      nbt = new NBTTagCompound
      this.asInstanceOf[ISave].save(nbt)
      return new PacketTile(x.asInstanceOf[Int], y.asInstanceOf[Int], z.asInstanceOf[Int], Array[Any](0, nbt))
    }
    return null
  }

  /** Sends the packet to all players around this tile
    * @param packet - packet to send */
  def sendPacket(packet: AbstractPacket)
  {
    sendPacket(packet, 64)
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
  def sendPacketToPlayer(player: EntityPlayer, packet: AbstractPacket)
  {
    if (player.isInstanceOf[EntityPlayerMP])
    {
      ResonantEngine.instance.packetHandler.sendToPlayer(packet, player.asInstanceOf[EntityPlayerMP])
    }
  }
}
