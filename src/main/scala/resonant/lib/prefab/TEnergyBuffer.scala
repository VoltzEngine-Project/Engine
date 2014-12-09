package resonant.lib.prefab

import resonant.lib.grid.energy.EnergyStorage

/** Wrapper for EnergyStorage object
  * Created  on 11/8/2014.
  * @author Darkguardsman
  */
trait TEnergyBuffer
{

  def getEnergyStorage(): EnergyStorage

  /** Current energy value of the node
    * defaults to buffer.getEnergy()
    *
    * @return energy as a double
    */
  def getEnergy(): Double = getEnergyStorage.getEnergy

  /** Set the energy stored into the buffer
    * @param e - energy to set
    */
  def setEnergy(e: Double)
  {
    getEnergyStorage.setEnergy(e)
  }

  /** Sets the capacity of the buffer
    * @param e - new capacity
    */
  def setCapacity(e: Double)
  {
    getEnergyStorage.setCapacity(e)
  }

  /** @return max ammount of energy that can be stored */
  def getEnergyCapacity() : Double = getEnergyStorage.getEnergyCapacity

  /** Sets the max transfer limit of energy
    * @param e - new limit
    */
  def setMaxTransfer(e: Double)
  {
    getEnergyStorage.setMaxTransfer(e)
  }

  def setMaxReceive(e: Double)
  {
    getEnergyStorage.setMaxReceive(e)
  }

  /** @return max amount of energy that can be received */
  def getMaxReceive() : Double = getEnergyStorage.getMaxReceive

  def setMaxExtract(e: Double)
  {
    getEnergyStorage.setMaxExtract(e)
  }

  /** @return max amount of energy that can be removed */
  def getMaxExtract() : Double = getEnergyStorage.getMaxExtract

  def receiveEnergy(receive: Double, doReceive: Boolean): Double =
  {
    return getEnergyStorage.receiveEnergy(receive, doReceive)
  }

  def receiveEnergy(doReceive: Boolean): Double =
  {
    return getEnergyStorage.receiveEnergy(doReceive)
  }

  def receiveEnergy: Double =
  {
    return getEnergyStorage.receiveEnergy
  }

  def extractEnergy(extract: Double, doExtract: Boolean): Double =
  {
    return getEnergyStorage.extractEnergy(extract, doExtract)
  }

  def extractEnergy(doExtract: Boolean): Double =
  {
    return getEnergyStorage.extractEnergy(doExtract)
  }

  def extractEnergy: Double =
  {
    return getEnergyStorage.extractEnergy
  }
}
