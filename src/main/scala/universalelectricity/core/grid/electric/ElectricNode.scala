package universalelectricity.core.grid.electric

import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection
import universalelectricity.core.grid.{Node, TickingGrid}
import universalelectricity.core.grid.api.INodeProvider
import java.util.Iterator
import java.util.Map
import java.lang.Byte._
import universalelectricity.core.vector.Vector3

/**
 * The node used for electrical objects.
 *
 * @author Calclavia
 */
class ElectricNode(parent: INodeProvider) extends Node[INodeProvider, TickingGrid[_], ElectricNode](parent)
{
  var amperage = 0D
  var voltage = 0D
  var connectionMap: Byte = parseByte("111111", 2)
  private var currents: Array[Double] = null
  var capacity = 0D
  final val timeMultiplier = 1 / 20D

  def getCurrentCapacity = capacity

  def getResistance = 0.01D

  def getAmperageScale = 0.07000000000000001D

  def getCondParallel = 0.5D

  /**
   * Recache the connections. This is the default connection implementation.
   */
  override def doRecache
  {
    connections.clear

    for (dir <- ForgeDirection.VALID_DIRECTIONS)
    {
      val tile: TileEntity = (position + dir) getTileEntity (world)
      if (tile.isInstanceOf[INodeProvider])
      {
        val check: ElectricNode = (tile.asInstanceOf[INodeProvider]).getNode(classOf[ElectricNode], dir.getOpposite)
        if (check != null && canConnect(dir, check) && check.canConnect(dir.getOpposite, this))
        {
          connections.put(check, dir)
        }
      }
    }

    this.currents = new Array[Double](connections.size)
  }

  override def canConnect(from: ForgeDirection, source: AnyRef): Boolean =
  {
    return (source.isInstanceOf[ElectricNode]) && (connectionMap & (1 << from.ordinal)) != 0
  }

  def world: World =
  {
    return if (parent.isInstanceOf[TileEntity]) (parent.asInstanceOf[TileEntity]).getWorldObj else null
  }

  def position: Vector3 =
  {
    return if (parent.isInstanceOf[TileEntity]) new Vector3(parent.asInstanceOf[TileEntity]) else null
  }

  protected def computeVoltage(deltaTime: Double)
  {
    this.voltage += deltaTime * amperage * getCurrentCapacity
    this.amperage = 0.0D
  }

  def getVoltage: Double =
  {
    computeVoltage(timeMultiplier)
    return this.voltage
  }

  def applyCurrent(amperage: Double)
  {
    getVoltage
    this.amperage += amperage
  }

  def applyPower(wattage: Double)
  {
    val calculatedVoltage: Double = Math.sqrt(this.voltage * this.voltage + 0.1D * wattage * getCurrentCapacity) - this.voltage
    applyCurrent(timeMultiplier * calculatedVoltage / getCurrentCapacity)
  }

  def drawPower(wattage: Double)
  {
    val p1: Double = this.voltage * this.voltage - 0.1D * wattage * getCurrentCapacity
    val calculatedVoltage: Double = if (p1 < 0.0D) 0.0D else Math.sqrt(p1) - this.voltage
    applyCurrent(timeMultiplier * calculatedVoltage / getCurrentCapacity)
  }

  def getEnergy(voltageThreshold: Double): Double =
  {
    val d: Double = getVoltage
    val tr: Double = 0.5D * (d * d - voltageThreshold * voltageThreshold) / getCurrentCapacity
    return if (tr < 0.0D) 0.0D else tr
  }

  override def update(deltaTime: Double)
  {
    computeVoltage(deltaTime)

    connections synchronized
      {
        val it: Iterator[Map.Entry[ElectricNode, ForgeDirection]] = connections.entrySet.iterator
        while (it.hasNext)
        {
          val entry: Map.Entry[ElectricNode, ForgeDirection] = it.next
          val dir: ForgeDirection = entry.getValue
          val adjacent: ElectricNode = entry.getKey
          val totalResistance: Double = getResistance + adjacent.getResistance
          var current: Double = currents(dir.ordinal)
          val voltageDifference: Double = voltage - adjacent.getVoltage
          this.currents(dir.ordinal) += (voltageDifference - current * totalResistance) * getAmperageScale
          current += voltageDifference * getCondParallel
          applyCurrent(-current)
          adjacent.applyCurrent(current)
        }
      }
  }

  override def load(nbt: NBTTagCompound)
  {
    super.load(nbt)
    voltage = nbt.getDouble("voltage")
    amperage = nbt.getDouble("amperage")
  }

  override def save(nbt: NBTTagCompound)
  {
    super.save(nbt)
    nbt.setDouble("voltage", voltage)
    nbt.setDouble("amperage", amperage)
  }

  protected def newGrid: TickingGrid[_] = new TickingGrid[ElectricNode](this, classOf[ElectricNode])
}