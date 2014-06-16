package resonant.engine.spatial.block

import resonant.api.IPlayerUsing
import resonant.core.ResonantEngine
import resonant.lib.network.PacketAnnotation
import resonant.lib.utility.LanguageUtility

/**
 * All tiles inherit this class.
 *
 * @author Calclavia
 */
abstract class SpatialTile(name: String, material: Material) extends SpatialBlock(name, material) with IPlayerUsing
{
  private final val playersUsing: HashSet[EntityPlayer] = new HashSet[EntityPlayer]
  protected var ticks = 0L

  def this(newMaterial: Material) = this(LanguageUtility.decapitalizeFirst(getClass.getSimpleName.replaceFirst("Tile", "")), newMaterial)

  override def tile: TileBlock =
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
    return ResonantEngine.INSTANCE.packetHandler.toMCPacket(new PacketAnnotation(this))
  }

  def getPlayersUsing: HashSet[EntityPlayer] =
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