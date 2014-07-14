package resonant.content.spatial.block

import java.util.{HashSet => JHashSet, Set => JSet}

import net.minecraft.block.material.Material
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.network.Packet
import resonant.engine.ResonantEngine
import resonant.lib.network.IPlayerUsing
import resonant.lib.network.discriminator.PacketAnnotation
import resonant.lib.network.netty.PacketManager

/**
 * All tiles inherit this class.
 *
 * @author Calclavia
 */
abstract class SpatialTile(material: Material) extends SpatialBlock(material) with IPlayerUsing
{
  /**
   * The players to send packets to for machine update info.
   */
  final val playersUsing = new JHashSet[EntityPlayer]()
  protected var ticks = 0L

  override def tile: SpatialTile = this

  /**
   * Called on the TileEntity's first tick.
   */
  def start()
  {
  }

  override def update()
  {
    if (ticks == 0)
    {
      start()
    }

    if (ticks >= Long.MaxValue)
    {
      ticks = 1
    }
    ticks += 1
  }

  override def getPlayersUsing: JSet[EntityPlayer] = playersUsing
}