package universalelectricity.api

import net.minecraft.nbt.NBTTagCompound

/**
 * Can be used internally for IEnergyInterface blocks. This is optional.
 *
 * @author Calclavia
 */
class EnergyStorage
{

  protected var energy: Double = .0
  protected var capacity: Double = .0
  protected var maxReceive: Double = .0
  protected var maxExtract: Double = .0
  /**
   * A cache of the last energy stored through extract and receive.
   */
  protected var lastEnergy: Double = .0

  def this(capacity: Double, maxReceive: Double, maxExtract: Double) =
  {
    this()
    this.capacity = capacity
    this.maxReceive = maxReceive
    this.maxExtract = maxExtract
  }

  def this(capacity: Double) = this(capacity, capacity, capacity)

  def this(capacity: Double, maxTransfer: Double) = this(capacity, maxTransfer, maxTransfer)

  def readFromNBT(nbt: NBTTagCompound): EnergyStorage =
  {
    this.energy = nbt.getDouble("energy")
    return this
  }

  def writeToNBT(nbt: NBTTagCompound): NBTTagCompound =
  {
    nbt.setDouble("energy", this.getEnergy)
    return nbt
  }

  def setCapacity(capacity: Double)
  {
    this.capacity = capacity
    if (getEnergy > capacity)
    {
      energy = capacity
    }
  }

  def setMaxTransfer(maxTransfer: Double)
  {
    setMaxReceive(maxTransfer)
    setMaxExtract(maxTransfer)
  }

  def getMaxReceive: Double =
  {
    return maxReceive
  }

  def setMaxReceive(maxReceive: Double)
  {
    this.maxReceive = maxReceive
  }

  def getMaxExtract: Double =
  {
    return maxExtract
  }

  def setMaxExtract(maxExtract: Double)
  {
    this.maxExtract = maxExtract
  }

  /**
   * This function is included to allow the containing tile to directly and efficiently modify the
   * energy contained in the EnergyStorage. Do not rely on this
   * externally, as not all IEnergyHandlers are guaranteed to have it.
   *
   * @param energy
   */
  def modifyEnergyStored(energy: Double)
  {
    this.setEnergy(this.getEmptySpace + energy)
    if (this.getEnergy > this.getEnergyCapacity)
    {
      this.setEnergy(this.getEnergyCapacity)
    }
    else if (this.getEnergy < 0)
    {
      this.setEnergy(0)
    }
  }

  def receiveEnergy(receive: Double, doReceive: Boolean): Double =
  {
    val energyReceived: Double = Math.min(this.getEnergyCapacity - this.getEnergy, Math.min(this.getMaxReceive, receive))
    if (doReceive)
    {
      this.lastEnergy = this.getEnergy
      this.setEnergy(this.getEnergy + energyReceived)
    }
    return energyReceived
  }

  def receiveEnergy(doReceive: Boolean): Double =
  {
    return this.receiveEnergy(this.getMaxReceive, doReceive)
  }

  def receiveEnergy: Double =
  {
    return this.receiveEnergy(true)
  }

  def extractEnergy(extract: Double, doExtract: Boolean): Double =
  {
    val energyExtracted: Double = Math.min(this.getEnergy, Math.min(this.getMaxExtract, extract))
    if (doExtract)
    {
      this.lastEnergy = this.getEnergy
      this.setEnergy(this.getEnergy - energyExtracted)
    }
    return energyExtracted
  }

  def extractEnergy(doExtract: Boolean): Double =
  {
    return this.extractEnergy(this.getMaxExtract, doExtract)
  }

  def extractEnergy: Double =
  {
    return this.extractEnergy(true)
  }

  def checkReceive(receive: Double): Boolean =
  {
    return this.receiveEnergy(receive, false) >= receive
  }

  def checkReceive: Boolean =
  {
    return this.checkReceive(this.getMaxReceive)
  }

  def checkExtract(extract: Double): Boolean =
  {
    return this.extractEnergy(extract, false) >= extract
  }

  def checkExtract: Boolean =
  {
    return this.checkExtract(this.getMaxExtract)
  }

  def isFull: Boolean =
  {
    return this.getEnergy >= this.getEnergyCapacity
  }

  def isEmpty: Boolean =
  {
    return this.getEnergy == 0
  }

  def getLastEnergy: Double =
  {
    return this.lastEnergy
  }

  /**
   * @return True if the last energy state and the current one are either in an
   *         "empty or not empty" change state.
   */
  def didEnergyStateChange: Boolean =
  {
    return (this.getLastEnergy == 0 && this.getEnergy > 0) || (this.getLastEnergy > 0 && this.getEnergy == 0)
  }

  /**
   * Returns the amount of energy this storage can further store.
   */
  def getEmptySpace: Double =
  {
    return this.getEnergyCapacity - this.getEnergy
  }

  def getEnergy: Double =
  {
    return this.energy
  }

  /**
   * This function is included to allow for server -> client sync. Do not call this externally to
   * the containing Tile Entity, as not all IEnergyHandlers are
   * guaranteed to have it.
   *
   * @param energy
   */
  def setEnergy(energy: Double)
  {
    this.energy = energy
    if (this.getEnergy > this.getEnergyCapacity)
    {
      this.energy = this.getEnergyCapacity
    }
    else if (this.getEnergy < 0)
    {
      this.energy = 0
    }
  }

  def getEnergyCapacity: Double =
  {
    return this.capacity
  }

  override def toString: String =
  {
    return this.getClass.getSimpleName + "[" + this.getEnergy + "/" + this.getEnergyCapacity + "]"
  }

  def +=(d: Double)
  {
    this.energy += d
    if (energy >= capacity)
      energy = capacity
  }

  def +=(d: Integer)
  {
    this.energy += d
    if (energy >= capacity)
      energy = capacity
  }

  def -=(d: Double)
  {
    this.energy += d
    if (energy >= capacity)
      energy = capacity
  }

  def -=(d: Integer)
  {
    this.energy += d
    if (energy >= capacity)
      energy = capacity
  }
}