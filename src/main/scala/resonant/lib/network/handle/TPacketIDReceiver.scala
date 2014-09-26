package resonant.lib.network.handle

import cpw.mods.fml.common.network.ByteBufUtils
import io.netty.buffer.ByteBuf
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import resonant.engine.ResonantEngine
import resonant.lib.network.discriminator.{PacketTile, PacketType}
import resonant.lib.network.netty.AbstractPacket
import universalelectricity.api.core.grid.ISave
import universalelectricity.core.transform.vector.{IVectorWorld, IVector3, TVectorWorld}

/** Trait to be applied to scala classes to implement basic packet handling.
  * Handles desc packet by default using packet ID 0
  *
  * Created by robert(Darkguardsman) on 9/25/2014.
  */
class TPacketIDReceiver extends IPacketIDReceiver with TVectorWorld
{
  override def read(buf: ByteBuf, id: Int, player: EntityPlayer, `type`: PacketType): Boolean =
  {
    if(id == 0)
    {
      if(this.isInstanceOf[TileEntity])
      {
        this.asInstanceOf[TileEntity].readFromNBT(ByteBufUtils.readTag(buf))
        return true
      }
      else if(this.isInstanceOf[ISave])
      {
        this.asInstanceOf[ISave].load(ByteBufUtils.readTag(buf))
        return true
      }
    }
    return false
  }


  /** Gets the description packet */
  def getDescPacket : AbstractPacket =
  {
    var nbt : NBTTagCompound = null

    if(this.isInstanceOf[TileEntity])
    {
      nbt = new NBTTagCompound
      this.asInstanceOf[TileEntity].writeToNBT(nbt)
      return new PacketTile(this.asInstanceOf[IVector3], 0, nbt)
    }
    else if(this.isInstanceOf[ISave])
    {
      nbt = new NBTTagCompound
      this.asInstanceOf[ISave].save(nbt)
      return new PacketTile(this.asInstanceOf[IVector3], 0, nbt)
    }
    return null
  }

  /** Sends the desc packet to all players around this tile */
  def sendDescPacket
  {
    sendPacket(getDescPacket)
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
  def sendPacket(packet: AbstractPacket, distance : Double)
  {
    ResonantEngine.instance.packetHandler.sendToAllAround(packet, this.asInstanceOf[IVectorWorld], distance)
  }

}
