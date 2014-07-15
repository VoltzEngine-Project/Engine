package resonant.content.spatial.block

import java.util.{HashSet => JHashSet, Set => JSet}

import net.minecraft.block.material.Material
import net.minecraft.entity.player.EntityPlayer
import resonant.lib.network.IPlayerUsing

/**
 * All tiles inherit this class.
 *
 * @author Calclavia
 */
abstract class SpatialTile(material: Material) extends SpatialBlock(material) with IPlayerUsing with TraitTicker
{
  /**
   * The players to send packets to for machine update info.
   */
  final val playersUsing = new JHashSet[EntityPlayer]()

  override def update()
  {
    super[TraitTicker].update()
  }

  override def tile: SpatialTile = this

  override def getPlayersUsing: JSet[EntityPlayer] = playersUsing
}