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
import resonant.lib.network.IPacketReceiver
import resonant.lib.schematic.Schematic
import universalelectricity.core.transform.vector.Vector3

import scala.collection.convert.wrapAll._

/**
 * Automatically set up structures to allow easy debugging in creative mode.
 */
object BlockCreativeBuilder
{
  def register(schematic: Schematic): Int =
  {
    REGISTRY.add(schematic)
    return REGISTRY.size - 1
  }

  final val REGISTRY: List[Schematic] = new ArrayList[Schematic]
}

class BlockCreativeBuilder extends SpatialBlock(Material.iron) with TRotatable with IPacketReceiver
{
  creativeTab = CreativeTabs.tabTools
  rotationMask = Integer.parseInt("111111", 2).toByte

  /**
   * Called when the block is right clicked by the player
   */
  def onMachineActivated(par1World: World, x: Int, y: Int, z: Int, par5EntityPlayer: EntityPlayer, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean =
  {
    if (BlockCreativeBuilder.REGISTRY.size > 0)
    {
      par5EntityPlayer.openGui(ResonantEngine.instance, -1, par1World, x, y, z)
      return true
    }
    return false
  }

  def onReceivePacket(data: ByteBuf, player: EntityPlayer, extra: AnyRef*)
  {
    val world: World = player.worldObj
    if (!world.isRemote)
    {
      if (MinecraftServer.getServer.getConfigurationManager.isPlayerOpped(player.getCommandSenderName))
      {
        try
        {
          val schematicID: Int = data.readInt
          val size: Int = data.readInt
          val translation: Vector3 = new Vector3(extra(0).asInstanceOf[Double], extra(1).asInstanceOf[Double], extra(2).asInstanceOf[Double])
          if (size > 0)
          {
            val map = BlockCreativeBuilder.REGISTRY.get(schematicID).getStructure(ForgeDirection.getOrientation(translation.getBlockMetadata(world)), size)
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