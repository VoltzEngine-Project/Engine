package universalelectricity.core.grid.electric

import net.minecraftforge.common.util.ForgeDirection
import universalelectricity.core.grid.{Node, TickingGrid}
import java.lang.Byte._
import universalelectricity.api.core.grid.INodeProvider
import universalelectricity.api.core.grid.electric.IElectricNode
import scala.collection.convert.wrapAsScala._
import net.minecraft.nbt.NBTTagCompound

/**
 * The node used for electrical objects.
 *
 * @author Calclavia
 */
class ElectricNode(parent: INodeProvider) extends Node(parent) with IElectricNode
{
  var amperage = 0D
  var voltage = 0D

  private var currents: Array[Double] = null
  final val timeMultiplier = 20D

  var connectionMap = parseByte("111111", 2)
  private var capacity = 0D
  private var resistance = 0.01D

  override def getEnergyCapacity = capacity

  override def setEnergyCapacity(value: Double)
  {
    capacity = value
  }

  override def getResistance = resistance

  override def setResistance(value: Double)
  {
    resistance = value
  }

  def getCurrentEfficiency = 0.07000000000000001D

  def getParallelMultiplier = 0.5D

  /**
   * Recache the connections. This is the default connection implementation.
   */
  override def doRecache()
  {
    super.doRecache()
    this.currents = new Array[Double](connections.size)
  }

  override def canConnect(from: ForgeDirection, source: AnyRef): Boolean =
  {
    return (source.isInstanceOf[ElectricNode]) && (connectionMap & (1 << from.ordinal)) != 0
  }

  protected def calculateVoltage(deltaTime: Double)
  {
    voltage += deltaTime * amperage * getEnergyCapacity
    amperage = 0
  }

  override def getVoltage: Double =
  {
    calculateVoltage(1 / timeMultiplier)
    return voltage
  }

  def applyCurrent(amperage: Double)
  {
    calculateVoltage(1 / timeMultiplier)
    this.amperage += amperage
  }

  override def applyPower(wattage: Double)
  {
    val calculatedVoltage = Math.sqrt(voltage * voltage + 0.1 * wattage * getEnergyCapacity) - voltage
    applyCurrent(timeMultiplier * calculatedVoltage / getEnergyCapacity)
  }

  override def drawPower(wattage: Double)
  {
    val voltageSquared = voltage * voltage - 0.1 * wattage * getEnergyCapacity
    val calculatedVoltage = if (voltageSquared < 0.0D) 0.0D else Math.sqrt(voltageSquared) - this.voltage
    applyCurrent(timeMultiplier * calculatedVoltage / getEnergyCapacity)
  }

  override def getEnergy(voltageThreshold: Double): Double =
  {
    val volts = getVoltage
    val tr = 0.5D * (volts * volts - voltageThreshold * voltageThreshold) / getEnergyCapacity
    return if (tr < 0.0D) 0.0D else tr
  }

  def getEmptySpace: Double = this.getEnergyCapacity - this.getEnergy(getVoltage)

  override def update(deltaTime: Double)
  {
    calculateVoltage(deltaTime)

    connections synchronized
            {
              connections.entrySet.iterator.foreach(
                entry =>
                {
                  val dir = entry.getValue
                  val adjacent = entry.getKey
                  val totalResistance = getResistance + adjacent.getResistance
                  var current = currents(dir.ordinal)
                  val voltageDifference = voltage - adjacent.getVoltage
                  currents(dir.ordinal) += (voltageDifference - current * totalResistance) * getCurrentEfficiency
                  current += voltageDifference * getParallelMultiplier
                  applyCurrent(-current)
                  adjacent.applyCurrent(current)
                }
              )
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

  protected def newGrid: TickingGrid[ElectricNode] = new TickingGrid[ElectricNode](classOf[ElectricNode])
}