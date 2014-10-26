package resonant.lib.content.prefab

import net.minecraftforge.common.util.ForgeDirection
import universalelectricity.api.core.grid.electric.IEnergyContainer

/**
 * @author Calclavia
 */
trait TElectricStorage extends TElectric with IEnergyContainer
{
  /**
   * Sets the amount of energy this unit stored.
   *
   * This function is NOT recommended for calling.
   */
  override def setEnergy(from: ForgeDirection, amount: Double) =
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
