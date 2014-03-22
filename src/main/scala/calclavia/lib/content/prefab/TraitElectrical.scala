package calclavia.lib.content.prefab

import _root_.calclavia.lib.utility.nbt.ISaveObj
import universalelectricity.api.energy.{EnergyStorageHandler, IEnergyContainer, IEnergyInterface}
import net.minecraft.item.ItemStack
import universalelectricity.api.{CompatibilityModule, UniversalClass}
import net.minecraftforge.common.ForgeDirection
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import universalelectricity.api.vector.Vector3
import net.minecraft.world.World

@UniversalClass
trait TraitElectrical extends TraitIO with IEnergyInterface with IEnergyContainer with ISaveObj
{
  protected var energy: EnergyStorageHandler

  def recharge(stack: ItemStack)
  {
    if (this.energy != null)
    {
      this.energy.extractEnergy(CompatibilityModule.chargeItem(stack, this.energy.getEnergy(), true), true)
    }

  }

  def discharge(stack: ItemStack)
  {
    if (this.energy != null)
    {
      this.energy.receiveEnergy(CompatibilityModule.dischargeItem(stack, this.energy.getEmptySpace, true), true)
    }

  }

  def canConnect(dir: ForgeDirection, obj: Object): Boolean =
  {
    if (CompatibilityModule.isHandler(obj))
    {
      if (dir == null || dir.equals(ForgeDirection.UNKNOWN))
      {
        return false
      }
      return getInputDirections.contains(dir) || getOutputDirections.contains(dir)
    }
    return false
  }

  def getEnergy(from: ForgeDirection): Long =
  {
    if (this.energy != null)
    {
      return this.energy.getEnergy()
    }
    return 0
  }

  def getEnergyCapacity(from: ForgeDirection): Long =
  {
    if (this.energy != null)
    {
      return this.energy.getEnergyCapacity
    }
    else
      return 0

  }

  def onReceiveEnergy(from: ForgeDirection, receive: Long, doReceive: Boolean): Long =
  {
    if (this.energy != null && (from == ForgeDirection.UNKNOWN || this.getInputDirections.contains(from)))
    {
      return this.energy.receiveEnergy(receive, doReceive)
    }
    return 0
  }

  def onExtractEnergy(from: ForgeDirection, extract: Long, doExtract: Boolean): Long =
  {
    if (this.energy != null && (from == ForgeDirection.UNKNOWN || this.getOutputDirections.contains(from)))
    {
      return this.energy.extractEnergy(extract, doExtract)
    }
    return 0
  }

  protected def produce(outputEnergy: Long, coord: Vector3, world: World): Long =
  {
    var usedEnergy: Long = 0;

    for (dir: ForgeDirection <- this.getOutputDirections)
    {
      if (outputEnergy > 0)
      {
        val tile: TileEntity = coord.translate(dir).getTileEntity(world)

        if (tile != null)
        {
          usedEnergy += CompatibilityModule.receiveEnergy(tile, dir.getOpposite, outputEnergy, true)
        }
      }
    }
    return usedEnergy

  }

  protected def produce(coord: Vector3, world: World): Long =
  {
    return energy.extractEnergy(produce(energy.getMaxExtract, coord, world), true)

  }

  override def save(nbt: NBTTagCompound)
  {
    if (this.energy != null)
    {
      this.energy.readFromNBT(nbt)
    }
  }

  override def load(nbt: NBTTagCompound)
  {
    if (this.energy != null)
    {
      this.energy.writeToNBT(nbt)
    }
  }


}