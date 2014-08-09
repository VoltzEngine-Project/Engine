package resonant.lib.content.prefab

import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.common.util.ForgeDirection
import universalelectricity.api.EnergyStorage
import universalelectricity.api.core.grid.electric.IEnergyContainer

/**
 * @author Calclavia
 */
trait TElectricStorage extends TElectric with IEnergyContainer
{
  override def energy: EnergyStorage = _

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

  override def save(nbt: NBTTagCompound)
  {
    super.save(nbt);

    if (energy != null)
    {
      energy.readFromNBT(nbt)
    }
  }

  override def load(nbt: NBTTagCompound)
  {
    super.load(nbt);

    if (energy != null)
    {
      energy.writeToNBT(nbt)
    }
  }
}
