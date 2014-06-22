package resonant.lib.content.prefab

import net.minecraftforge.common.util.ForgeDirection
import net.minecraft.nbt.NBTTagCompound
import universalelectricity.api.core.grid.electric.EnergyStorage
import net.minecraft.item.ItemStack
import universalelectricity.compatibility.Compatibility

/**
 * @author Calclavia
 */
trait TElectricStorage extends TElectric
{
  protected var energy: EnergyStorage = _

  //@Callback
  def recharge(stack: ItemStack)
  {
    electricNode.drawPower(Compatibility.getHandler(stack.getItem).chargeItem(stack, electricNode.getEnergy(getVoltage), true))
  }

  //@Callback
  protected def discharge(stack: ItemStack)
  {
    electricNode.applyPower(Compatibility.getHandler(stack.getItem).dischargeItem(stack, this.energy.getEmptySpace, true))
  }

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
