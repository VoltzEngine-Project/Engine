package resonant.content.prefab.java

import net.minecraft.block.material.Material
import net.minecraft.network.Packet
import net.minecraftforge.common.util.ForgeDirection
import resonant.api.IRotatable
import resonant.content.spatial.block.SpatialBlock
import resonant.engine.ResonantEngine
import resonant.lib.network.netty.AbstractPacket

/**
 * Created by robert on 7/29/2014.
 */
class TileAdvanced(material : Material) extends SpatialBlock(material : Material) with IRotatable
{
  protected var ticks : Long = 0
  protected var canRotate : Boolean = true

  def initiate()
  {

  }

  override def update()
  {
    super.update()
    if(ticks == 0)
      initiate
    ticks += 1
    if(ticks >= Long.MaxValue - 1)
      ticks = 1
  }

  override def getDescriptionPacket: Packet = {
    ResonantEngine.instance.packetHandler.toMCPacket(getDescPacket)
  }

  def getDescPacket: AbstractPacket = {
    return null
  }

  def sendPacket(packet: AbstractPacket) {ResonantEngine.instance.packetHandler.sendToAllAround(packet, this) }

  override def getDirection: ForgeDirection = if(canRotate) ForgeDirection.getOrientation(getBlockMetadata) else ForgeDirection.NORTH

  override def setDirection(direction: ForgeDirection): Unit = {if(canRotate) setMeta(direction.ordinal())}
}
