package com.builtbroken.lib.network.handle

import io.netty.buffer.ByteBuf
import net.minecraft.entity.player.{EntityPlayer, EntityPlayerMP}
import net.minecraft.tileentity.TileEntity
import com.builtbroken.mod.BBL
import com.builtbroken.lib.wrapper.ByteBufWrapper._
import com.builtbroken.lib.network.discriminator.PacketTile
import com.builtbroken.lib.network.netty.AbstractPacket
import com.builtbroken.lib.transform.vector.IVectorWorld

/**
 * Implement this if an object can send a packet with an ID
 *
 * GetDescriptionPacket will FORWARD with an packet ID of zero to the write method
 *
 * @author Calclavia
 */
trait TPacketSender extends TileEntity
{
  override def getDescriptionPacket = BBL.instance.packetHandler.toMCPacket(getPacket(0))

  /**
   * Override this method
   * Be sure to super this method or manually write the ID into the packet when sending
   */
  def write(buf: ByteBuf, id: Int)
  {
    buf <<< id
  }

  def getPacket(id: Int): PacketTile =
  {
    val packet = new PacketTile(this)
    write(packet.data, id)
    return packet
  }

  /** Sends the desc packet to all players around this tile */
  def sendDescPacket()
  {
    sendPacket(0)
  }

  def sendPacket(id: Int, distance: Double =64)
  {
    if (distance > 0)
      sendPacket(getPacket(id), distance)
    else
      sendPacket(getPacket(id))
  }

  /**
   * Sends the packet to all players
   * @param packet - packet to send
   */
  def sendPacket(packet: AbstractPacket)
  {
    if (!getWorldObj.isRemote)
      BBL.instance.packetHandler.sendToAll(packet)
    else
      throw new RuntimeException("[TPacketReceiver] Trying to send a packet to clients from client side.")
  }

  /** Sends the packet to all players around this tile
    * @param packet - packet to send
    * @param distance - distance in blocks to search for players
    */
  def sendPacket(packet: AbstractPacket, distance: Double)
  {
    if (!getWorldObj.isRemote)
      BBL.instance.packetHandler.sendToAllAround(packet, this.asInstanceOf[IVectorWorld], distance)
    else
      throw new RuntimeException("[TPacketReceiver] Trying to send a packet to clients from client side.")
  }

  /** Sends the packet to the player. Useful for updating GUI information of those with GUIs open.
    * @param player - player to send the packet to
    * @param packet - packet to send
    */
  def sendPacket(packet: AbstractPacket, player: EntityPlayer)
  {
    if (!getWorldObj.isRemote)
      BBL.instance.packetHandler.sendToPlayer(packet, player.asInstanceOf[EntityPlayerMP])
    else
      throw new RuntimeException("[TPacketReceiver] Trying to send a packet to clients from client side.")
  }
}
