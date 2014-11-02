package resonant.lib.content.prefab

import net.minecraftforge.common.util.ForgeDirection
import resonant.api.electric.EnergyStorage

/**
 * @author Calclavia
 */
trait TEnergyStorage extends TElectric
{
  var energy: EnergyStorage = _

  /**
   * Sets the amount of energy this unit stored.
   *
   * This function is NOT recommended for calling.
   */
  def setEnergy(amount: Double, from: ForgeDirection) =
  {
    if (energy != null)
      energy.setEnergy(amount)
  }

  //@Callback
  def getEnergy(from: ForgeDirection): Double =
  {
    if (energy != null)
    {
      return energy.getEnergy
    }
    return 0
  }

  //@Callback
  def getEnergyCapacity(from: ForgeDirection): Double =
  {
    if (energy != null)
    {
      return energy.getEnergyCapacity
    }
    else
      return 0
  }
}
