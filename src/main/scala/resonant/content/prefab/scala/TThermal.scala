package resonant.content.prefab.scala

import net.minecraft.nbt.NBTTagCompound
import resonant.content.spatial.block.SpatialBlock

/**
 * Applied to spatials that interact with heat change.
 * @author - Calclavia
 */
trait TThermal extends SpatialBlock
{
  def addThermalEnergy(energy: Long): Int =
  {
    val mass: Float = getMass
    val changeInTemperature: Int = (energy / (mass * specificHeatCapacity)).asInstanceOf[Int]
    temperature += energy
    onTemperatureChanged
    return changeInTemperature
  }

  protected def onTemperatureChanged
  {
    if (temperature > boilingPoint)
    {
      boil()
    }
    else if (temperature < meltingPoint)
    {
      melt()
    }
  }

  /**
   * Gets the mass in KG Mass (KG) = Volume (Cubic Meters) * Densitry (kg/m-cubed)
   */
  def getMass: Float =
  {
    return 1
  }

  def boilingPoint: Int

  protected def boil
  {
  }

  def meltingPoint: Int

  protected def melt
  {
  }

  def specificHeatCapacity: Int

  override def readFromNBT(nbt: NBTTagCompound)
  {
    super.readFromNBT(nbt)
    temperature = nbt.getInteger("temperature")
  }

  override def writeToNBT(nbt: NBTTagCompound)
  {
    super.writeToNBT(nbt)
    nbt.setInteger("temperature", temperature)
  }

  private var temperature: Int = 273 + 20
}