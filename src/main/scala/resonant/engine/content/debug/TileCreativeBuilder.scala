package resonant.engine.content.debug

import java.util

import io.netty.buffer.ByteBuf
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import resonant.content.prefab.java.TileAdvanced
import resonant.content.prefab.scal.TRotatable
import resonant.engine.ResonantEngine
import resonant.lib.`type`.Pair
import resonant.engine.network.discriminator.{PacketTile, PacketType}
import resonant.engine.network.handle.{TPacketReceiver, TPacketSender}
import resonant.lib.schematic.SchematicRegistry
import resonant.lib.transform.vector.Vector3

import scala.collection.JavaConversions._

class TileCreativeBuilder extends TileAdvanced(Material.iron) with TRotatable with TPacketReceiver with TPacketSender
{
  //Current build task vars
  var doBuild: Boolean = false
  var buildMap: util.HashMap[Vector3, Pair[Block, Integer]] = null
  var buildLimit = 20
  //Gui vars
  var schematicID = -1
  var size = -1

  //Constructor
  creativeTab = CreativeTabs.tabTools
  rotationMask = 0x3F

  override def update()
  {
    super.update()

    if (!world.isRemote)
    {
      if (buildMap != null)
      {
        for (entry <- buildMap.entrySet())
        {
          val placement = this.toVectorWorld + entry.getKey
          placement.setBlock(entry.getValue.left, entry.getValue.right)
        }
        doBuild = false
        buildMap = null
      }
      else
      {
        val sch = SchematicRegistry.INSTANCE.getByID(schematicID)
        if (sch != null)
        {
          buildMap = sch.getStructure(getDirection, size)
        }
      }
    }
  }

  /**
   * Called when the block is right clicked by the player
   */
  override def activate(player: EntityPlayer, side: Int, hit: Vector3): Boolean =
  {
    player.openGui(ResonantEngine.instance, -1, world, x.toInt, y.toInt, z.toInt)
    return true
  }

  override def getDescPacket: PacketTile =
  {
    return new PacketTile(x.toInt, y.toInt, z.toInt, Array(1, schematicID, size, doBuild))
  }

  override def read(buf: ByteBuf, packetID: Int, packet: PacketType)
  {
    val player = packet.sender

    if (!world.isRemote)
    {
      if (packetID == 0 && player.capabilities.isCreativeMode)
      {
        schematicID = buf.readInt
        size = buf.readInt
        doBuild = true
        //TODO check for packet spamming as this could be abused by players to create a lag machine
        markUpdate()
      }
    }
    if (packetID == 1)
    {
      schematicID = buf.readInt()
      size = buf.readInt()
      doBuild = buf.readBoolean()
    }
  }

}