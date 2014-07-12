package resonant.engine.content.debug

import java.util.{ArrayList, List}

import io.netty.buffer.ByteBuf
import net.minecraft.block.material.Material
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.server.MinecraftServer
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection
import resonant.content.spatial.block.SpatialBlock
import resonant.engine.ResonantEngine
import resonant.lib.content.prefab.TRotatable
import resonant.lib.network.discriminator.{PacketTile, PacketType}
import resonant.lib.network.handle.TPacketReceiver
import resonant.lib.schematic.Schematic
import universalelectricity.core.transform.vector.Vector3

import scala.collection.convert.wrapAll._

/**
 * Automatically set up structures to allow easy debugging in creative mode.
 */
object TileCreativeBuilder
{
  def register(schematic: Schematic): Int =
  {
    registry.add(schematic)
    return registry.size - 1
  }

  final val registry: List[Schematic] = new ArrayList[Schematic]
}

class TileCreativeBuilder extends SpatialBlock(Material.iron) with TRotatable with TPacketReceiver
{
  creativeTab = CreativeTabs.tabTools
  rotationMask = Integer.parseInt("111111", 2).toByte

  /**
   * Called when the block is right clicked by the player
   */
  override def activate(player: EntityPlayer, side: Int, hit: Vector3): Boolean =
  {
    if (TileCreativeBuilder.registry.size > 0)
    {
      player.openGui(ResonantEngine.instance, -1, world, x, y, z)
      return true
    }

    return false
  }

  override def read(data: ByteBuf, player: EntityPlayer, packet: PacketType)
  {
    val world: World = player.worldObj
    if (!world.isRemote)
    {
      if (MinecraftServer.getServer().getConfigurationManager().func_152596_g(player.getGameProfile()))
      {
        try
        {
          val schematicID = data.readInt
          val size = data.readInt
          val packetTile = packet.asInstanceOf[PacketTile]
          val translation = new Vector3(packetTile.x, packetTile.y, packetTile.z)

          if (size > 0)
          {
            val map = TileCreativeBuilder.registry.get(schematicID).getStructure(ForgeDirection.getOrientation(translation.getBlockMetadata(world)), size)
            map.foreach(entry => (entry._1 + translation).setBlock(world, entry._2.left(), entry._2.right()))
          }
        }
        catch
          {
            case e: Exception =>
            {
              e.printStackTrace
            }
          }
      }
    }
  }
}