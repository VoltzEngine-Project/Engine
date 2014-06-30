package resonant.content.spatial.block

import java.util

import net.minecraft.block.material.Material
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.network.Packet
import resonant.api.IPlayerUsing
import resonant.engine.ResonantEngine
import resonant.lib.network.PacketAnnotation

/**
 * All tiles inherit this class.
 *
 * @author Calclavia
 */
abstract class SpatialTile(material: Material) extends SpatialBlock(material) with IPlayerUsing
{
  private final val playersUsing = new util.HashSet[EntityPlayer]
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

  override def getDescriptionPacket: Packet = ResonantEngine.instance.packetHandler.toMCPacket(new PacketAnnotation(this))

  def getPlayersUsing: util.HashSet[EntityPlayer] = playersUsing
}