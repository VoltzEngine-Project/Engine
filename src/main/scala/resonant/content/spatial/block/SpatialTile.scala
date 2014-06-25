package resonant.content.spatial.block

import java.util

import net.minecraft.block.material.Material
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.network.Packet
import net.minecraftforge.common.util.ForgeDirection
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
  private final val playersUsing: util.HashSet[EntityPlayer] = new util.HashSet[EntityPlayer]
  protected var ticks = 0L

  override def tile: SpatialTile =
  {
    return this
  }

  /**
   * Called on the TileEntity's first tick.
   */
  def start()
  {
  }

  override def update()
  {
    if (this.ticks == 0)
    {
      start()
    }
    if (ticks >= Long.MaxValue)
    {
      ticks = 1
    }
    ticks += 1
  }

  override def getDescriptionPacket: Packet =
  {
    return ResonantEngine.instance.packetHandler.toMCPacket(new PacketAnnotation(this))
  }

  def getPlayersUsing: util.HashSet[EntityPlayer] =
  {
    return this.playersUsing
  }

  def getDirection: ForgeDirection =
  {
    return ForgeDirection.getOrientation(this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord))
  }

  def setDirection(direction: ForgeDirection)
  {
    this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, direction.ordinal, 3)
  }

}