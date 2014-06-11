package resonant.lib.content.prefab.trait

import net.minecraftforge.common.util.ForgeDirection
import net.minecraft.nbt.NBTTagCompound

/**
 * @author Calclavia
 */
trait TraitElectricStorage extends TraitElectric
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
      return energy.getEnergy()
    }
    return 0
  }

  //@Callback
  def getEnergyCapacity(from: ForgeDirection): Double =
  {
    if (energy != null)
    {
      return energy.getEnergyCapacity
    } else
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
